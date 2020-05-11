package ru.magzyumov.weatherapp.room.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.magzyumov.weatherapp.room.database.Location.Location;
import ru.magzyumov.weatherapp.room.database.Location.LocationDao;

@Database(entities = {Location.class}, version = 1)
public abstract class ForecastDatabase extends RoomDatabase {
    public abstract LocationDao getLocationDao();
}