package ru.magzyumov.weatherapp.room.init;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.magzyumov.weatherapp.App;
import ru.magzyumov.weatherapp.room.database.Location.LocationDao;
import ru.magzyumov.weatherapp.room.database.Location.LocationSource;

import static android.content.Context.MODE_PRIVATE;


// Класс для копрования данных из исходной базы данных
// в Room приложения
public class DatabaseCopier {
    private static final String TAG = DatabaseCopier.class.getSimpleName();
    private static final String DATABASE_NAME = "storage";
    private static SharedPreferences sharedPref;
    private static Context appContext;
    private LocationSource locationSource;

    private static class Holder {
        private static final DatabaseCopier INSTANCE = new DatabaseCopier();
    }

    public static DatabaseCopier getInstance(Context context) {
        appContext = context;
        sharedPref =  appContext.getSharedPreferences("SETTINGS", MODE_PRIVATE);
        return Holder.INSTANCE;
    }

    private DatabaseCopier() {
        //call method that check if database not exists and copy prepopulate file from assets
        if(! sharedPref.getBoolean("INIT", false)){
            copyAttachedDatabase(appContext, DATABASE_NAME);

            SQLHandler.connect();

            LocationDao locationDao = App
                    .getInstance()
                    .getLocationDao();

            locationSource = new LocationSource(locationDao);
            locationSource.insertBigData(SQLHandler.getAllData());

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("INIT", true);
            editor.apply();
        }
    }

    private void copyAttachedDatabase(Context context, String databaseName) {
        final File dbPath = context.getDatabasePath(databaseName);

        // If the database already exists, return
        if (dbPath.exists()) {
            return;
        }

        // Make sure we have a path to the file
        dbPath.getParentFile().mkdirs();

        // Try to copy database file
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
