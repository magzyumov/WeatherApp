package ru.magzyumov.weatherapp;

public interface Constants {
    String TAG = "WEATHER";
    String CURR_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?lat=55.75&lon=37.62&appid=";
    String DAILY_WEATHER_URL = "https://api.openweathermap.org/data/2.5/forecast?lat=55.75&lon=37.62&appid=";
    int REQUEST_TIMEOUT = 1000;

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

}
