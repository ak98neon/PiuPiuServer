package com.unity.shooter.piupiu_server.service;

import com.unity.shooter.piupiu_server.model.Client;

import java.io.IOException;

public interface ReceiveListener {
    void dataReceive(Client client, String data);

    void removeClient(Client client) throws IOException;
}
