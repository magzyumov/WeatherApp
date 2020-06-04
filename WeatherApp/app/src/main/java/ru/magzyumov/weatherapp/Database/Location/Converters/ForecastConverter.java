package ru.magzyumov.weatherapp.Database.Location.Converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import ru.magzyumov.weatherapp.Forecast.Display.ForecastSource;

public class ForecastConverter {
    private Gson gson;

    public ForecastConverter(){
        this.gson = new Gson();
    }

    @TypeConverter
    public String fromForecast(ForecastSource hourlyForecast){
        return gson.toJson(hourlyForecast);
    }

    @TypeConverter
    public ForecastSource toForecast(String data){
        return gson.fromJson(data, ForecastSource.class);
    }
}
