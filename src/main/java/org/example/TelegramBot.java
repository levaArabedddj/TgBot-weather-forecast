package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramBotConfig botConfig;

    public TelegramBot()throws IOException{
        botConfig = new TelegramBotConfig();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            String command = message.getText();
            if (command.equals("/weather")) {
                try {
                    String weatherInfo = getFormattedWeatherInfo("Reni");
                    sendResponse(message.getChatId(), weatherInfo);
                } catch (IOException e) {
                    e.printStackTrace();
                    sendResponse(message.getChatId(), "Failed to fetch weather information.");
                }
            }
        }
    }

    private String getFormattedWeatherInfo(String city) throws IOException {
        String jsonWeather = getWeatherInfo(city);
        JSONObject jsonObject = new JSONObject(jsonWeather);

        StringBuilder formattedWeather = new StringBuilder();

        // Координаты
        JSONObject coord = jsonObject.getJSONObject("coord");
        double lon = coord.getDouble("lon");
        double lat = coord.getDouble("lat");
        formattedWeather.append("1. Координаты: ").append(lon).append(", ").append(lat).append("\n");

        // Погода
        JSONArray weatherArray = jsonObject.getJSONArray("weather");
        JSONObject weather = weatherArray.getJSONObject(0);
        String description = weather.getString("description");
        formattedWeather.append("2. Погода: ").append(description).append(".\n");

        // Основная информация о погоде
        JSONObject main = jsonObject.getJSONObject("main");
        double temp = main.getDouble("temp");
        double feelsLike = main.getDouble("feels_like");
        double tempMin = main.getDouble("temp_min");
        double tempMax = main.getDouble("temp_max");
        double pressure = main.getDouble("pressure");
        int humidity = main.getInt("humidity");
        formattedWeather.append("3. Основная информация о погоде:\n");
        formattedWeather.append("   Температура: ").append(temp).append("°C\n");
        formattedWeather.append("   Температура по ощущениям: ").append(feelsLike).append("°C\n");
        formattedWeather.append("   Минимальная температура: ").append(tempMin).append("°C\n");
        formattedWeather.append("   Максимальная температура: ").append(tempMax).append("°C\n");
        formattedWeather.append("   Атмосферное давление: ").append(pressure).append(" гПа\n");
        formattedWeather.append("   Влажность: ").append(humidity).append("%\n");

        // Видимость
        int visibility = jsonObject.getInt("visibility");
        formattedWeather.append("4. Видимость: ").append(visibility).append(" м\n");

        // Информация о ветре
        JSONObject wind = jsonObject.getJSONObject("wind");
        double windSpeed = wind.getDouble("speed");
        double windDeg = wind.getDouble("deg");
        formattedWeather.append("5. Информация о ветре:\n");
        formattedWeather.append("   Скорость ветра: ").append(windSpeed).append(" м/с\n");
        formattedWeather.append("   Направление ветра: ").append(windDeg).append("°\n");

        return formattedWeather.toString();
    }

    private String getWeatherInfo(String city) throws IOException {
        String apiKey = botConfig.getApiKey();
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric&lang=ru";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream inputStream = connection.getInputStream();
        Scanner scanner = new Scanner(inputStream);
        StringBuilder response = new StringBuilder();
        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }
        scanner.close();
        return response.toString();
    }

    private void sendResponse(Long chatId, String message) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());
        response.setText(message);
        try {
            execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getNameBot();
    }

    @Override
    public String getBotToken(){
        return botConfig.getTokenBot();
    }




}
