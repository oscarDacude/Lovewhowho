package com.oscar.netty.heartbeat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {

	// Return a unreleasable view on the given ByteBuf
	// which will just ignore release and retain calls.
	private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled
			.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat", CharsetUtil.UTF_8)); // 1定义了心跳时，要发送的内容

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

		if (evt instanceof IdleStateEvent) { // 2 判断是否是 IdleStateEvent 事件，是则处理
			IdleStateEvent event = (IdleStateEvent) evt;
			String type = "";
			if (event.state() == IdleState.READER_IDLE) {
				type = "read idle";
			} else if (event.state() == IdleState.WRITER_IDLE) {
				type = "write idle";
			} else if (event.state() == IdleState.ALL_IDLE) {
				type = "all idle";
			}

			ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE); // 3将心跳内容发送给客户端

			System.out.println(ctx.channel().remoteAddress() + "超时类型：" + type);
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}
}