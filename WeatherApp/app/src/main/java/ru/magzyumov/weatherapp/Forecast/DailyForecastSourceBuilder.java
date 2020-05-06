package ru.magzyumov.weatherapp.Forecast;

import android.content.Context;
import android.content.res.Resources;

public class DailyForecastSourceBuilder {
    private Resources resources;
    private Context context;

    public DailyForecastSourceBuilder setResources(Resources resources){
        this.resources = resources;
        return this;
    }

    public DailyForecastSourceBuilder setContext(Context context){
        this.context = context;
        return this;
    }

    public DailyForecastDataSource build(){
        DailyForecastSource dailyForecastSource = new DailyForecastSource(resources, context, 7);
        dailyForecastSource.init();
        return dailyForecastSource;
    }
}