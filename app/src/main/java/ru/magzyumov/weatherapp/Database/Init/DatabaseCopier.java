package ru.magzyumov.weatherapp.Database.Init;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// Класс для копрования заранее заготовленной базы данных
public class DatabaseCopier {
    private static final String TAG = DatabaseCopier.class.getSimpleName();
    private static final String DATABASE_NAME = "storage";
    private static Context appContext;

    private static class Holder {
        private static final DatabaseCopier INSTANCE = new DatabaseCopier();
    }

    public static DatabaseCopier getInstance(Context context) {
        appContext = context;
        return Holder.INSTANCE;
    }

    // Метод проверяет сущестует ли база данных.
    private DatabaseCopier() {
        copyAttachedDatabase(appContext, DATABASE_NAME);
    }

    private void copyAttachedDatabase(Context context, String databaseName) {
        final File dbPath = context.getDatabasePath(databaseName);

        // Если база существует, выходим
        if (dbPath.exists()) {
            return;
        }

        // Создаем необходимые директории
        dbPath.getParentFile().mkdirs();

        // Копируем базу
        try {
            final InputStream inputStream = context.getAssets().open("databases/" + databaseName);
            final OutputStream output = new FileOutputStream(dbPath);

            byte[] buffer = new byte[8192];
            int length;

            while ((length = inputStream.read(buffer, 0, 8192)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            inputStream.close();
        }
        catch (IOException e) {
            Log.d(TAG, "Failed to open file", e);
            e.printStackTrace();
        }
    }
}
