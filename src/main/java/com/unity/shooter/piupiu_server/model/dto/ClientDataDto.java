package com.unity.shooter.piupiu_server.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.unity.shooter.piupiu_server.constants.ClientStatus;
import com.unity.shooter.piupiu_server.model.Position;
import com.unity.shooter.piupiu_server.model.Rotation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ClientDataDto {
    private String id;
    private Position position;
    private Rotation rotation;
    private Position target;
    private String targetId;
    private ClientStatus action;

    public ClientDataDto(String id, Position position, Rotation rotation, ClientStatus action) {
        this.id = id;
        this.position = position;
        this.rotation = rotation;
        this.action = action;
    }

    public ClientDataDto(String id, Position position, Rotation rotation, Position target, ClientStatus action) {
        this.id = id;
        this.position = position;
        this.rotation = rotation;
        this.target = target;
        this.action = action;
    }
}
