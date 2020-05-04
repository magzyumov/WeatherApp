package ru.magzyumov.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity implements Constants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getBooleanPreference(SETTING, NIGHT_MODE)){
            setTheme(R.style.AppDarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
    }

    // Чтение boolean настроек
    public boolean getBooleanPreference(String preference, String parameter) {
        // Работаем через специальный класс сохранения и чтения настроек
        SharedPreferences sharedPref = getSharedPreferences(preference, MODE_PRIVATE);
        //Прочитать тему, если настройка не найдена - взять по умолчанию true
        return sharedPref.getBoolean(parameter, false);
    }

    // Чтение String настроек
    public String getStringPreference(String preference, String parameter) {
        // Работаем через специальный класс сохранения и чтения настроек
        SharedPreferences sharedPref = getSharedPreferences(preference, MODE_PRIVATE);
        //Прочитать тему, если настройка не найдена - взять по умолчанию true
        return sharedPref.getString(parameter, "UFA");
    }

    // Сохранение boolean настроек
    public void setBooleanPreference(String preference, String parameter, boolean value) {
        SharedPreferences sharedPref = getSharedPreferences(preference, MODE_PRIVATE);
        // Настройки сохраняются посредством специального класса editor.
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(parameter, value);
        editor.apply();
    }

    // Сохранение String настроек
    public void setStringPreference(String preference, String parameter, String value) {
        SharedPreferences sharedPref = getSharedPreferences(preference, MODE_PRIVATE);
        // Настройки сохраняются посредством специального класса editor.
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(parameter, value);
        editor.apply();
    }
}
