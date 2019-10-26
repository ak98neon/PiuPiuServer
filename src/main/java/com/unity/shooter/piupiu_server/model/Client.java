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
import java.nio.charset.StandardCharsets;
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
        @Override
        public void run() {
            super.run();
            int sizeOfBuffer = 4096;
            byte[] bytes = new byte[sizeOfBuffer];
            int len = 0;
            while (!client.isClosed()) {
                try {
                    int data = inputStream.read(bytes);
                    if (data != -1) {
                        String buffString = new String(bytes, 0, data, StandardCharsets.US_ASCII);

                        len = parseRequest(bytes, len, buffString);
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

        private int parseRequest(byte[] bytes, int len, String buffString) {
            if (isNumeric(buffString)) {
                len = Integer.parseInt(buffString);
            } else if (buffString.length() == len
                    && buffString.startsWith("{")
                    && buffString.endsWith("}")) {
                String requestJson = new String(bytes, 0, len);
                System.out.println(requestJson);

                ClientDataDto clientDataDto = gson.fromJson(requestJson, ClientDataDto.class);
                position = clientDataDto.getPosition();
                rotation = clientDataDto.getRotation();

                listener.dataReceive(Client.this, requestJson);
                if (clientDataDto.getAction() == ClientStatus.REMOVE) {
                    listener.removeClient(Client.this);
                }
            }
            return len;
        }

        private boolean isNumeric(String strNum) {
            try {
                Double.parseDouble(strNum);
                return true;
            } catch (NumberFormatException | NullPointerException nfe) {
                return false;
            }
        }
    }
}
