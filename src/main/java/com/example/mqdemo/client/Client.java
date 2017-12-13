package com.example.mqdemo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by simon.liu on 2017/12/2.
 */
public class Client {


        private String serverIp = "112.91.86.162";

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
                    Bootstrap b = new Bootstrap();
                    bossGroup = new NioEventLoopGroup();
                    b.group(bossGroup);
                    b.channel(NioSocketChannel.class);
                    b.option(ChannelOption.SO_KEEPALIVE, true);
                    b.handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("TransMsgEncoder", new StringEncoder());
                            pipeline.addLast("TransMsgDecoder", new StringDecoder());
                            //pipeline.addLast("AdapterHandler", new ReadHandler());
                        }
                    });
                    initServer();
                    ChannelFuture f = b.connect(serverIp, serverPort).sync();
                    channel = f.channel();

                    channel.closeFuture().sync();
                } catch (Exception e) {
                } finally {
                    shutdown();
                }
            }).start();


        }

        /**
         * shutdown server
         */
        public void shutdown() {
            if (null == channel) {
            } else {
                channel.closeFuture().syncUninterruptibly();
            }
            bossGroup.shutdownGracefully();
            bossGroup = null;
            workerGroup = null;
            channel = null;
        }

        public void setServerIp(String serverIp) {
            this.serverIp = serverIp;
        }
}
