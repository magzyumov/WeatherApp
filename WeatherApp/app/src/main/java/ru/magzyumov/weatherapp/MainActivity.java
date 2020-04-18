package ru.magzyumov.weatherapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements Constants {
    private Intent intent;
    private final int REQUEST_CODE_LOCATION = 1;
    final MainPresenter presenter = MainPresenter.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Устанавливаем Toolbar
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        //Иницилизируем кнопку-ссылку
        initBottomLink();

        //Ставим background картинку
        LinearLayout linearLayout = findViewById(R.id.linearLayoutPic);
        linearLayout.setBackgroundResource(getResources().getIdentifier(getBackgroundPic(),"drawable", getApplicationContext().getPackageName()));

        makeHeaderTable();
        makeDateTime();
    }

    private void initBottomLink(){
        TextView textView = findViewById(R.id.textViewProvider);
        textView.setText(Html.fromHtml("<a href=" + PROVIDER_URL + "><font color=#AAA>" + getString(R.string.provider) + "</font></a>"));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(PROVIDER_URL));
                startActivity(browser);
            }
        });
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

        getCurrCity(presenter.getCurrentCity());
    }

    private void getCurrCity(String city){
        String cityStr = (city==null)?(getResources().getString(R.string.currentCityName)):city;
        TextView currCity = findViewById(R.id.textViewCurrentCity);
        currCity.setText(cityStr);
    }

    private String getBackgroundPic(){
        boolean dayClear = false;
        StringBuilder picName = new StringBuilder();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        int month = calendar.get(Calendar.MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        switch (month) {
            case Calendar.DECEMBER:
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
                picName.append("winter_city_");
                break;
            case Calendar.MARCH:
            case Calendar.APRIL:
            case Calendar.MAY:
                picName.append("spring_city_");
                break;
            case Calendar.JUNE:
            case Calendar.JULY:
            case Calendar.AUGUST:
                picName.append("summer_city_");
                break;
            case Calendar.SEPTEMBER:
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            default:
                picName.append("autumn_city_");
                break;
        }

        if(((hour >= 6) & (hour <= 8)) || ((hour >= 19) & (hour <= 20))) picName.append("dawn_");
        if((hour >= 9) & (hour <= 18)) picName.append("day_");
        if((hour >= 21) || (hour <= 5)) picName.append("night_");

        picName.append(dayClear ? ("clear") : ("overcast"));

        return picName.toString();
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
