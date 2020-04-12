package ru.magzyumov.helloactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_home) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.menu_settings) {
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        intent = new Intent(this, MainActivity.class);
        intent.putExtra("newCity", query);
        startActivity(intent);
        //Toast.makeText(LocationActivity.this, query, Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //Toast.makeText(LocationActivity.this, newText, Toast.LENGTH_LONG).show();
        return false;
    }
}
