package com.unity.shooter.piupiu_server.client;

import com.google.gson.Gson;
import com.unity.shooter.piupiu_server.constants.ClientStatus;
import com.unity.shooter.piupiu_server.service.ReceiveListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Clients implements ReceiveListener {
    private static Logger log = Logger.getLogger(Clients.class.getName());

    private List<Client> clientList;
    private Gson gson = new Gson();

    public Clients() {
        clientList = new ArrayList<>();
    }

    public synchronized void addClient(Client client) {
        log.info("addClient");
        clientList.add(client);
        sendConnectNewPlayer(client);
        getAllPlayers(client);
    }

    @Override
    public synchronized void removeClient(Client client) throws IOException {
        int index = clientList.indexOf(client);
        if (index != -1) {
            Client removeClient = clientList.get(index);
            removeClient.getClientSocketConnection().close();
            clientList.remove(client);
        }
    }

    @Override
    public synchronized void dataReceive(Client client, String data) {
        sendBroadcast(client, data);
    }

    private synchronized void sendBroadcast(Client client, String data) {
        log.info("sendBroadcast");
        clientList.stream().filter(item -> !item.getId().equals(client.getId())).forEach(item -> item.sendToClient(data));
    }

    private synchronized void sendConnectNewPlayer(Client client) {
        log.info("sendConnectNewPlayer");
        for (Client item : clientList) {
            if (!item.getId().equals(client.getId())) {
                ClientData responseDto = new ClientData(client.getId(), client.getPosition(),
                        client.getRotation(), ClientStatus.NEW_CLIENT);
                String json = gson.toJson(responseDto);
                sendBroadcast(client, json);
            }
        }
    }

    private synchronized void getAllPlayers(Client client) {
        log.info("getAllPlayers");
        clientList.stream().filter(item -> item != client).map(item -> new ClientData(item.getId(), item.getPosition(),
                item.getRotation(), ClientStatus.NEW_CLIENT)).map(responseDto -> gson.toJson(responseDto)).forEach(client::sendToClient);
    }
}
