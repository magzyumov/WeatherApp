package ru.magzyumov.weatherapp.CloudMessaging;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.magzyumov.weatherapp.Constants;

public class ApiClient implements Constants {
    private String BASE_URL = FCM_BASE_URL;
    private Retrofit retrofit = null;

    public Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
