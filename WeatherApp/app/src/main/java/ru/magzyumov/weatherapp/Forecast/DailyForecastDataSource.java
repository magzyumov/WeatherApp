package ru.magzyumov.weatherapp.Forecast;

public interface DailyForecastDataSource {
    DailyForecast getDailyForecast(int position);
    int size();
}
