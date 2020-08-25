package ru.magzyumov.weatherapp.Forecast.Display;

public interface ForecastDataSource {
    Forecast getForecast(int position);
    int size();
}
