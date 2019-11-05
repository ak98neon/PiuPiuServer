package com.unity.shooter.piupiu_server;

import com.unity.shooter.piupiu_server.client.Client;
import com.unity.shooter.piupiu_server.client.Clients;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.ServerSocket;
import java.net.Socket;

@SpringBootApplication
public class PiupiuServerApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(PiupiuServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Clients clients = new Clients();

        ServerSocket serverSocket = new ServerSocket(16000);
        while (true) {
            System.out.println("Wait client");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");
            Client client = new Client(socket, clients);
            clients.addClient(client);
        }
    }
}
