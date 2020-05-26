package ru.magzyumov.weatherapp.Forecast.Polling;

import ru.magzyumov.weatherapp.Forecast.Display.CurrentForecast;
import ru.magzyumov.weatherapp.Forecast.Display.DailyForecastSource;

public interface ForecastListener {
    void setCurrentForecast(CurrentForecast currentForecast);
    void setDailyForecast(DailyForecastSource dailyForecast);
    void showMessage(String message);
    void initListener();
}
