package ru.magzyumov.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

public class LocationActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    //Объявляем переменные
    private Toolbar toolbar;
    private SearchView searchView;
    private Intent intent;

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
    public boolean onQueryTextSubmit(String query) {
        intent = new Intent();
        intent.putExtra("newCity", query);
        setResult(RESULT_OK, intent);
        finish();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
