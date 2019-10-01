package com.unity.shooter.piupiu_server.model;

import com.google.gson.Gson;
import com.unity.shooter.piupiu_server.constants.ClientStatus;
import com.unity.shooter.piupiu_server.model.dto.ClientDataDto;
import com.unity.shooter.piupiu_server.model.dto.ClientDataResponseDto;
import com.unity.shooter.piupiu_server.service.ReceiveListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

public class Client {
    public volatile Position position;
    public volatile Rotation rotation;
    private Socket client;
    private ReceiveListener listener;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String id = UUID.randomUUID().toString();

    private Gson gson = new Gson();

    public Client(Socket client, ReceiveListener listener) throws IOException {
        this.client = client;
        this.listener = listener;
        inputStream = client.getInputStream();
        outputStream = client.getOutputStream();
        position = new Position();
        rotation = new Rotation();
        new ReadThread().start();
        sendStart();
    }

    private static byte[] intToByteArray(int a) {
        byte[] ret = new byte[4];
        ret[0] = (byte) (a & 0xFF);
        ret[1] = (byte) ((a >> 8) & 0xFF);
        ret[2] = (byte) ((a >> 16) & 0xFF);
        ret[3] = (byte) ((a >> 24) & 0xFF);
        return ret;
    }

    public String getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    private void sendStart() {
        ClientDataResponseDto responseDto = new ClientDataResponseDto(id, position, rotation, ClientStatus.NEW_SESSION);
        String json = gson.toJson(responseDto);
        sendToClient(json);
    }

    public void sendToClient(String json) {
        try {
            System.out.println("sendToClient");
            byte[] bytes = json.getBytes();
            byte[] bytesSize = intToByteArray(json.length());
            outputStream.write(bytesSize, 0, 4);
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            byte[] bytes = new byte[1024];
            while (!client.isClosed()) {
                try {
                    int data = inputStream.read(bytes);
                    if (data != -1) {
                        String string = new String(bytes, 0, data);
                        System.out.println(string);
                        ClientDataDto clientDataDto = gson.fromJson(string, ClientDataDto.class);
                        position = clientDataDto.getPosition();
                        rotation = clientDataDto.getRotation();
                        listener.dataReceive(Client.this, string);
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
