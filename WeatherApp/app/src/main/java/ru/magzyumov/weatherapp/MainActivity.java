package ru.magzyumov.weatherapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ru.magzyumov.weatherapp.Fragments.FragmentChanger;
import ru.magzyumov.weatherapp.Fragments.FragmentFinder;
import ru.magzyumov.weatherapp.Fragments.LocationFragment;
import ru.magzyumov.weatherapp.Fragments.MainFragment;
import ru.magzyumov.weatherapp.Fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity implements FragmentChanger {
    private FragmentFinder fragmentFinder = new FragmentFinder(getSupportFragmentManager());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // На первом старте добавляем основной фрагмент
        if(getSupportFragmentManager().getFragments().isEmpty()){
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, new MainFragment(),"mainFragment").commit();
        }

        //Устанавливаем Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStack();
            }
        });
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

    @Override
    public void changeFragment(String tag, Bundle args, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = fragmentFinder.findFragment(tag);
        transaction.replace(R.id.mainLayout, fragment,tag);
        if(addToBackStack) transaction.addToBackStack("");
        transaction.commitAllowingStateLoss();
    }
}
