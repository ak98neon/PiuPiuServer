package com.unity.shooter.piupiu_server.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unity.shooter.piupiu_server.constants.ClientStatus;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ClientData {
    private String id;
    private Position position;
    private Rotation rotation;
    private ClientStatus action;

    @JsonCreator
    public ClientData(@JsonProperty("id") String id, @JsonProperty("position") Position position,
                      @JsonProperty("rotation") Rotation rotation, @JsonProperty("action") ClientStatus action) {
        this.id = id;
        this.position = position;
        this.rotation = rotation;
        this.action = action;
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

    public ClientStatus getAction() {
        return action;
    }
}
