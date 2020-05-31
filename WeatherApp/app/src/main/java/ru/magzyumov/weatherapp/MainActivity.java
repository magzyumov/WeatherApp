package ru.magzyumov.weatherapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    private BroadcastReceiver networkReceiver;
    private ActionBarDrawerToggle toggle;
    private TextView alarmBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkReceiver = new NetworkReceiver(this);

        DatabaseCopier.getInstance(getApplicationContext());

        if(getSupportFragmentManager().getFragments().isEmpty()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainLayout, new MainFragment(),"mainFragment").commit();
        }

        Toolbar toolbar = initToolbar();

        initDrawer(toolbar);

        initNotificationChannel();

        registerNetworkReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkReceiver();
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

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("network", "name", importance);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void registerNetworkReceiver() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    private void unregisterNetworkReceiver() {
        try {
            unregisterReceiver(networkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void showAlarm(boolean value){

        alarmBox = findViewById(R.id.textViewAlarm);

        if(alarmBox != null){
            if(value){
                alarmBox.setText(getString(R.string.okInternet));
                alarmBox.setBackgroundColor(Color.GREEN);
                alarmBox.setTextColor(Color.BLACK);

                Handler handler = new Handler();
                Runnable delay = () -> alarmBox.setVisibility(View.GONE);
                handler.postDelayed(delay, 3000);
            }else {
                alarmBox.setVisibility(View.VISIBLE);
                alarmBox.setText(getString(R.string.noInternet));
                alarmBox.setBackgroundColor(Color.RED);
                alarmBox.setTextColor(Color.WHITE);
            }
        }
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

}