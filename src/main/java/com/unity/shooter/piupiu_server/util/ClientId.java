package com.unity.shooter.piupiu_server.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ClientId {
    public String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
}
