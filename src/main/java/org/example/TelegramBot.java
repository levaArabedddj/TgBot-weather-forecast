package org.example;
import Another_Bot.Weather;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramBotConfig botConfig;
    private final Weather weather = new Weather();

    public TelegramBot() throws IOException {
        botConfig = new TelegramBotConfig();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            String messageText = update.getMessage().getText(); // Получаем текст сообщения пользователя
            Long chatId = update.getMessage().getChatId(); // Получаем chatId пользователя
            switch (messageText) {
                case "/start" ->
                        sendMessage(
                                chatId,
                                "Привет! Я помогу узнать погоду, просто напиши название города."
                        );
                case "/help" ->
                        sendMessage(
                                chatId,
                                "Чтобы узнать погоду, отправь название города. Если такого города нет, я промолчу!"
                        );
                default -> {
                    try {
                        sendMessage(
                                chatId,
                                weather.getFormattedWeatherInfo(messageText));

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }

    // Метод для отправки сообщения пользователю
    public void sendMessage(Long chatId, String messageText) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(messageText);
        sendMessage.setChatId(chatId);
        try {
            executeAsync(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getNameBot();
    }

    @Override
    public String getBotToken() {
        return botConfig.getTokenBot();
    }
}