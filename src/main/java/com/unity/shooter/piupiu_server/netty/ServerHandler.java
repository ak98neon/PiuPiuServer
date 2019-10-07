package com.unity.shooter.piupiu_server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.net.SocketAddress;

public class ServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket o) throws Exception {
        SocketAddress remoteAddress = o.sender();

        ByteBuf byteBuf = o.content();
        String in = byteBuf.toString(CharsetUtil.UTF_8);
        System.out.println(in);
        channelHandlerContext.writeAndFlush("Response from netty: HI!");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
    }
}
