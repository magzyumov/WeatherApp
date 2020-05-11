package ru.magzyumov.weatherapp.room.database.Location;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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

    // Последний прогноз для этого города
    // Флаг о том, что данную локацию искали ранее
    @ColumnInfo(name = "forecast")
    public String forecast;
}
