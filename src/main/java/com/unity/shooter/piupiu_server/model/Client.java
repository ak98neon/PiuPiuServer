package com.unity.shooter.piupiu_server.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.unity.shooter.piupiu_server.constants.ClientStatus;
import com.unity.shooter.piupiu_server.model.dto.ClientDataDto;
import com.unity.shooter.piupiu_server.model.dto.ClientDataResponseDto;
import com.unity.shooter.piupiu_server.service.ReceiveListener;
import com.unity.shooter.piupiu_server.util.ByteBufferUtil;

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

    private Gson gson = new GsonBuilder().setLenient().create();

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
            byte[] bytesSize = ByteBufferUtil.intToByteArray(json.length());
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
            byte[] bytes = new byte[4096];
            while (!client.isClosed()) {
                try {
                    int data = inputStream.read(bytes);
                    if (data != -1) {
                        String buffString = new String(bytes, 0, data);
                        String[] listRequest = buffString.split("\n");
                        for (String string : listRequest) {
//                            string = string.replaceAll("[.]", ",");
                            System.out.println(string);
                            ClientDataDto clientDataDto = gson.fromJson(string, ClientDataDto.class);
                            position = clientDataDto.getPosition();
                            rotation = clientDataDto.getRotation();
                            listener.dataReceive(Client.this, string);
                            if (clientDataDto.getAction().equals(ClientStatus.REMOVE.name())) {
                                listener.removeClient(Client.this);
                            }
                        }
                    }
                } catch (IOException | JsonIOException e) {
                    System.out.println(e.getMessage());
                    try {
                        client.close();
                        listener.removeClient(Client.this);
                    } catch (IOException ex) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}
