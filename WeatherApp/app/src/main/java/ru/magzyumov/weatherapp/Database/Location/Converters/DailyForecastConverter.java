package ru.magzyumov.weatherapp.Database.Location.Converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import ru.magzyumov.weatherapp.Forecast.Display.DailyForecastSource;

public class DailyForecastConverter {
    private Gson gson;

    public DailyForecastConverter(){
        this.gson = new Gson();
    }

    @TypeConverter
    public String fromForecast(DailyForecastSource dailyForecast){
        return gson.toJson(dailyForecast);
    }

    @TypeConverter
    public DailyForecastSource toForecast(String data){
        return gson.fromJson(data, DailyForecastSource.class);
    }
}
