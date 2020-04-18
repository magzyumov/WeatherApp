package ru.magzyumov.weatherapp;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {
    final MainPresenter presenter = MainPresenter.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Устанавливаем Toolbar
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        //Инициализируем переключатели
        initSwitch((Switch) findViewById(R.id.switchNightMode), MainPresenter.Field.SETTING_NIGHT_MODE);
        initSwitch((Switch) findViewById(R.id.switchTempEU), MainPresenter.Field.SETTING_TEMP_EU);
        initSwitch((Switch) findViewById(R.id.switchWindEU), MainPresenter.Field.SETTING_WIND_EU);
        initSwitch((Switch) findViewById(R.id.switchPressureEU), MainPresenter.Field.SETTING_PRESS_EU);
        initSwitch((Switch) findViewById(R.id.switchNotice), MainPresenter.Field.SETTING_NOTICE);

        //Меняем текст в шапке
        getSupportActionBar().setTitle(R.string.menu_settings);

        //Показываем кнопку назад
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initSwitch(Switch view, final MainPresenter.Field field)  {
        Switch switchButton = view;
        switchButton.setChecked(presenter.getSwitch(field));
        switchButton.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.setSwitch(isChecked, field);
            }
        });
    }
}