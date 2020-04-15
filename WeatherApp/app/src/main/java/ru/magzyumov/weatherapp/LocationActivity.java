package ru.magzyumov.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    //Объявляем переменные
    private Toolbar toolbar;
    private SearchView searchView;
    private Intent intent;
    private String newCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //Устанавливаем Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Инициализируем строку поиска
        searchView = findViewById(R.id.searchViewCity);
        searchView.setOnQueryTextListener(this);

        //Меняем текст в шапке
        getSupportActionBar().setTitle(R.string.menu_location);

        //Показываем кнопку назад
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putString("newCity", newCity); // Сохраняем введеное значение города
    }

    @Override
    protected void onRestoreInstanceState(Bundle saveInstanceState){
        super.onRestoreInstanceState(saveInstanceState);
        newCity = saveInstanceState.getString("newCity"); // Восстанавливаем введенное значение поиска
        searchView.setQuery(newCity,false);     //вводим раннее введенный текст
        TextView tV = findViewById(R.id.textVievLastQuery);
        tV.setText(newCity);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        intent = new Intent();
        intent.putExtra("newCity", query);
        setResult(RESULT_OK, intent);
        finish();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newCity = newText;
        return false;
    }
}
