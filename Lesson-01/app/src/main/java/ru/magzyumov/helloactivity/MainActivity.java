package ru.magzyumov.helloactivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.os.Bundle;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM);

        makeDateTime();
        getCurrTemperature();
        getCurrCity();
    }

    private void getCurrCity(){
        TextView currCity = findViewById(R.id.currentCityName);
        currCity.setText(getResources().getString(R.string.currentCityName));
    }

    private void getCurrTemperature(){
        Random random = new Random();
        Integer currentTemperature = random.nextInt(50);
        TextView currTempTextView = findViewById(R.id.currentTemperature);
        currTempTextView.setText(currentTemperature.toString());
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
