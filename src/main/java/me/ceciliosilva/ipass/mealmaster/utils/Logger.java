package me.ceciliosilva.ipass.mealmaster.utils;

import org.apache.tomcat.util.buf.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Logger {
    // The levels of logging
    // ERROR - 40
    // WARNING - 30
    // INFO - 20
    // DEBUG - 10

    // The minimum level to log
    private static final int logLevel = 10;

    private static void log(String level, String location, String... msg){
        String message = StringUtils.join(Arrays.asList(msg), ' ');
        System.out.printf("[%s] - %s: (%s) %s%n", LocalDateTime.now(), level, location, message);
    }

    public static void debug(String location, String... msg){
        if(logLevel <= 10){
            log("DEBUG", location, msg);
        }
    }

    public static void info(String location, String... msg){
        if(logLevel <= 20){
            log("INFO", location, msg);
        }
    }

    public static void warning(String location, String... msg){
        if(logLevel <= 30){
            log("WARNING", location, msg);
        }
    }

    public static void error(String location, String... msg){
        if(logLevel <= 40){
            log("ERROR", location, msg);
        }
    }
}
