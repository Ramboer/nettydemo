package com.example.mqdemo.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by simon.liu on 2017/12/2.
 */
public class ReadHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Connected ip : " + ctx.channel().remoteAddress().toString());
    }
}
