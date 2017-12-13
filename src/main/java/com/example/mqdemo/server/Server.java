/*
 * Copyright (c) 2017. For Intelligent Group.
 */

package com.example.mqdemo.server;

import com.example.mqdemo.handler.ReadHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Garen.Pang on 2017/10/30.
 */
public class Server {

	private static Logger logger = LoggerFactory.getLogger(Server.class);

	private String serverIp = "172.11.200.200";

	private int serverPort = 9876;

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private Channel channel;

	/**
	 * CKServer
	 */
	public void initServer() {
	}

	/**
	 * Start the DA Server for TCP.
	 */
	public void start() {
		new Thread(() -> {
			try {
				logger.info("Starting DA Server...");
				ServerBootstrap b = new ServerBootstrap();
				bossGroup = new NioEventLoopGroup();
				workerGroup = new NioEventLoopGroup();
				b.group(bossGroup, workerGroup);
				b.channel(NioServerSocketChannel.class);
				b.option(ChannelOption.SO_KEEPALIVE, true);

				b.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						ChannelPipeline pipeline = socketChannel.pipeline();
						pipeline.addLast("TransMsgEncoder", new StringEncoder());
						pipeline.addLast("TransMsgDecoder", new StringDecoder());
						pipeline.addLast("AdapterHandler", new ReadHandler());

					}
				});
				initServer();
				ChannelFuture f = b.bind(serverIp, serverPort).sync();
				logger.info("Binding ip:{}, port:{}.", serverIp, serverPort);
				channel = f.channel();
				channel.closeFuture().sync();

			} catch (Exception e) {
				logger.error("Unexpected exception from Client " + e.getMessage());
			} finally {
				shutdown();
			}
		}).start();

	}

	/**
	 * shutdown server
	 */
	public void shutdown() {
		logger.info("destroy server resources");
		if (null == channel) {
			logger.error("server channel is null");
		} else {
			channel.closeFuture().syncUninterruptibly();
		}
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
		bossGroup = null;
		workerGroup = null;
		channel = null;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
}
