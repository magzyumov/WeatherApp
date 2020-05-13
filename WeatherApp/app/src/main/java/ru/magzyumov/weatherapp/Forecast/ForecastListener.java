package ru.magzyumov.weatherapp.Forecast;

import ru.magzyumov.weatherapp.Forecast.Model.CurrentForecastModel;
import ru.magzyumov.weatherapp.Forecast.Model.DailyForecastModel;

public interface ForecastListener {
    void setCurrentForecastModel(CurrentForecastModel currentForecastModel);
    void setDailyForecastModel(DailyForecastModel dailyForecastModel);
    void showMessage(String message);
    void initActivity();
}
