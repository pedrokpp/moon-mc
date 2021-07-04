package me.kp.moon.moondiscord;

import me.kp.moon.moondiscord.mysql.MySQL;

import javax.security.auth.login.LoginException;

public class Run {

    public static void main(String[] args) throws LoginException, InterruptedException {
        MySQL mySQL = new MySQL();
        mySQL.connectToDBS();
        mySQL.updateDisplayTAG("a5141a58-b35f-3835-9798-22888ab8fb78", "teste");
    }
}
