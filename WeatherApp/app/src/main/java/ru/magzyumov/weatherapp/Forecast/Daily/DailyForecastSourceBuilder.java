package ru.magzyumov.weatherapp.Forecast.Daily;

import android.content.res.Resources;

public class DailyForecastSourceBuilder {
    private Resources resources;

    public DailyForecastSourceBuilder setResources(Resources resources){
        this.resources = resources;
        return this;
    }

    public DailyForecastDataSource build(){
        DailyForecastSource dailyForecastSource = new DailyForecastSource(resources, 7);
        dailyForecastSource.init();
        return dailyForecastSource;
    }
}