package ru.magzyumov.weatherapp;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import ru.magzyumov.weatherapp.Fragments.FragmentChanger;
import ru.magzyumov.weatherapp.Fragments.FragmentFinder;
import ru.magzyumov.weatherapp.Fragments.HistoryFragment;
import ru.magzyumov.weatherapp.Fragments.LocationFragment;
import ru.magzyumov.weatherapp.Fragments.MainFragment;
import ru.magzyumov.weatherapp.Fragments.SettingsFragment;
import ru.magzyumov.weatherapp.Database.Init.DatabaseCopier;
import ru.magzyumov.weatherapp.Fragments.PlacesFragment;


public class MainActivity extends BaseActivity implements FragmentChanger, NavigationView.OnNavigationItemSelectedListener{
    private FragmentFinder fragmentFinder = new FragmentFinder(getSupportFragmentManager());
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseCopier.getInstance(getApplicationContext());

        if(getSupportFragmentManager().getFragments().isEmpty()){
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, new MainFragment(),"mainFragment").commit();
        }

        //Устанавливаем Toolbar
        Toolbar toolbar = initToolbar();

        initDrawer(toolbar);
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnFragment();
            }
        });

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void changeFragment(Fragment newFragment, String newFragmentTag, boolean addToBackStack, Bundle args) {
        // Проверяем, если данный фрагмент в стеке
        Fragment fragment = fragmentFinder.findFragment(newFragmentTag);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Выполняем транзакцию по замене фрагмента если его нет
        if (fragment == null) {
            transaction.replace(R.id.mainLayout, newFragment, newFragmentTag);
            if(addToBackStack) transaction.addToBackStack("");
            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void changeHeader(String text) {
        getSupportActionBar().setTitle(text);
    }

    @Override
    public void changeSubHeader(String text) {
        getSupportActionBar().setSubtitle(text);
    }

    @Override
    public void showBackButton(boolean show) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(show);
    }

    @Override
    public void returnFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void setDrawerIndicatorEnabled(boolean enabled) {
        toggle.setDrawerIndicatorEnabled(enabled);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            // Вынимаем из стека послдний фрагмент
            returnFragment();
        } else if (id == R.id.nav_location) {
            // Выполняем транзакцию по замене фрагмента
            changeFragment(new LocationFragment(),"locationFragment", true, null);
        } else if (id == R.id.nav_history) {
            // Выполняем транзакцию по замене фрагмента
            changeFragment(new HistoryFragment(),"historyFragment", true, null);
        } else if (id == R.id.nav_settings) {
            // Выполняем транзакцию по замене фрагмента
            changeFragment(new SettingsFragment(),"settingsFragment", true, null);
        } else if (id == R.id.nav_google) {
            // Выполняем транзакцию по замене фрагмента
            changeFragment(new PlacesFragment(),"placesFragment", true, null);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public FragmentTransaction getFragmentTransaction(){
        return getSupportFragmentManager().beginTransaction();
    }
}
