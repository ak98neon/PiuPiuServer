package com.unity.shooter.piupiu_server.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.unity.shooter.piupiu_server.constants.ClientStatus;
import com.unity.shooter.piupiu_server.model.dto.ClientDataDto;
import com.unity.shooter.piupiu_server.service.ReceiveListener;
import com.unity.shooter.piupiu_server.util.ByteBufferUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

public class Client {
    private volatile Position position;
    private volatile Rotation rotation;
    private Socket clientSocketConnection;
    private ReceiveListener listener;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String id = UUID.randomUUID().toString();

    private Gson gson = new GsonBuilder().setLenient().create();

    public Client(Socket clientSocketConnection, ReceiveListener listener) throws IOException {
        this.clientSocketConnection = clientSocketConnection;
        this.listener = listener;
        inputStream = clientSocketConnection.getInputStream();
        outputStream = clientSocketConnection.getOutputStream();
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

    public Rotation getRotation() {
        return rotation;
    }

    private void sendStart() {
        ClientDataDto responseDto = new ClientDataDto(id, position, rotation, ClientStatus.NEW_SESSION);
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
        private static final String DELIMETER = "|";

        @Override
        public void run() {
            super.run();
            int sizeOfBuffer = 4096;
            byte[] bytes = new byte[sizeOfBuffer];
            byte[] patternOfDelimeter = DELIMETER.getBytes();

            while (!clientSocketConnection.isClosed()) {
                try {
                    int data = inputStream.read(bytes);
                    if (data != -1) {
                        for (int i = 0; i < bytes.length; i++) {
                            if (patternOfDelimeter[0] == bytes[i]) {
                                bytes[i] = 0;
                                parseRequest(bytes, i);
                            }
                        }
                    }
                } catch (IOException | JsonIOException e) {
                    System.out.println(e.getMessage());
                    try {
                        clientSocketConnection.close();
                        listener.removeClient(Client.this);
                    } catch (IOException ex) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }

        private void parseRequest(byte[] bytes, int indexOfDelimeter) {
            String requestJson = new String(bytes, 0, indexOfDelimeter);
            System.out.println(requestJson);

            ClientDataDto clientDataDto = gson.fromJson(requestJson, ClientDataDto.class);
            position = clientDataDto.getPosition();
            rotation = clientDataDto.getRotation();

            listener.dataReceive(Client.this, requestJson);
            if (clientDataDto.getAction() == ClientStatus.REMOVE) {
                listener.removeClient(Client.this);
            }
        }
    }
}
