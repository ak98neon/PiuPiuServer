package com.unity.shooter.piupiu_server.netty;

import com.google.gson.Gson;
import com.unity.shooter.piupiu_server.constants.ClientStatus;
import com.unity.shooter.piupiu_server.container.ChannelClients;
import com.unity.shooter.piupiu_server.model.dto.ClientDataDto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class ServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private ChannelClients channelClients;
    private Gson gson;

    public ServerHandler() {
        this.channelClients = new ChannelClients();
        this.gson = new Gson();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket o) {
        InetSocketAddress sender = o.sender();

        if (!channelClients.isAddressExist(sender)) {
            channelClients.addNewClient(sender);
            System.out.println("Add new client: " + sender);
        }

        ByteBuf byteBuf = o.content();
        String in = byteBuf.toString(CharsetUtil.UTF_8);
        System.out.println(in);

        ClientDataDto clientDataDto = gson.fromJson(in, ClientDataDto.class);

        if (clientDataDto.getAction().equals(ClientStatus.REMOVE)) {
            System.out.println("REMOVE CLIENT");
        } else if (!clientDataDto.getAction().equals(ClientStatus.NEW_SESSION)) {
            channelClients.sendToAllClients(channelHandlerContext, o, sender);
        } else {
            channelClients.startNewSession(channelHandlerContext, sender, o);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        System.out.println(cause.getMessage());
    }
}
