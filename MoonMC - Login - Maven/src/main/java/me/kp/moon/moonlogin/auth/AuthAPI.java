package me.kp.moon.moonlogin.auth;

import java.util.Base64;
import java.util.Random;

public class AuthAPI {

    public static String encodeString(String str) {
        String finalStr = str;
        for (int i = 0; i < 6; i++) {
            finalStr = Base64.getEncoder().encodeToString(finalStr.getBytes());
        }
        return finalStr;
    }

}
