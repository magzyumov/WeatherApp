package ru.magzyumov.weatherapp;

public interface Constants {
    String TAG = "WEATHER";
    String CURR_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=moscow&units=metric&lang=ru&appid=";
    String DAILY_WEATHER_URL = "https://api.openweathermap.org/data/2.5/forecast?q=moscow&units=metric&lang=ru&appid=";
    int REQUEST_TIMEOUT = 5000;
    int CONNECTION_TIMEOUT = 5000;

    String CURRENT_FORECAST = "currentForecast";
    String DAILY_FORECAST = "dailyForecast";

    String PROVIDER_URL = "https://yandex.ru/pogoda/";

    String SETTING = "SETTING";
    String NIGHT_MODE = "NIGHT_MODE";
    String TEMP_EU = "TEMP_EU";
    String WIND_EU = "WIND_EU";
    String PRESS_EU = "PRESS_EU";
    String NOTICE = "NOTICE";

    String LOCATION = "LOCATION";
    String CURRENT_CITY = "CURRENT_CITY";
    String SEARCH_HISTORY = "SEARCH_HISTORY";

    String FORECAST = "FORECAST";
    String LAST_CURRENT = "LAST_CURRENT";
    String LAST_DAILY = "LAST_DAILY";

    float HPA = 0.75006375541921f;

}
