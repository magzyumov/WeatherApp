package ru.magzyumov.weatherapp.CloudMessaging;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import ru.magzyumov.weatherapp.CloudMessaging.Model.RootModel;

import static ru.magzyumov.weatherapp.BuildConfig.FCM_SERVER_KEY;

public interface ApiInterface {
    @Headers({"Authorization: key=" + FCM_SERVER_KEY, "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body RootModel root);
}
