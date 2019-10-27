package com.unity.shooter.piupiu_server.util;

public class NumberUtil {
    public static boolean isNumeric(String strNum) {
        try {
            Double.parseDouble(strNum);
            return true;
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
    }
}
