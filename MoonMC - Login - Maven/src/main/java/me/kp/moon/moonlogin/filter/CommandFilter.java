package me.kp.moon.moonlogin.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

import java.util.Arrays;
import java.util.List;

public class CommandFilter {

    private final List<String> blockedCommands = Arrays.asList("/login", "/logar", "/register", "/registrar", "/mudarsenha", "/changepassword");

//    @Override
//    public boolean isLoggable(LogRecord record) {
//        return false;
//    }

    public void hideConsoleMessages() {
        ((Logger) LogManager.getRootLogger()).addFilter(new Filter() {

            @Override
            public Result getOnMismatch() {
                return null;
            }

            @Override
            public Result getOnMatch() {
                return null;
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String s, Object... objects) {
                return null;
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, Object o, Throwable throwable) {
                return null;
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, Message message, Throwable throwable) {
                return null;
            }

            @Override
            public Result filter(LogEvent event) {
                if (!blockedCommands.isEmpty()) {
                    for (final String s : blockedCommands) {
                        if (event.getMessage().toString().contains(s)) {
                            return Filter.Result.DENY;
                        }
                    }
                }
                return null;
            }
        });
    }

}
