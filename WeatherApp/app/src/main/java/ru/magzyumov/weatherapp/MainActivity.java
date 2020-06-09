package ru.magzyumov.weatherapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import ru.magzyumov.weatherapp.BroadcastReceivers.BatteryReceiver;
import ru.magzyumov.weatherapp.BroadcastReceivers.NetworkReceiver;
import ru.magzyumov.weatherapp.CloudMessaging.TokenSender;
import ru.magzyumov.weatherapp.Fragments.FragmentChanger;
import ru.magzyumov.weatherapp.Fragments.FragmentFinder;
import ru.magzyumov.weatherapp.Fragments.GeoMapFragment;
import ru.magzyumov.weatherapp.Fragments.HistoryFragment;
import ru.magzyumov.weatherapp.Fragments.LocationFragment;
import ru.magzyumov.weatherapp.Fragments.MainFragment;
import ru.magzyumov.weatherapp.Fragments.MembersFragment;
import ru.magzyumov.weatherapp.Fragments.SendPushFragment;
import ru.magzyumov.weatherapp.Fragments.SettingsFragment;
import ru.magzyumov.weatherapp.Database.Init.DatabaseCopier;
import ru.magzyumov.weatherapp.Fragments.PlacesFragment;


public class MainActivity extends BaseActivity implements FragmentChanger, NavigationView.OnNavigationItemSelectedListener{
    private FragmentFinder fragmentFinder = new FragmentFinder(getSupportFragmentManager());
    private BroadcastReceiver networkReceiver;
    private BroadcastReceiver batteryReceiver;
    private ActionBarDrawerToggle toggle;
    private TextView alarmBox;
    private BaseActivity baseActivity;
    private TokenSender tokenSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(this instanceof BaseActivity) baseActivity = (BaseActivity) this;

        if(tokenSender == null) tokenSender = new TokenSender(getApplicationContext());

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(this, fcmListener);

        networkReceiver = new NetworkReceiver(this);
        batteryReceiver = new BatteryReceiver(this);

        DatabaseCopier.getInstance(getApplicationContext());

        if(getSupportFragmentManager().getFragments().isEmpty()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainLayout, new MainFragment(),"mainFragment").commit();
        }

        Toolbar toolbar = initToolbar();

        initDrawer(toolbar);

        initNotificationChannel();

        registerReceivers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceivers();
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
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            returnFragment();
        } else if (id == R.id.nav_location) {
            changeFragment(new LocationFragment(),"locationFragment", true, null);
        } else if (id == R.id.nav_history) {
            changeFragment(new HistoryFragment(),"historyFragment", true, null);
        } else if (id == R.id.nav_settings) {
            changeFragment(new SettingsFragment(),"settingsFragment", true, null);
        } else if (id == R.id.nav_google) {
            changeFragment(new PlacesFragment(),"placesFragment", true, null);
        } else if (id == R.id.nav_send_push) {
            changeFragment(new SendPushFragment(),"sendPushFragment", true, null);
        } else if (id == R.id.nav_geomap) {
            changeFragment(new GeoMapFragment(),"geoMapFragment", true, null);
        } else if (id == R.id.nav_members) {
            changeFragment(new MembersFragment(),"MembersFragment", true, null);
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
        if(!baseActivity.isContain(SETTING, NOTICE)){
            baseActivity.setBooleanPreference(SETTING,NOTICE,true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;

            NotificationChannel network = new NotificationChannel("network", "Network", importance);
            notificationManager.createNotificationChannel(network);

            NotificationChannel pushMessage = new NotificationChannel("pushMessage", "Cloud", importance);
            notificationManager.createNotificationChannel(pushMessage);
        }
    }

    private void registerReceivers() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

            IntentFilter  intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
            intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
            intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
            intentFilter.addAction(Intent.ACTION_BATTERY_OKAY);
            registerReceiver(batteryReceiver, intentFilter);
        }
    }

    private void unregisterReceivers() {
        try {
            unregisterReceiver(networkReceiver);
            unregisterReceiver(batteryReceiver);
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

    OnSuccessListener<InstanceIdResult> fcmListener = new OnSuccessListener<InstanceIdResult>(){
        @Override
        public void onSuccess(InstanceIdResult instanceIdResult) {
            String token = instanceIdResult.getToken();
            tokenSender.sendRegistrationToServer(token);
        }
    };

}