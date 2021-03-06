package com.unity.shooter.piupiu_server.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.unity.shooter.piupiu_server.constants.Action;
import com.unity.shooter.piupiu_server.constants.ClientActionType;
import com.unity.shooter.piupiu_server.service.ReceiveListener;
import com.unity.shooter.piupiu_server.util.ByteBufferUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.logging.Logger;

public class Client {
    private static Logger log = Logger.getLogger(Client.class.getName());

    private Position position;
    private Rotation rotation;
    private Socket clientSocketConnection;
    private ReceiveListener listener;
    private BufferedInputStream inputStream;
    private BufferedOutputStream outputStream;
    private String id = UUID.randomUUID().toString();

    private Gson gson = new GsonBuilder().setLenient().create();

    public Client(Socket clientSocketConnection, ReceiveListener listener) throws IOException {
        this.clientSocketConnection = clientSocketConnection;
        this.listener = listener;
        InputStream input = clientSocketConnection.getInputStream();
        this.inputStream = new BufferedInputStream(input, 1024 * 1024);
        OutputStream output = clientSocketConnection.getOutputStream();
        this.outputStream = new BufferedOutputStream(output, 1024 * 1024);
        position = new Position();
        rotation = new Rotation();
        new ReadThread().start();
        sendStart();
    }

    private void sendStart() {
        RequestData responseDto = new RequestData(id, position, rotation, ClientActionType.PLAYER, Action.NEW_SESSION);
        String json = gson.toJson(responseDto);
        sendToClient(json);
    }

    public void sendToClient(String json) {
        try {
            log.info("sendToClient");
            byte[] bytes = json.getBytes();
            byte[] bytesSize = ByteBufferUtil.intToByteArray(json.length());
            outputStream.write(bytesSize, 0, 4);
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
        } catch (IOException e) {
            log.warning(e.getMessage());
        }
    }

    public Position getPosition() {
        return position;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public Socket getClientSocketConnection() {
        return clientSocketConnection;
    }

    public String getId() {
        return id;
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
                                break;
                            }
                        }
                    }
                } catch (IOException | JsonIOException e) {
                    log.info(e.getMessage());
                    try {
                        clientSocketConnection.close();
                        listener.removeClient(Client.this);
                    } catch (IOException ex) {
                        log.info(e.getMessage());
                    }
                }
            }
        }

        private void parseRequest(byte[] bytes, int indexOfDelimeter) {
            String requestJson = new String(bytes, 0, indexOfDelimeter);
            log.info(requestJson);

            try {
                if (requestJson.contains(Action.REMOVE_CLIENT.name())) {
                    listener.dataReceive(Client.this, requestJson);
                    listener.removeClient(Client.this);
                } else {
                    RequestData requestData = gson.fromJson(requestJson, RequestData.class);
                    position = requestData.getPosition();
                    rotation = requestData.getRotation();

                    listener.dataReceive(Client.this, requestJson);
                }
            } catch (JsonSyntaxException | IOException e) {
                log.warning("Bad string " + requestJson);
            }
        }
    }
}
