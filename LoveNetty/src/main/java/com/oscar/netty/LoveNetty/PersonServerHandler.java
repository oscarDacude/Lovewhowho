package com.oscar.netty.LoveNetty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class PersonServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        Person person = (Person) msg;
        System.out.println(person.getName());
        System.out.println(person.getAge());
        System.out.println(person.getSex());
        Person chen = new Person();
        chen.setAge(25);
        chen.setName("chenchen");
        chen.setSex("meinv");
        ctx.writeAndFlush("aaa");
      
            try {
                // Do something with msg
            } finally {
                ReferenceCountUtil.release(msg);
            }
      
   
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server 读取数据完毕..");
        ctx.flush();//刷新后才将数据发出到SocketChannel
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}