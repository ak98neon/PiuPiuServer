package com.unity.shooter.piupiu_server.environment;

import com.unity.shooter.piupiu_server.client.Position;
import com.unity.shooter.piupiu_server.client.RequestData;
import com.unity.shooter.piupiu_server.client.Rotation;
import com.unity.shooter.piupiu_server.constants.Action;
import com.unity.shooter.piupiu_server.constants.ClientActionType;

import java.util.ArrayList;
import java.util.List;

public class GameEnvironment {

    public static List<RequestData> getEnvironment() {
        List<RequestData> dataList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dataList.add(createNewAnimal());
        }
        return dataList;
    }

    public static RequestData createNewAnimal() {
        return new RequestData("", new Position(), new Rotation(), ClientActionType.ANIMAL,
                Action.ANIMAL_CREATE);
    }
}
