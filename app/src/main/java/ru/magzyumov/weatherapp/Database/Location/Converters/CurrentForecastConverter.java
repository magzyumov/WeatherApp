package ru.magzyumov.weatherapp.Database.Location.Converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import ru.magzyumov.weatherapp.Forecast.Display.CurrentForecast;

public class CurrentForecastConverter {
    private Gson gson;

    public CurrentForecastConverter(){
        this.gson = new Gson();
    }

    @TypeConverter
    public String fromForecast(CurrentForecast currentForecast){
        return gson.toJson(currentForecast);
    }

    @TypeConverter
    public CurrentForecast toForecast(String data){
        return gson.fromJson(data, CurrentForecast.class);
    }
}
