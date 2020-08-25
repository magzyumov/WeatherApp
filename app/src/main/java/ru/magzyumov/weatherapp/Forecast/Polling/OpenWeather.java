package ru.magzyumov.weatherapp.Forecast.Polling;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.magzyumov.weatherapp.Forecast.Model.CurrentForecastModel;
import ru.magzyumov.weatherapp.Forecast.Model.OneCallModel;

public interface OpenWeather {
    @GET("data/2.5/weather")
    Call<CurrentForecastModel> loadCurrentWeather(@Query("q") String cityCountry,
                                           @Query("units") String units,
                                           @Query("lang") String lang,
                                           @Query("appId") String keyApi);

    @GET("data/2.5/weather")
    Call<CurrentForecastModel> loadCurrentWeather(@Query("lat") double latitude,
                                                  @Query("lon") double longitude,
                                                  @Query("units") String units,
                                                  @Query("lang") String lang,
                                                  @Query("appId") String keyApi);

    @GET("data/2.5/onecall")
    Call<OneCallModel> loadOneCallWeather(@Query("lat") double latitude,
                                          @Query("lon") double longitude,
                                          @Query("units") String units,
                                          @Query("lang") String lang,
                                          @Query("appId") String keyApi);
}