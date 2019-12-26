package com.unity.shooter.piupiu_server.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unity.shooter.piupiu_server.constants.Action;
import com.unity.shooter.piupiu_server.constants.ClientActionType;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ClientData {
    private String id;
    private Position position;
    private Rotation rotation;
    private ClientActionType actionType;
    private Action action;

    @JsonCreator
    public ClientData(@JsonProperty("id") String id,
                      @JsonProperty("position") Position position,
                      @JsonProperty("rotation") Rotation rotation,
                      @JsonProperty("actionType") ClientActionType actionType,
                      @JsonProperty("action") Action action) {
        this.id = id;
        this.position = position;
        this.rotation = rotation;
        this.actionType = actionType;
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

    public ClientActionType getActionType() {
        return actionType;
    }

    public Action getAction() {
        return action;
    }
}
