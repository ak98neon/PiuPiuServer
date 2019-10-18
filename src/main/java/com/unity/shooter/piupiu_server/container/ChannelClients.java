package com.unity.shooter.piupiu_server.container;

import com.unity.shooter.piupiu_server.util.ClientId;

import java.util.HashMap;
import java.util.Map;

public final class ChannelClients {
    private ClientId clientId;
    private Map<String, String> clientsChannels = new HashMap<>();

    public ChannelClients() {
        this.clientId = new ClientId();
    }

    public boolean isAddressExist(String socketAddress) {
        return clientsChannels.containsKey(socketAddress);
    }

    public void addNewClient(String socketAddress) {
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
