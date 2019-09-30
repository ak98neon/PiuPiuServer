package com.unity.shooter.piupiu_server.container;

import com.unity.shooter.piupiu_server.model.dto.ClientDataDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MapClients {
    private static Map<String, ClientDataDto> clientDataDtoMap = new HashMap<>();

    public static Map<String, ClientDataDto> getClientDataDtoMap() {
        return clientDataDtoMap;
    }

    public static void setClientDataDtoMap(Map<String, ClientDataDto> clientDataDtoMap) {
        MapClients.clientDataDtoMap = clientDataDtoMap;
    }
}
