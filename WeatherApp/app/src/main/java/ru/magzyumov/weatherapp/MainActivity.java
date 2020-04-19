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

import java.util.Arrays;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity implements Constants {
    private Intent intent;
    private final int REQUEST_CODE_LOCATION = 1;
    final MainPresenter presenter = MainPresenter.getInstance();
    final PicLogic picLogic = PicLogic.getInstance();

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
        linearLayout.setBackgroundResource(getResources().getIdentifier(picLogic.getBackgroundPicName(),"drawable", getApplicationContext().getPackageName()));

        //Обновляем данные
        picLogic.refreshData();
        makeHeaderTable();
        makeLine();
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

    private void makeLine(){
        String string = "textView1H";
        String image = "imageView1H";

        ImageView imageView;
        TextView textView;

        imageView = findViewById(R.id.imageViewNow);
        imageView.setImageResource(getResources().getIdentifier(picLogic.getLinePic(PicLogic.Field.values()[0]),"drawable", getApplicationContext().getPackageName()));

        int cnt = 0;
        for (int i = 1; i <= 24 ; i++) {
            if((picLogic.getCurrentHour() + i) == 23) cnt = i;
            textView = findViewById(getResources().getIdentifier(string.replace("1", String.valueOf(i)),"id", getApplicationContext().getPackageName()));
            textView.setText(String.format("%02d:00", (((picLogic.getCurrentHour()+i) >= 24) ? (i-cnt-1) : (picLogic.getCurrentHour()+i))));

            imageView = findViewById(getResources().getIdentifier(image.replace("1", String.valueOf(i)),"id", getApplicationContext().getPackageName()));
            imageView.setImageResource(getResources().getIdentifier(picLogic.getLinePic(PicLogic.Field.values()[i]),"drawable", getApplicationContext().getPackageName()));
        }
    }
}
