package ru.magzyumov.weatherapp.Forecast.Polling;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Forecast.Model.CurrentForecastModel;
import ru.magzyumov.weatherapp.Forecast.Model.OneCallModel;
import ru.magzyumov.weatherapp.R;

import static java.util.Locale.getDefault;
import static ru.magzyumov.weatherapp.BuildConfig.WEATHER_API_KEY;

public class RetrofitClass implements Constants {
    private ServerPolling serverPolling;
    private String keyApi;
    private OpenWeather openWeather;
    private Retrofit retrofit;
    private Gson gson;

    public RetrofitClass(ServerPolling serverPolling){
        this.gson = new Gson();
        this.keyApi = WEATHER_API_KEY;
        this.serverPolling = serverPolling;
        this.retrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.openWeather = retrofit.create(OpenWeather.class);
    }

    public void getCurrentRequest(String city, String units, Handler handler){

        String lang = getDefault().getLanguage();

        openWeather.loadCurrentWeather(city, units, lang, keyApi)
                .enqueue(new Callback<CurrentForecastModel>() {
                    @Override
                    public void onResponse(Call<CurrentForecastModel> call, Response<CurrentForecastModel> response) {
                        if(response.isSuccessful()){
                            if(response.body() != null){
                                handler.post(() -> serverPolling.responsePars(response.body()));
                            }
                        } else {
                            if (response.code() == HttpURLConnection.HTTP_NOT_FOUND){
                                handler.post(() -> serverPolling.showMsgToListeners(serverPolling.getResources().getString(R.string.cityNotFound)));
                                handler.post(() -> serverPolling.badResponseHandler());
                            } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED){
                                handler.post(() -> serverPolling.showMsgToListeners(serverPolling.getResources().getString(R.string.invalidKey)));
                                handler.post(() -> serverPolling.badResponseHandler());
                            } else {
                                handler.post(() -> serverPolling.showMsgToListeners(response.message()));
                                handler.post(() -> serverPolling.badResponseHandler());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentForecastModel> call, Throwable t) {
                        handler.post(() -> serverPolling.showMsgToListeners(serverPolling.getResources().getString(R.string.badResponse)));
                        handler.post(() -> serverPolling.badResponseHandler());
                    }
                });
    }

    public void getCurrentRequest(double latitude, double longitude, String units, Handler handler){

        String lang = getDefault().getLanguage();

        openWeather.loadCurrentWeather(latitude, longitude, units, lang, keyApi)
                .enqueue(new Callback<CurrentForecastModel>() {
                    @Override
                    public void onResponse(Call<CurrentForecastModel> call, Response<CurrentForecastModel> response) {
                        if(response.isSuccessful()){
                            if(response.body() != null){
                                handler.post(() -> serverPolling.responsePars(response.body()));
                            }
                        } else {
                            if (response.code() == HttpURLConnection.HTTP_NOT_FOUND){
                                handler.post(() -> serverPolling.showMsgToListeners(serverPolling.getResources().getString(R.string.cityNotFound)));
                                handler.post(() -> serverPolling.badResponseHandler());
                            } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED){
                                handler.post(() -> serverPolling.showMsgToListeners(serverPolling.getResources().getString(R.string.invalidKey)));
                                handler.post(() -> serverPolling.badResponseHandler());
                            } else {
                                handler.post(() -> serverPolling.showMsgToListeners(response.message()));
                                handler.post(() -> serverPolling.badResponseHandler());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentForecastModel> call, Throwable t) {
                        handler.post(() -> serverPolling.showMsgToListeners(serverPolling.getResources().getString(R.string.badResponse)));
                        handler.post(() -> serverPolling.badResponseHandler());
                    }
                });
    }

    public void getOneCallRequest(double latitude, double longitude, String units, Handler handler){

        String lang = getDefault().getLanguage();

        openWeather.loadOneCallWeather(latitude, longitude, units, lang, keyApi)
                .enqueue(new Callback<OneCallModel>() {
                    @Override
                    public void onResponse(Call<OneCallModel> call, Response<OneCallModel> response) {
                        if(response.isSuccessful()){
                            if(response.body() != null){
                                handler.post(() -> serverPolling.responsePars(response.body()));
                            }
                        } else {
                            if (response.code() == HttpURLConnection.HTTP_NOT_FOUND){
                                handler.post(() -> serverPolling.showMsgToListeners(serverPolling.getResources().getString(R.string.cityNotFound)));
                                handler.post(() -> serverPolling.badResponseHandler());
                            } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED){
                                handler.post(() -> serverPolling.showMsgToListeners(serverPolling.getResources().getString(R.string.invalidKey)));
                                handler.post(() -> serverPolling.badResponseHandler());
                            } else {
                                handler.post(() -> serverPolling.showMsgToListeners(response.message()));
                                handler.post(() -> serverPolling.badResponseHandler());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OneCallModel> call, Throwable t) {
                        handler.post(() -> serverPolling.showMsgToListeners(serverPolling.getResources().getString(R.string.badResponse)));
                        handler.post(() -> serverPolling.badResponseHandler());
                    }
                });
    }
}
