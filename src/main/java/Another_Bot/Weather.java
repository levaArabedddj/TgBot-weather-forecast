package Another_Bot;


import org.example.TelegramBotConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;


public class Weather {


    // Получаем API-ключ из переменной окружения
    private final String APIkey = System.getenv("APIkey");




    // Метод, который получает текущую погоду в указанном городе.
    public String getFormattedWeatherInfo(String city) throws IOException {
        String jsonWeather = getUrlContent("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + APIkey + "&units=metric&lang=ru");
        JSONObject jsonObject = new JSONObject(jsonWeather);

        StringBuilder formattedWeather = new StringBuilder();

        // Выводим информацию о координатах
        JSONObject coord = jsonObject.getJSONObject("coord");
        formattedWeather.append("1.Координаты: ").append(coord.getDouble("lon")).append(", ").append(coord.getDouble("lat")).append("\n");

        // Выводим информацию о погоде
        JSONArray weatherArray = jsonObject.getJSONArray("weather");
        JSONObject weather = weatherArray.getJSONObject(0);
        formattedWeather.append("2.Погода: ").append(weather.getString("description")).append("\n");
        // Добавляем условие для вывода фотографии в зависимости от погоды


        // Выводим основную информацию о погоде
        JSONObject main = jsonObject.getJSONObject("main");
        formattedWeather.append("\n3.Температура: ").append(main.getDouble("temp")).append("°C\n");
        formattedWeather.append("Ощущается как: ").append(main.getDouble("feels_like")).append("°C\n");
        formattedWeather.append("Минимальная температура: ").append(main.getDouble("temp_min")).append("°C\n");
        formattedWeather.append("Максимальная температура: ").append(main.getDouble("temp_max")).append("°C\n");
        formattedWeather.append("Давление: ").append(main.getDouble("pressure")).append("hPa\n");
        formattedWeather.append("Влажность: ").append(main.getInt("humidity")).append("%\n");

        // Выводим информацию о ветре
        JSONObject wind = jsonObject.getJSONObject("wind");
        formattedWeather.append("\n3.Скорость ветра: ").append(wind.getDouble("speed")).append(" м/с\n");
        formattedWeather.append("Направление ветра: ").append(wind.getDouble("deg")).append("°\n");

        // Выводим информацию о видимости
        formattedWeather.append("\n4.Видимость: ").append(jsonObject.getInt("visibility")).append(" метров\n");

        // Выводим информацию о облачности
        JSONObject clouds = jsonObject.getJSONObject("clouds");
        formattedWeather.append("5.Облачность: ").append(clouds.getInt("all")).append("%\n");

        // Выводим информацию о системе
        JSONObject sys = jsonObject.getJSONObject("sys");
        formattedWeather.append("\n6.Страна: ").append(sys.getString("country")).append("\n");
        formattedWeather.append("7.Восход солнца: ").append(sys.getLong("sunrise")).append("\n");
        formattedWeather.append("8.Закат солнца: ").append(sys.getLong("sunset")).append("\n");

        // Выводим остальные данные
        formattedWeather.append("9.Часовой пояс: ").append(jsonObject.getInt("timezone")).append("\n");
        formattedWeather.append("\n10. ID города: ").append(jsonObject.getInt("id")).append("\n");
        formattedWeather.append("11.Название города: ").append(jsonObject.getString("name")).append("\n");
        formattedWeather.append("12.Код ответа: ").append(jsonObject.getInt("cod")).append("\n");

        return formattedWeather.toString();
    }

    private String getUrlContent(String urlAddress) throws IOException {
        StringBuilder content = new StringBuilder();
        URL url = new URL(urlAddress);
        URLConnection urlConnection = url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            content.append(line).append("\n");
        }
        bufferedReader.close();
        return content.toString();
    }


}