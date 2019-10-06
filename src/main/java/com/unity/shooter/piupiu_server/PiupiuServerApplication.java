package com.unity.shooter.piupiu_server;

import com.unity.shooter.piupiu_server.netty.NettyServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PiupiuServerApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(PiupiuServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws InterruptedException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 9092;
        new NettyServer(port).run();
    }
}
