package com.unity.shooter.piupiu_server.service;

import com.unity.shooter.piupiu_server.model.Client;

public interface ReceiveListener {
    public void dataReceive(Client client, String data);
}
