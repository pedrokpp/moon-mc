package me.kp.moon.moonlogin.auth;

import java.util.Base64;
import java.util.Random;

public class AuthAPI {

    public static String encodeString(String str, int discriminator) {
        String finalStr = str;
        for (int i = 0; i < discriminator; i++) {
            finalStr = Base64.getEncoder().encodeToString(finalStr.getBytes());
        }
        return finalStr;
    }

    public static int genDiscriminator() {
        return new Random().nextInt(20 - 6) + 6;
    }

}
