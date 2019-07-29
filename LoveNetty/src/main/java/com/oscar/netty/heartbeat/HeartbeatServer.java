package com.oscar.netty.heartbeat;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public final class HeartbeatServer {

	static final int PORT = 8082;

	public static void main(String[] args) throws Exception {

		// Configure the server.
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<Channel>() {
						private static final int READ_IDEL_TIME_OUT = 4; // 读超时
						private static final int WRITE_IDEL_TIME_OUT = 5;// 写超时
						private static final int ALL_IDEL_TIME_OUT = 7; // 所有超时

						@Override
						protected void initChannel(Channel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new IdleStateHandler(READ_IDEL_TIME_OUT, WRITE_IDEL_TIME_OUT,
									ALL_IDEL_TIME_OUT, TimeUnit.SECONDS)); // 1
							pipeline.addLast(new HeartbeatServerHandler()); // 2
						}
					});

			// Start the server.
			ChannelFuture f = b.bind(PORT).sync();

			// Wait until the server socket is closed.
			f.channel().closeFuture().sync();
		} finally {
			// Shut down all event loops to terminate all threads.
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}