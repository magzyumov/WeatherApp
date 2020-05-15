package ru.magzyumov.weatherapp.Forecast.Display;

public interface DailyForecastDataSource {
    DailyForecast getDailyForecast(int position);
    int size();
}
