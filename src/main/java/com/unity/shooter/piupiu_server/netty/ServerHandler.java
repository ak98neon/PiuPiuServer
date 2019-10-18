package com.unity.shooter.piupiu_server.netty;

import com.unity.shooter.piupiu_server.container.ChannelClients;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class ServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private ChannelClients channelClients;

    public ServerHandler() {
        this.channelClients = new ChannelClients();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket o) {
        String hostName = o.sender().getAddress().getHostAddress();
        if (!channelClients.isAddressExist(hostName)) {
            channelClients.addNewClient(hostName);
            System.out.println("Add new client: " + hostName);
        }

        ByteBuf byteBuf = o.content();
        String in = byteBuf.toString(CharsetUtil.UTF_8);
        System.out.println(in);
        channelHandlerContext.writeAndFlush("Response from netty: HI!");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        System.out.println(cause.getMessage());
    }
}
