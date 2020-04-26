package ru.magzyumov.weatherapp.Forecast.Hourly;

import android.content.res.Resources;

public class HourlyForecastSourceBuilder {
    private Resources resources;

    public HourlyForecastSourceBuilder setResources(Resources resources){
        this.resources = resources;
        return this;
    }

    public HourlyForecastDataSource build(){
        HourlyForecastSource hourlyForecastSource = new HourlyForecastSource(resources, 24);
        hourlyForecastSource.init();
        return hourlyForecastSource;
    }
}