package com.unity.shooter.piupiu_server.container;

import com.google.gson.Gson;
import com.unity.shooter.piupiu_server.model.dto.ClientDataDto;
import com.unity.shooter.piupiu_server.util.ClientId;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public final class ChannelClients {
    private ClientId clientId;
    private Map<InetSocketAddress, String> clientsChannels = new HashMap<>();

    public ChannelClients() {
        this.clientId = new ClientId();
    }

    public boolean isAddressExist(InetSocketAddress socketAddress) {
        return clientsChannels.containsKey(socketAddress);
    }

    public void addNewClient(InetSocketAddress clientAddress) {
        String id = clientId.generateUniqueId();
        clientsChannels.put(clientAddress, id);
    }

    public void startNewSession(ChannelHandlerContext channelHandlerContext, InetSocketAddress clientAddress,
                                DatagramPacket packet) {
        Gson gson = new Gson();
        ByteBuf byteBuf = packet.content();
        String in = byteBuf.toString(CharsetUtil.UTF_8);

        ClientDataDto clientDataDto = gson.fromJson(in, ClientDataDto.class);
        clientDataDto.setId(clientId.generateUniqueId());

        String resp = gson.toJson(clientDataDto);
        ByteBuf responseByte = channelHandlerContext.alloc().buffer();
        responseByte.writeBytes(resp.getBytes());

        DatagramPacket datagramPacket = new DatagramPacket(responseByte, clientAddress);
        sendToClient(channelHandlerContext, clientAddress, datagramPacket);
    }

    public void sendToAllClients(ChannelHandlerContext channelHandlerContext, DatagramPacket packet,
                                 InetSocketAddress sender) {
        for (InetSocketAddress clientAddress : clientsChannels.keySet()) {
            if (clientAddress != sender) {
                sendToClient(channelHandlerContext, clientAddress, packet);
            }
        }
    }

    private void sendToClient(ChannelHandlerContext channelHandlerContext, InetSocketAddress clientAddress,
                              DatagramPacket packet) {
        ByteBuf content = packet.content();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(clientAddress.getHostName(), 16000);
        DatagramPacket datagramPacket = new DatagramPacket(content, inetSocketAddress);
        System.out.println("Send to: " + inetSocketAddress);
        channelHandlerContext.writeAndFlush(datagramPacket.retain());
    }
}
