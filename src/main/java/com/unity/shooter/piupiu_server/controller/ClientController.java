package com.unity.shooter.piupiu_server.controller;

import com.unity.shooter.piupiu_server.constants.ClientStatus;
import com.unity.shooter.piupiu_server.container.MapClients;
import com.unity.shooter.piupiu_server.model.dto.ClientDataDto;
import com.unity.shooter.piupiu_server.model.dto.ClientDataResponseDto;
import com.unity.shooter.piupiu_server.service.ClientService;
import com.unity.shooter.piupiu_server.util.ClientId;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

import static com.unity.shooter.piupiu_server.constants.Api.API_NAME;
import static com.unity.shooter.piupiu_server.constants.Api.API_VERSION;

@RestController
@RequestMapping("/" + API_NAME + "/" + API_VERSION)
public class ClientController {
    private ClientService clientService;
    private ClientId clientId;

    public ClientController(ClientService clientService, ClientId clientId) {
        this.clientService = clientService;
        this.clientId = clientId;
    }

    @PostMapping("/client")
    public ClientDataResponseDto receiveClientData(@RequestBody @NotNull ClientDataDto dataDto) {
        if (!StringUtils.hasText(dataDto.getId())) {
            String id = clientId.generateUniqueId();
            dataDto.setId(id);
            MapClients.getClientDataDtoMap().put(id, dataDto);
            return new ClientDataResponseDto(dataDto.getId(), dataDto.getPosition(), dataDto.getRotation(), ClientStatus.NEW_CLIENT);
        } else {
            return new ClientDataResponseDto(dataDto.getId(), dataDto.getPosition(), dataDto.getRotation(), ClientStatus.MOVE);
        }
    }
}
