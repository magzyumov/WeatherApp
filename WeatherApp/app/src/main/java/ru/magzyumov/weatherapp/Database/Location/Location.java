package ru.magzyumov.weatherapp.Database.Location;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import ru.magzyumov.weatherapp.Database.Location.Converters.CurrentForecastConverter;
import ru.magzyumov.weatherapp.Database.Location.Converters.DailyForecastConverter;
import ru.magzyumov.weatherapp.Forecast.Display.CurrentForecast;
import ru.magzyumov.weatherapp.Forecast.Display.DailyForecastSource;

// @Entity - это признак табличного объекта, то есть объект будет сохраняться
// в базе данных в виде строки
// indices указывает на индексы в таблице
@Entity(indices = {@Index(value = {"region", "city"})})

public class Location {

    // @PrimaryKey - указывает на ключевую запись,
    // autoGenerate = true - автоматическая генерация ключа
    @PrimaryKey(autoGenerate = true)
    public long id;

    // Регион города
    // @ColumnInfo позволяет задавать параметры колонки в БД
    // name = "region" - имя колонки
    @ColumnInfo(name = "region")
    public String region;

    // Название города
    @ColumnInfo(name = "city")
    public String city;

    // Флаг о том, что данная локация текущая
    @ColumnInfo(name = "isCurrent")
    public boolean isCurrent;

    // Флаг о том, что данную локацию искали ранее
    @ColumnInfo(name = "isSearched")
    public boolean isSearched;

    // Флаг о том, что данные нужно обновить
    @ColumnInfo(name = "needUpdate")
    public boolean needUpdate;

    // Последний текущий прогноз для этого города
    @TypeConverters(CurrentForecastConverter.class)
    @ColumnInfo(name = "currentForecast")
    public CurrentForecast currentForecast;

    // Последний текущий прогноз для этого города
    @TypeConverters(DailyForecastConverter.class)
    @ColumnInfo(name = "dailyForecast")
    public DailyForecastSource dailyForecast;
}
