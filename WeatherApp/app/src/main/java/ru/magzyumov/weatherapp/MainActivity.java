package ru.magzyumov.weatherapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // На первом старте добавляем основной фрагмент
        if(getSupportFragmentManager().getFragments().isEmpty()){
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, new MainFragment(),"mainFragment").commit();
        }

        //Устанавливаем Toolbar
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_location) {
            // Выполняем транзакцию по замене фрагмента если его нет
            if(getSupportFragmentManager().findFragmentByTag("locationFragment") == null){
                getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, new LocationFragment(),"locationFragment").addToBackStack("").commit();
            }
            return true;
        }

        if (id == R.id.menu_settings) {
            // Выполняем транзакцию по замене фрагмента если его нет
            if(getSupportFragmentManager().findFragmentByTag("settingsFragment") == null){
                getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, new SettingsFragment(), "settingsFragment").addToBackStack("").commit();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
