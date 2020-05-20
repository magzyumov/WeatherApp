package ru.magzyumov.weatherapp;

public interface Constants {
    String TAG = "WEATHER";
    String CURR_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&lang=%s&appid=";
    String DAILY_WEATHER_URL = "https://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&lang=%s&appid=";
    int REQUEST_TIMEOUT = 5000;
    int CONNECTION_TIMEOUT = 5000;

    String CURRENT_FORECAST = "currentForecast";
    String DAILY_FORECAST = "dailyForecast";

    String PROVIDER_URL = "https://openweathermap.org/";

    String SETTING = "SETTING";
    String NIGHT_MODE = "NIGHT_MODE";
    String TEMP_EU = "TEMP_EU";
    String WIND_EU = "WIND_EU";
    String PRESS_EU = "PRESS_EU";
    String NOTICE = "NOTICE";

    String LOCATION = "location";
    String CURRENT_CITY = "current_city";
    String SEARCH_HISTORY = "search_history";

    String FORECAST = "forecast";
    String CURRENT = "current";
    String DAILY = "daily";

    float HPA = 0.75006375541921f;

}
