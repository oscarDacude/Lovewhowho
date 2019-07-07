package com.oscar.netty.day2;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler  extends ChannelInboundHandlerAdapter{
	
    private ByteBuf buf;
	 @Override
	    public void handlerAdded(ChannelHandlerContext ctx) {
	        buf = ctx.alloc().buffer(4); // (1)
	    }

	    @Override
	    public void handlerRemoved(ChannelHandlerContext ctx) {
	        buf.release(); // (1)
	        buf = null;
	    }
	
	  @Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg) {
		  
		    ByteBuf m = (ByteBuf) msg;
	        buf.writeBytes(m); // (2)所有数据将会被累计到buf中； 
	        m.release();

	        if (buf.readableBytes() >= 4) { // (3)通过判断数据包的大小；如果足够长将会读取内容并且处理；
	            long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
	            System.out.println(new Date(currentTimeMillis));
	            ctx.close();
	        }
	        //以上是处理粘包的方法；
	        
	   //     ByteBuf m = (ByteBuf) msg; // (1)
	        try {
	            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
	            System.out.println(new Date(currentTimeMillis));
	            ctx.close();
	        } finally {
	            m.release();
	        }
	    }

	    @Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	        cause.printStackTrace();
	        ctx.close();
	    }
}
