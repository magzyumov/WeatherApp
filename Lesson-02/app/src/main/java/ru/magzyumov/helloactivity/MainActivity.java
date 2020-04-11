package ru.magzyumov.helloactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intent = getIntent();

        makeDateTime();
        getCurrTemperature();
        getCurrCity(intent.getStringExtra("newCity"));
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
            intent = new Intent(this, LocationActivity.class);
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

    private void getCurrCity(String city){
        String cityStr = (city==null)?(getResources().getString(R.string.currentCityName)):city;
        TextView currCity = findViewById(R.id.currentCityName);
        currCity.setText(cityStr);
    }

    private void getCurrTemperature(){
        Random random = new Random();
        Integer currentTemperature = random.nextInt(50);
        TextView currTempTextView = findViewById(R.id.currentTemperature);
        String str = currentTemperature.toString() + " " + getResources().getString(R.string.celsius);
        currTempTextView.setText(str);
    }

    private void makeDateTime(){
        Date currentDate = new Date();

        DateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        DateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        String dayText = dayFormat.format(currentDate);
        String dateText = dateFormat.format(currentDate);
        String timeText = timeFormat.format(currentDate);

        TextView currentDayTextView = findViewById(R.id.currentDayTextView);
        TextView currentDateTextView = findViewById(R.id.currentDateTextView);
        TextView currentTimeTextView = findViewById(R.id.currentTimeTextView);

        currentDayTextView.setText(dayText);
        currentDateTextView.setText(dateText);
        currentTimeTextView.setText(timeText);
    }
}
