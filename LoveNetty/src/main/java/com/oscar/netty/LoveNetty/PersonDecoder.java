package com.oscar.netty.LoveNetty;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
 
 /**
  * 反序列化
  * 将Byte[]转换为Object
  *
  */
public class PersonDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {      
        byte[] bytes = new byte[in.readableBytes()];// 创建byte[]
        in.readBytes(bytes);// 将ByteBuf转换为byte[]
        //工具类：将byte[]转换为object
        Object obj = ByteObjConverter.byteToObject(bytes);
        out.add(obj);
    }
 
}