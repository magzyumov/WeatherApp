package ru.magzyumov.weatherapp.Forecast;

import ru.magzyumov.weatherapp.Forecast.DailyForecast;

public interface DailyForecastDataSource {
    DailyForecast getDailyForecast(int position);
    int size();
}
