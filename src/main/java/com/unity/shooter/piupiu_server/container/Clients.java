package com.unity.shooter.piupiu_server.container;

import com.google.gson.Gson;
import com.unity.shooter.piupiu_server.constants.ClientStatus;
import com.unity.shooter.piupiu_server.model.Client;
import com.unity.shooter.piupiu_server.model.dto.ClientDataResponseDto;
import com.unity.shooter.piupiu_server.service.ReceiveListener;

import java.util.ArrayList;
import java.util.List;

public class Clients implements ReceiveListener {
    private List<Client> clients;
    private Gson gson = new Gson();

    public Clients() {
        clients = new ArrayList<>();
    }

    public void addClient(Client client) {
        System.out.println("addClient");
        clients.add(client);
        sendConnectNewPlayer(client);
        getAllPlayers(client);
    }

    @Override
    public void removeClient(Client client) {
        clients.remove(client);
    }

    @Override
    public void dataReceive(Client client, String data) {
        sendBroadcast(client, data);
    }

    public void sendBroadcast(Client client, String data) {
        System.out.println("sendBroadcast");
        for (Client item : clients) {
            if (!item.getId().equals(client.getId())) {
                item.sendToClient(data);
            }
        }
    }

    private void sendConnectNewPlayer(Client client) {
        System.out.println("sendConnectNewPlayer");
        for (Client item : clients) {
            ClientDataResponseDto responseDto = new ClientDataResponseDto(client.getId(), client.getPosition(),
                    client.getRotation(), ClientStatus.NEW_CLIENT);
            String json = gson.toJson(responseDto);
            sendBroadcast(client, json);
        }
    }

    private void getAllPlayers(Client client) {
        System.out.println("getAllPlayers");
        for (Client item : clients) {
            if (item != client) {
                ClientDataResponseDto responseDto = new ClientDataResponseDto(item.getId(), item.getPosition(),
                        item.getRotation(), ClientStatus.NEW_CLIENT);
                String json = gson.toJson(responseDto);
                client.sendToClient(json);
            }
        }
    }
}
