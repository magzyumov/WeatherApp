package ru.magzyumov.weatherapp.Forecast.Hourly;

public interface HourlyForecastDataSource {
    HourlyForecast getHourlyForecast(int position);
    int size();
}