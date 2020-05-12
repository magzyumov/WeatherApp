package ru.magzyumov.weatherapp;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ru.magzyumov.weatherapp.Forecast.CurrentForecastParcel;
import ru.magzyumov.weatherapp.Forecast.DailyForecastParcel;
import ru.magzyumov.weatherapp.Forecast.ForecastListener;
import ru.magzyumov.weatherapp.Forecast.GetData;
import ru.magzyumov.weatherapp.Fragments.FragmentChanger;
import ru.magzyumov.weatherapp.Fragments.FragmentFinder;
import ru.magzyumov.weatherapp.Fragments.HistoryFragment;
import ru.magzyumov.weatherapp.Fragments.LocationFragment;
import ru.magzyumov.weatherapp.Fragments.MainFragment;
import ru.magzyumov.weatherapp.Fragments.SettingsFragment;
import ru.magzyumov.weatherapp.room.init.DatabaseCopier;

public class MainActivity extends BaseActivity implements FragmentChanger, ForecastListener {
    private FragmentFinder fragmentFinder = new FragmentFinder(getSupportFragmentManager());
    private CurrentForecastParcel currentForecastParcel;
    private DailyForecastParcel dailyForecastParcel;
    private Bundle bundle;
    private GetData getData;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseCopier.getInstance(getApplicationContext());

        currentForecastParcel = new CurrentForecastParcel();
        dailyForecastParcel = new DailyForecastParcel();

        getData = new GetData(currentForecastParcel, dailyForecastParcel, getApplicationContext());
        getData.addListener(this);
        getData.build();

        //Устанавливаем Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { returnFragment(); }
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

        if (id == R.id.menu_history) {
            // Выполняем транзакцию по замене фрагмента если его нет
            if(getSupportFragmentManager().findFragmentByTag("historyFragment") == null){
                getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, new HistoryFragment(), "historyFragment").addToBackStack("").commit();
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
    public void initActivity(){
        boolean refreshFragment = false;

        bundle = new Bundle();

        bundle.putParcelable(CURRENT_FORECAST, currentForecastParcel);
        bundle.putParcelable(DAILY_FORECAST, dailyForecastParcel);

        // На первом старте добавляем основной фрагмент
        MainFragment mainFragment = (MainFragment) fragmentFinder.findFragment("mainFragment");
        if (mainFragment == null ){
            mainFragment = new MainFragment();
        } else {
            refreshFragment = true;
        }

        if(getSupportFragmentManager().getFragments().isEmpty()){
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, mainFragment.getClass(), bundle, "mainFragment").commit();
        }

        if (refreshFragment){
            mainFragment.setArguments(bundle);

            mainFragment.getParcel();
            mainFragment.initDailyForecast();
            mainFragment.makeHeaderTable();
        }
    }

    @Override
    public void showMessage(String message) {
        Toast toast = Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = toast.getView();
        view.setBackgroundResource(R.drawable.border);
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setTextSize(36);
        toast.show();
    }
}
