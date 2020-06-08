package com.luban.socket;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

//网络服务端程序
public class NioServer {
    public static void main(String[] args) throws IOException {

            //得到serverSocketChannel对象   老大
            ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();

            //得到Selector对象   间谍
            Selector selector=Selector.open();

            //绑定端口
            serverSocketChannel.bind(new InetSocketAddress(9090));

            //设置非阻塞式
            serverSocketChannel.configureBlocking(false);

            //把ServerSocketChannel注册给Selector
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);   //监听连接

            //干活
         while (true){
                try {
                //监控客户端
                if(selector.select(2000)==0){
//                    System.out.println("2秒内没有客户端来连接我");
                    continue;
                }
                //得到SelectionKey对象，判断是事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
//                System.out.println(selectionKeys.size());
//                System.out.println(selector.keys().size());
                for (SelectionKey selectionKey : selectionKeys) {
                    if(selectionKey.isAcceptable()){     //连接事件
                        System.out.println("有人来连接");
                        //获取网络通道
                        SocketChannel clientSocket = serverSocketChannel.accept();
                        //设置非阻塞式
                        clientSocket.configureBlocking(false);
                        //连接上了  注册读取事件
                        clientSocket.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    }
                    if(selectionKey.isReadable()){     //读取客户端数据事件
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                        socketChannel.read(byteBuffer);
                        System.out.println(new String(byteBuffer.array()));
                        byteBuffer.put("你好".getBytes());
                        byteBuffer.flip();
                        socketChannel.write(byteBuffer);
//                        selectionKey.interestOps(SelectionKey.OP_WRITE);
                    }
                    if(selectionKey.isWritable()){
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                        byteBuffer.put("你好".getBytes());
                        byteBuffer.clear();
                        socketChannel.write(byteBuffer);
                        selectionKey.interestOps(SelectionKey.OP_READ);
                    }
                    //手动从当前集合将本次运行完的对象删除
                    selectionKeys.remove(selectionKey);
                }
            }catch (Exception e){
                System.out.println("错了");
            }
         }

    }
}
