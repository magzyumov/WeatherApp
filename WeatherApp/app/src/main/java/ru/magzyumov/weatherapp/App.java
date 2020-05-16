package ru.magzyumov.weatherapp;

import android.app.Application;

import androidx.room.Room;

import ru.magzyumov.weatherapp.Database.ForecastDatabase;
import ru.magzyumov.weatherapp.Database.Location.LocationDao;

// Паттерн Singleton, наследуем класс Application, создаём базу данных
// в методе onCreate
public class App extends Application {

    private static App instance;

    // База данных
    private ForecastDatabase db;

    // Получаем объект приложения
    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Сохраняем объект приложения (для Singleton’а)
        instance = this;

        // Строим базу
        db = Room.databaseBuilder(
                getApplicationContext(),
                ForecastDatabase.class,
                "storage.db")
                .allowMainThreadQueries() //Только для примеров и тестирования.
                .fallbackToDestructiveMigration()
                .build();
    }

    // Получаем Dao для составления запросов
    public LocationDao getLocationDao() {
        return db.getLocationDao();
    }
}

