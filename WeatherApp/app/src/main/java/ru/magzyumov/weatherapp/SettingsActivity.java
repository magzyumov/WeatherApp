package ru.magzyumov.weatherapp;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Switch switchNightMode;
    private Switch switchTempEU;
    private Switch switchWindEU;
    private Switch switchPressureEU;
    private Switch switchNotice;
    final MainPresenter presenter = MainPresenter.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        //Устанавливаем Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Инициализируем переключатели настроек
        initSwitch();

        //Меняем текст в шапке
        getSupportActionBar().setTitle(R.string.menu_settings);

        //Показываем кнопку назад
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initSwitch(){
        switchNightMode = findViewById(R.id.switchNightMode);
        switchNightMode.setChecked(presenter.getSettingNightMode());
        switchNightMode.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    presenter.setSettingNightMode(true);
                } else {
                    presenter.setSettingNightMode(false);
                }
            }
        });

        switchTempEU = findViewById(R.id.switchTempEU);
        switchTempEU.setChecked(presenter.getSettingTempEu());
        switchTempEU.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    presenter.setSettingTempEu(true);
                } else {
                    presenter.setSettingTempEu(false);
                }
            }
        });

        switchWindEU = findViewById(R.id.switchWindEU);
        switchWindEU.setChecked(presenter.getSettingWindEU());
        switchWindEU.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    presenter.setSettingWindEU(true);
                } else {
                    presenter.setSettingWindEU(false);
                }
            }
        });

        switchPressureEU = findViewById(R.id.switchPressureEU);
        switchPressureEU.setChecked(presenter.getSettingPressEU());
        switchPressureEU.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    presenter.setSettingPressEU(true);
                } else {
                    presenter.setSettingPressEU(false);
                }
            }
        });

        switchNotice = findViewById(R.id.switchNotice);
        switchNotice.setChecked(presenter.getSettingNotice());
        switchNotice.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    presenter.setSettingNotice(true);
                } else {
                    presenter.setSettingNotice(false);
                }
            }
        });
    }
}