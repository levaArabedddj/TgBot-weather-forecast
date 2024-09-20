package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TelegramBotConfig {
    private final Properties properties;


    TelegramBotConfig()throws IOException{
        properties = new Properties();
        String path = "src\\main\\resources\\application.properties";
        properties.load(new FileInputStream(path));
    }

    public String getNameBot(){
        return properties.getProperty("bot.name");
    }
    public String getTokenBot(){
        return properties.getProperty("bot.token");
    }

    public String getApiKey(){
        return properties.getProperty("api.key");
    }
}
