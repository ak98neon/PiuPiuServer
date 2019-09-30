package com.unity.shooter.piupiu_server.model.dto;

import com.unity.shooter.piupiu_server.constants.ClientStatus;
import com.unity.shooter.piupiu_server.model.Position;
import com.unity.shooter.piupiu_server.model.Rotation;

public class ClientDataResponseDto {
    private String id;
    private Position position;
    private Rotation rotation;
    private ClientStatus status;

    public ClientDataResponseDto() {
    }

    public ClientDataResponseDto(String id, Position position, Rotation rotation, ClientStatus status) {
        this.id = id;
        this.position = position;
        this.rotation = rotation;
        this.status = status;
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

    public ClientStatus getStatus() {
        return status;
    }

    public void setStatus(ClientStatus status) {
        this.status = status;
    }
}
