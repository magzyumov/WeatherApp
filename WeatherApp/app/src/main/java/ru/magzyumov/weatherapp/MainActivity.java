package ru.magzyumov.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Intent intent;
    private final int REQUEST_CODE_LOCATION = 1;
    private final String TAG = "WeatherApp";
    final MainPresenter presenter = MainPresenter.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            makeToast("Первый запуск!");
        } else {
            makeToast("Повторный запуск!");
        }

        //Устанавливаем Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Устанавливаем Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        makeHeaderTable();
        makeDateTime();
    }

    //Begin Методы для проверки жизненного цикла
    @Override
    protected void onStart() {
        super.onStart();
        makeToast("onStart()");
    }

    @Override
    protected void onRestoreInstanceState(Bundle saveInstanceState){
        super.onRestoreInstanceState(saveInstanceState);
        makeToast("Повторный запуск!! - onRestoreInstanceState()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeToast("onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        makeToast("onPause()");
    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);
        makeToast("onSaveInstanceState()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        makeToast("onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        makeToast("onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        makeToast("onDestroy()");
    }

    private void makeToast (String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, message);
    }
    //End Методы для проверки жизненного цикла


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_LOCATION:
                    getCurrCity(data.getStringExtra("newCity"));
                    break;
            }
        } else {
            Toast.makeText(getApplicationContext(), "Wrong result", Toast.LENGTH_SHORT).show();
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
