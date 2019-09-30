package com.unity.shooter.piupiu_server.model.dto;

import com.unity.shooter.piupiu_server.model.Position;
import com.unity.shooter.piupiu_server.model.Rotation;

import java.util.Objects;

public class ClientDataDto {
    private String id;
    private Position position;
    private Rotation rotation;

    public ClientDataDto() {
    }

    public ClientDataDto(String id, Position position, Rotation rotation) {
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

    @Override
    public String toString() {
        return "ClientDataDto{" +
                "id='" + id + '\'' +
                ", position=" + position +
                ", rotation=" + rotation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientDataDto that = (ClientDataDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(position, that.position) &&
                Objects.equals(rotation, that.rotation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, position, rotation);
    }
}
