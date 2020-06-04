package ru.magzyumov.weatherapp.Forecast.Polling;

import ru.magzyumov.weatherapp.Forecast.Display.CurrentForecast;
import ru.magzyumov.weatherapp.Forecast.Display.ForecastSource;

public interface ForecastListener {
    void setCurrentForecast(CurrentForecast currentForecast);
    void setForecast (ForecastSource hourlyForecast, ForecastSource dailyForecast);
    void showMessage(String message);
    void initListener();
}
