package com.unity.shooter.piupiu_server.model.dto;

import com.unity.shooter.piupiu_server.constants.ClientStatus;
import com.unity.shooter.piupiu_server.model.Position;
import com.unity.shooter.piupiu_server.model.Rotation;

public class ClientDataDto {
    private String id;
    private Position position;
    private Rotation rotation;
    private ClientStatus action;

    public ClientDataDto() {
    }

    public ClientDataDto(String id, Position position, Rotation rotation, ClientStatus action) {
        this.id = id;
        this.position = position;
        this.rotation = rotation;
        this.action = action;
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

    public ClientStatus getAction() {
        return action;
    }

    public void setAction(ClientStatus action) {
        this.action = action;
    }
}
