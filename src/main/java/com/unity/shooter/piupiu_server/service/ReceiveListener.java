package com.unity.shooter.piupiu_server.service;

public interface ReceiveListener {
    void dataReceive(Client client, String data);

    void removeClient(Client client);
}
