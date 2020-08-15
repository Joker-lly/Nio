package com.luban.netty.thirddome;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;


public class TestServerHandler extends SimpleChannelInboundHandler<String>{

     // 直译 通道组
    private static ChannelGroup group=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //channel读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Channel channel = channelHandlerContext.channel();
        group.forEach(ch->{
            //  不发送给自己
            if(channel!=ch){
                ch.writeAndFlush(channel.remoteAddress()+"："+s+"\r\n");
            }
        });
        // 向下传播 传播给下一个handler
        channelHandlerContext.fireChannelRead(s);
    }

    //channel 助手类(拦截器)的添加 用户一旦来连接 就会调用此方法 创建一个新的 channel 添加 新的pipeline
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //  这行代码的意思是 发送给在这个group中的成员  这两行代码顺序如果调换一下的话 就会发给自己， 有待实践证明
        group.writeAndFlush(channel.remoteAddress()+"加入\n");
        group.add(channel);
    }

    //channel 助手类(拦截器)移除
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        group.writeAndFlush(channel.remoteAddress()+"离开\n");
    }

    //channel活跃 通道准备就绪事件
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress()+"上线\n");
        System.out.println(group.size());
    }

    //channel不活跃  通道关闭事件
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress()+"下线\n");
    }

    //channel注册事件
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered");
        super.channelRegistered(ctx);
    }

    //channel取消注册事件
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered");
        super.channelUnregistered(ctx);
    }

    //发生异常回调
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
