package com.unity.shooter.piupiu_server.client;

import com.google.gson.Gson;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

@Component
@EnableScheduling
public class ConnectionScheduler {
    private static Logger log = Logger.getLogger(ConnectionScheduler.class.getName());
    private Gson gson;

    public ConnectionScheduler(Gson gson) {
        this.gson = gson;
    }

    @Scheduled(fixedRate = 30_000, initialDelay = 10_000)
    public void isConnectionClientAlive() {
        Clients.getClientList().forEach(x ->
        {
            try {
                x.sendToClient(gson.toJson("Check client"));
            } catch (Exception e) {
                log.warning(e.getMessage());
                try {
                    new Clients().removeClient(x);
                } catch (IOException ex) {
                    log.warning(ex.getMessage());
                    Clients.getClientList().remove(x);
                }
            }
        });
    }
}
