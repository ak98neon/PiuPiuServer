package com.unity.shooter.piupiu_server.model;

public class Client {
    private String id;
    private Position position;
    private Rotation rotation;

    public Client() {
    }

    public Client(String id, Position position, Rotation rotation) {
        this.id = id;
        this.position = position;
        this.rotation = rotation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
