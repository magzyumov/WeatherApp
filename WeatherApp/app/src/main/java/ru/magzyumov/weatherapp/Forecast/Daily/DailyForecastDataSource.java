package ru.magzyumov.weatherapp.Forecast.Daily;

public interface DailyForecastDataSource {
    DailyForecast getDailyForecast(int position);
    int size();
}
