package ru.magzyumov.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

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

    // Проверка на наличие настройки
    public boolean isContain(String preference, String parameter){
        SharedPreferences sharedPref = getSharedPreferences(preference, MODE_PRIVATE);
        return sharedPref.contains(parameter);
    }

    // Чтение boolean настроек
    public boolean getBooleanPreference(String preference, String parameter) {
        SharedPreferences sharedPref = getSharedPreferences(preference, MODE_PRIVATE);
        return sharedPref.getBoolean(parameter, false);
    }

    // Чтение String настроек
    public String getStringPreference(String preference, String parameter) {
        SharedPreferences sharedPref = getSharedPreferences(preference, MODE_PRIVATE);
        return sharedPref.getString(parameter, "");
    }

    // Чтение String настроек
    public Set getStringSetPreference(String preference, String parameter) {
        Set set = new HashSet<String>();
        SharedPreferences sharedPref = getSharedPreferences(preference, MODE_PRIVATE);
        return sharedPref.getStringSet(parameter, set);
    }

    // Сохранение boolean настроек
    public void setBooleanPreference(String preference, String parameter, boolean value) {
        SharedPreferences sharedPref = getSharedPreferences(preference, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(parameter, value);
        editor.apply();
    }

    // Сохранение String настроек
    public void setStringPreference(String preference, String parameter, String value) {
        SharedPreferences sharedPref = getSharedPreferences(preference, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(parameter, value);
        editor.apply();
    }

    // Сохранение String настроек
    public void setStringSetPreference(String preference, String parameter, String value) {
        Set set = getStringSetPreference(preference, parameter);
        SharedPreferences sharedPref = getSharedPreferences(preference, MODE_PRIVATE);
        set.add(value);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(parameter, set);
        editor.apply();
    }
}
