package ru.magzyumov.weatherapp.room.database.Location;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import ru.magzyumov.weatherapp.Forecast.Model.CurrentForecastModel;
import ru.magzyumov.weatherapp.Forecast.Model.DailyForecastModel;

// @Entity - это признак табличного объекта, то есть объект будет сохраняться
// в базе данных в виде строки
// indices указывает на индексы в таблице
@Entity(indices = {@Index(value = {"region", "city","isCurrent", "isSearched"})})
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

    // Последний текущий прогноз для этого города
    @ColumnInfo(name = "currentForecast")
    public String currentForecast;

    // Последний текущий прогноз для этого города
    @ColumnInfo(name = "dailyForecast")
    public String dailyForecast;

    // Дата последнего прогноза для этого города
    @ColumnInfo(name = "date")
    public long date;

    // Температура по данному городу из
    // последнего прогноза
    @ColumnInfo(name = "temperature")
    public float temperature;

    // Флаг о том, что данные нужно обновить
    @ColumnInfo(name = "needUpdate")
    public boolean needUpdate;
}
