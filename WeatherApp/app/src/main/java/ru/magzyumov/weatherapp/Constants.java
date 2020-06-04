package ru.magzyumov.weatherapp;

import static ru.magzyumov.weatherapp.BuildConfig.FCM_SERVER_KEY;

public interface Constants {
    String TAG = "WEATHER";
    String WEATHER_BASE_URL = "https://api.openweathermap.org/";
    String FCM_BASE_URL = "https://fcm.googleapis.com/";
    String IMAGE_URL = "https://openweathermap.org/img/wn/%s@4x.png";

    String PROVIDER_URL = "https://openweathermap.org/";

    String SETTING = "settings";
    String NIGHT_MODE = "night_mode";
    String EU = "eu";
    String NOTICE = "notice";

    String LOCATION = "location";
    String CURRENT_CITY = "current_city";
    String SEARCH_HISTORY = "search_history";

    String FORECAST = "forecast";
    String CURRENT = "current";
    String HOURLY = "hourly";
    String DAILY = "daily";

    String TIMESTAMP_PATTERN = "dd MMMM HH:mm:ss";

    // Firebase database structure
    String PHONES = "phones";
    String INSTALLATION = "installation";
    String BATTERY = "battery";

    // Firebase cloud messaging
    String FCM_API_URL = "https://fcm.googleapis.com/fcm/send";
    String SUBSCRIBE_TO = "all_users";
    String TOPIC = "/topics/" + SUBSCRIBE_TO;
    String contentType = "application/json";
    String serverKey = "key=" + FCM_SERVER_KEY;

    float HPA = 0.75006375541921f;




}
