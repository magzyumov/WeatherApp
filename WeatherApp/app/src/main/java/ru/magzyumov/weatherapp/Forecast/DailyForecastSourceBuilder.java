package ru.magzyumov.weatherapp.Forecast;

import android.content.Context;
import android.content.res.Resources;

import ru.magzyumov.weatherapp.Forecast.Model.DailyForecastModel;

public class DailyForecastSourceBuilder {
    private DailyForecastModel dailyForecastModel;
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

    public DailyForecastSourceBuilder setDataFromServer(DailyForecastModel dailyForecastModel){
        this.dailyForecastModel = dailyForecastModel;
        return this;
    }

    public DailyForecastDataSource build(){
        DailyForecastSource dailyForecastSource = new DailyForecastSource(resources, context, dailyForecastModel);
        dailyForecastSource.init();
        return dailyForecastSource;
    }

    public DailyForecastDataSource build2(){
        DailyForecastSource dailyForecastSource = new DailyForecastSource(resources, context, 7);
        dailyForecastSource.init();
        return dailyForecastSource;
    }
}