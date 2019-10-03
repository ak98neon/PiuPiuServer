package com.unity.shooter.piupiu_server.util;

import java.nio.ByteBuffer;

public class ByteBufferUtil {
    public static byte[] intToByteArray(int a) {
        byte[] array = ByteBuffer.allocate(4).putInt(a).array();
        return reverseArray(array);
    }

    private static byte[] reverseArray(byte[] array) {
        byte[] duplicate = new byte[array.length];

        for (int i = array.length - 1; i >= 0; i--) {
            duplicate[array.length - 1 - i] = array[i];
        }
        return duplicate;
    }
}
