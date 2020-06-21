package com.luban.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * java.io中最为核心的概念是流（stream），面向流编程，java中，一个流要么是输入流，要么是输出流
 * java.nio中拥有3个核心概念：selector，channel，buffer；java.nio中，面向块(block)或是缓冲区(buffer)编程的，
 * buffer本身就是一块内存，实际就是个数组，数据的读.写都是通过buffer来实现的。
 *
 * java中的原生8种基本数据类型都有各自对应的buffer类型，（除Boolean外），如IntBuffer,CharBuffer,ByteBuffer,LongBuffer,ShortBuffer
 * 所有数据的读写都是通过buffer来进行的，永远不会出现直接channel中直接写入，读取数据
 * 与stream不同的是，channel是双向的，一个流只可能是InputStream或是OutputStream，channel则是双向的，channel打开后可以进行读又可以进行写
 *
 * capacity： 最大容量，它永远不可能为负数，并且是不会变化的
 * limit：  限制，它永远不可能为负数，并且不会大于capacity
 * position：下一个读或写的位置，它永远不可能为负数，并且不会大于limit
 *

 * selector：
 * 1.一个channel注册到selector上，这个动作是通过selectionKey来表示的；
 * 一个selector会维护三种selectionKey的集合
 * key set  表示注册到selector上面所有的selectionKey，通过keys()方法返回   全集
 * selected-key set  表示感兴趣的selectionKey  通过selectedKeys()方法返回  子集
 *
 * buffer.put()  往此buffer中放置元素（往数组中写）
 * buffer.get()  从buffer中取出元素
 *
 * channel.read(byteBuffer);  从通道中读取数据放置到buffer当中  对于buffer来说是写
 * channel.write(byteBuffer); 从buffer中读取数据写入通道中，   对于buffer来说是读
 *
 *
 * selectionKey
 * 要点
 * 是一个抽象类,表示selectableChannel在Selector中注册的标识.每个Channel向Selector注册时,都将会创建一个selectionKey
 * 选择键将Channel与Selector建立了关系,并维护了channel事件.
 * 可以通过cancel方法取消键,取消的键不会立即从selector中移除,而是添加到cancelledKeys中,在下一次select操作时移除它.所以在调用某个key时,需要使用isValid进行校验.
 *
 * 操作集
 *
 * interest 集合:当前channel感兴趣的操作,此类操作将会在下一次选择器select操作时被交付,可以通过selectionKey.interestOps(int)进行修改.
 * ready 集合:表示此选择键上,已经就绪的操作.每次select时,选择器都会对ready集合进行更新;外部程序无法修改此集合.
 *
 * 操作属性
 *
 * OP_ACCEPT:连接可接受操作,仅ServerSocketChannel支持
 * OP_CONNECT:连接操作,Client端支持的一种操作
 * OP_READ/OP_WRITE
 *
 * 0表示什么？
 *
 * 这些opts都不为0，如果向selector之中register一个为“0”的opts，表示此channel不关注任何类型的事件。（言外之意，register方法只是获取一个selectionKey，具体这个Channel对何种事件感兴趣，可以在稍后操作）
 *
 * 方法列表
 * public abstract SelectableChannel channel():返回此选择键所关联的通道.即使此key已经被取消,仍然会返回.
 * public abstract Selector selector():返回此选择键所关联的选择器,即使此键已经被取消,仍然会返回.
 * public abstract boolean isValid():检测此key是否有效.当key被取消,或者通道被关闭,或者selector被关闭,都将导致此key无效.在AbstractSelector.removeKey(key)中,会导致selectionKey被置为无效.
 * public abstract void cancel():请求将此键取消注册.一旦返回成功,那么该键就是无效的,被添加到selector的cancelledKeys中.cancel操作将key的valid属性置为false,并执行selector.cancel(key)(即将key加入cancelledkey集合)
 * public abstract int interesOps():获得此键的interes集合.
 * public abstract SelectionKey interestOps(int ops):将此键的interst设置为指定值.此操作会对ops和channel.validOps进行校验.如果此ops不会当前channel支持,将抛出异常.
 * public abstract int readyOps():获取此键上ready操作集合.即在当前通道上已经就绪的事件.
 * public final boolean isReadable(): 检测此键是否为"read"事件.等效于:k.,readyOps() & OP_READ != 0;还有isWritable(),isConnectable(),isAcceptable()
 * public final Object attach(Object ob):将给定的对象作为附件添加到此key上.在key有效期间,附件可以在多个ops事件中传递.
 * public final Object attachment():获取附件.一个channel的附件,可以再当前Channel(或者说是SelectionKey)生命周期中共享,但是attachment数据不会作为socket数据在网络中传输.
 */
