package ru.magzyumov.weatherapp.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.magzyumov.weatherapp.Database.Location.Location;
import ru.magzyumov.weatherapp.Database.Location.LocationDao;

@Database(entities = {Location.class}, version = 1)
public abstract class ForecastDatabase extends RoomDatabase {
    public abstract LocationDao getLocationDao();
}