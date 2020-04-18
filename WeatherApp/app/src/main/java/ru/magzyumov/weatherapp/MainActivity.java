package ru.magzyumov.weatherapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements Constants {
    private Toolbar toolbar;
    private Intent intent;
    private final int REQUEST_CODE_LOCATION = 1;
    private final String TAG = "WeatherApp";
    final MainPresenter presenter = MainPresenter.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Устанавливаем Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Устанавливаем Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Иницилизируем кнопку-ссылку
        TextView textView = findViewById(R.id.textViewProvider);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(PROVIDER_URL));
                startActivity(browser);
            }
        });

        makeHeaderTable();
        makeDateTime();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_LOCATION) & (resultCode == RESULT_OK)) {
            getCurrCity(data.getStringExtra("newCity"));
        }
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
            startActivityForResult(intent, REQUEST_CODE_LOCATION);
            return true;
        }
        if (id == R.id.menu_settings) {
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeHeaderTable(){
        TextView textViewCurrent = findViewById(R.id.textViewCurrent);
        textViewCurrent.setText(((Integer)presenter.getCurrentTemp()).toString());

        TextView textViewCurrentEU = findViewById(R.id.textViewCurrentEU);
        textViewCurrentEU.setText(getResources().getString(R.string.celsius));

        ImageView imageViewCurrent = findViewById(R.id.imageViewCurrent);
        imageViewCurrent.setImageResource(R.drawable.day_snow);
    }

    private void getCurrCity(String city){
        String cityStr = (city==null)?(getResources().getString(R.string.currentCityName)):city;
        TextView currCity = findViewById(R.id.textViewCurrentCity);
        currCity.setText(cityStr);
    }

    private void makeDateTime(){
        Date currentDate = new Date();

        DateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM", Locale.getDefault());
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        String dayText = dayFormat.format(currentDate);
        String dateText = dateFormat.format(currentDate);
        String timeText = timeFormat.format(currentDate);

        TextView currentDayTextView = findViewById(R.id.textViewTextToday);
        TextView currentDateTextView = findViewById(R.id.textViewDateToday);
        TextView currentTimeTextView = findViewById(R.id.textView1H);

        currentDayTextView.setText(dayText);
        currentDateTextView.setText(dateText);
        currentTimeTextView.setText(timeText);
    }
}
