package ru.magzyumov.weatherapp.Forecast.Polling;

import android.os.Handler;

import com.google.gson.Gson;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Forecast.Model.CurrentForecastModel;
import ru.magzyumov.weatherapp.Forecast.Model.DailyForecastModel;
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
                .baseUrl(BASE_URL)
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
                            } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED){
                                handler.post(() -> serverPolling.showMsgToListeners(serverPolling.getResources().getString(R.string.invalidKey)));
                            } else {
                                handler.post(() -> serverPolling.showMsgToListeners(response.message()));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentForecastModel> call, Throwable t) {
                        handler.post(() -> serverPolling.showMsgToListeners(t.getLocalizedMessage()));
                    }
                });
    }

    public void getDailyRequest(String city, String units, Handler handler){

        String lang = getDefault().getLanguage();

        openWeather.loadDailyWeather(city, units, lang, keyApi)
                .enqueue(new Callback<DailyForecastModel>() {
                    @Override
                    public void onResponse(Call<DailyForecastModel> call, Response<DailyForecastModel> response) {
                        if(response.isSuccessful()){
                            if(response.body() != null){
                                handler.post(() -> serverPolling.responsePars(response.body()));
                            }
                        } else {
                            if (response.code() == HttpURLConnection.HTTP_NOT_FOUND){
                                handler.post(() -> serverPolling.showMsgToListeners(serverPolling.getResources().getString(R.string.cityNotFound)));
                            } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED){
                                handler.post(() -> serverPolling.showMsgToListeners(serverPolling.getResources().getString(R.string.invalidKey)));
                            } else {
                                handler.post(() -> serverPolling.showMsgToListeners(response.message()));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DailyForecastModel> call, Throwable t) {
                        handler.post(() -> serverPolling.showMsgToListeners(t.getLocalizedMessage()));
                    }
                });
    }

    public void getOneCallRequest(String latitude, String longitude, String units, Handler handler){

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
                            } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED){
                                handler.post(() -> serverPolling.showMsgToListeners(serverPolling.getResources().getString(R.string.invalidKey)));
                            } else {
                                handler.post(() -> serverPolling.showMsgToListeners(response.message()));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OneCallModel> call, Throwable t) {
                        handler.post(() -> serverPolling.showMsgToListeners(t.getLocalizedMessage()));
                    }
                });
    }
}
