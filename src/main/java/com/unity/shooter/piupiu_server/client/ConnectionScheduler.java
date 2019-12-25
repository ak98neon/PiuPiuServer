package com.unity.shooter.piupiu_server.client;

import com.google.gson.Gson;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ConnectionScheduler {
    private Gson gson;

    public ConnectionScheduler(Gson gson) {
        this.gson = gson;
    }

    @Scheduled(fixedRate = 150_000, initialDelay = 10_000)
    public void isConnectionClientAlive() {
        Clients.getClientList().forEach(x ->
        {
            try {
                x.sendToClient(gson.toJson("Check client"));
            } catch (Exception e) {
                Clients.getClientList().remove(x);
            }
        });
    }
}
