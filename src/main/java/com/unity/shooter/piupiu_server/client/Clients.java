package com.unity.shooter.piupiu_server.client;

import com.google.gson.Gson;
import com.unity.shooter.piupiu_server.constants.Action;
import com.unity.shooter.piupiu_server.constants.ClientActionType;
import com.unity.shooter.piupiu_server.environment.GameEnvironment;
import com.unity.shooter.piupiu_server.service.ReceiveListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Clients implements ReceiveListener {
    private static Logger log = Logger.getLogger(Clients.class.getName());

    private static List<Client> clientList = new ArrayList<>();
    private Gson gson = new Gson();

    public static List<Client> getClientList() {
        return clientList;
    }

    public synchronized void addClient(Client client) {
        log.info("addClient");
        clientList.add(client);
        sendConnectNewPlayer(client);
        getAllPlayers(client);
        sendGameEnvironmentToNewPlayer(client, GameEnvironment.getEnvironment());
    }

    @Override
    public synchronized void removeClient(Client client) throws IOException {
        int index = clientList.indexOf(client);
        if (index != -1) {
            Client removeClient = clientList.get(index);
            removeClient.getClientSocketConnection().close();
            RequestData removeRequest = new RequestData(client.getId(), new Position(), new Rotation(),
                    ClientActionType.PLAYER, Action.REMOVE_CLIENT);
            sendBroadcast(client, gson.toJson(removeRequest));
            clientList.remove(client);
        }
    }

    @Override
    public synchronized void dataReceive(Client client, String data) {
        sendBroadcast(client, data);
    }

    private synchronized void sendBroadcast(Client client, String data) {
        log.info("sendBroadcast");
        clientList.stream()
                .filter(item -> !item.getId().equals(client.getId()))
                .forEach(item -> item.sendToClient(data));
    }

    private synchronized void sendConnectNewPlayer(Client client) {
        log.info("sendConnectNewPlayer");
        for (Client item : clientList) {
            if (!item.getId().equals(client.getId())) {
                RequestData responseDto = new RequestData(client.getId(), client.getPosition(),
                        client.getRotation(), ClientActionType.PLAYER, Action.NEW_CLIENT);
                String json = gson.toJson(responseDto);
                sendBroadcast(client, json);
            }
        }
    }

    private synchronized void sendGameEnvironmentToNewPlayer(Client client, List<RequestData> requestDataList) {
        log.info("sendGameEnvironmentToNewPlayer");
        requestDataList.stream()
                .map(dto -> gson.toJson(dto))
                .forEach(client::sendToClient);
    }

    private synchronized void getAllPlayers(Client client) {
        log.info("getAllPlayers");
        clientList.stream().filter(item -> item != client).map(item -> new RequestData(item.getId(), item.getPosition(),
                item.getRotation(), ClientActionType.PLAYER, Action.NEW_CLIENT))
                .map(responseDto -> gson.toJson(responseDto)).forEach(client::sendToClient);
    }
}