//聊天室服务端
public class ChatService {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private long timeout=2000;


    public ChatService(){
        try {
            //服务端channel
            serverSocketChannel=ServerSocketChannel.open();

            //选择器对象
            selector=Selector.open();

            //绑定端口
            serverSocketChannel.bind(new InetSocketAddress(9090));

            //设置非阻塞式
            serverSocketChannel.configureBlocking(false);

            //把ServerSocketChannel注册给Selector
            SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//监听连接

            System.out.println("服务端准备就绪");

            start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void start() throws Exception{
        int count=0;
        long start=System.nanoTime();
        //干活
        while (true){
            //监控 客户端
//            if(selector.select(timeout)==0){
//                System.out.println("2秒内没有客户端来连接我");
//                continue;
//            }
            selector.select(timeout);
//            System.out.println("2秒了");
            long end=System.nanoTime();
            if(end-start>= TimeUnit.MILLISECONDS.toNanos(timeout)){
                count=1;
            }else{
                count++;
            }

            if(count>=10){
                System.out.println("有可能发生空轮询"+count+"次");
                rebuildSelector();
                count=0;
                selector.selectNow();
                continue;
            }
            //得到SelectionKey对象，判断是事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey=iterator.next();
                if(selectionKey.isAcceptable()){     //连接事件
                    //获取网络通道
                    SocketChannel accept = serverSocketChannel.accept();
                    //设置非阻塞式
                    accept.configureBlocking(false);
                    //连接上了  注册读取事件
                    accept.register(selector,SelectionKey.OP_READ);
                    System.out.println(accept.getRemoteAddress().toString()+"上线了");
                }
                if(selectionKey.isReadable()){     //读取客户端数据事件
                    //读取客户端发来的数据
                    readClientData(selectionKey);
                }
                //手动从当前集合将本次运行完的soket，并不是将该socket 从轮询中剔除
                //selectionKey.cancel(); 这个方法才是将该socket删除
                iterator.remove();
            }
        }
    }

    private void rebuildSelector() throws IOException {
        Selector newSelector=Selector.open();
        Selector oldSelect=selector;
        for (SelectionKey selectionKey : oldSelect.keys()) {
            int i = selectionKey.interestOps();
            selectionKey.cancel();
            selectionKey.channel().register(newSelector,i);
        }
        selector=newSelector;
        oldSelect.close();
    }

    //读取客户端发来的数据
    private void readClientData(SelectionKey selectionKey) throws IOException {
        System.out.println("读取客户端发来的数据");
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int read = socketChannel.read(byteBuffer);
        byteBuffer.flip();
        if(read>0){
            byte[] bytes=new byte[read];
            byteBuffer.get(bytes,0,read);
            //读取了数据  广播
            String s = new String(bytes,"utf-8");
            writeClientData(socketChannel,s);
        }
    }

    //广播  将读取的数据群发
    private void writeClientData(SocketChannel socketChannel,String s) throws IOException {
        Set<SelectionKey> keys = selector.keys();
        for (SelectionKey key : keys) {
            if(key.isValid()){
                SelectableChannel channel = key.channel();
                if(channel instanceof  SocketChannel){
                    SocketChannel socketChannel1= (SocketChannel) channel;
                    // 去除自己
                    if(channel!=socketChannel){
                        ByteBuffer wrap = ByteBuffer.wrap(s.getBytes());
                        socketChannel1.write(wrap);
                    }
                }
            }
        }
    }


    public static void main(String[] args) throws Exception {
        new ChatService().start();
    }


}
