package com.unity.shooter.piupiu_server.container;

import com.unity.shooter.piupiu_server.util.ClientId;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

public final class ChannelClients {
    private ClientId clientId;
    private Map<SocketAddress, String> clientsChannels = new HashMap<>();

    public ChannelClients() {
        this.clientId = new ClientId();
    }

    public boolean isAddressExist(SocketAddress socketAddress) {
        return clientsChannels.containsKey(socketAddress);
    }

    public void addNewClient(SocketAddress socketAddress) {
        String id = clientId.generateUniqueId();
        clientsChannels.put(socketAddress, id);
    }

    public void sendInfoAboutNewClient() {
    }

    public void sendToAllClients() {
    }

    public void sendToClient() {
    }
}
