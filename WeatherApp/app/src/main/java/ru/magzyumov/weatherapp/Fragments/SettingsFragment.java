package ru.magzyumov.weatherapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.android.material.switchmaterial.SwitchMaterial;

import ru.magzyumov.weatherapp.BaseActivity;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.R;

public class SettingsFragment extends Fragment implements Constants {
    private View view;
    private BaseActivity baseActivity;
    private FragmentChanger fragmentChanger;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof FragmentChanger) fragmentChanger = (FragmentChanger) context;
        if(context instanceof BaseActivity) baseActivity = (BaseActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Деактивируем Drawer
        fragmentChanger.setDrawerIndicatorEnabled(false);

        //Меняем текст в шапке
        fragmentChanger.changeHeader(getResources().getString(R.string.menu_settings));
        fragmentChanger.changeSubHeader(getResources().getString(R.string.menu_settings));

        //Показываем кнопку назад
        fragmentChanger.showBackButton(true);

        //Инициализируем переключатели
        initSwitch(view.findViewById(R.id.switchNightMode), SETTING, NIGHT_MODE);
        initSwitch(view.findViewById(R.id.switchTempEU), SETTING, TEMP_EU);
        initSwitch(view.findViewById(R.id.switchWindEU), SETTING, WIND_EU);
        initSwitch(view.findViewById(R.id.switchPressureEU), SETTING, PRESS_EU);
        initSwitch(view.findViewById(R.id.switchNotice), SETTING, NOTICE);

        return view;
    }


    private void initSwitch(SwitchMaterial view, String preference, String parameter)  {
        SwitchMaterial switchButton = view;
        switchButton.setChecked(baseActivity.getBooleanPreference(preference, parameter));
        switchButton.setOnCheckedChangeListener(new SwitchMaterial.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                baseActivity.setBooleanPreference(preference, parameter, isChecked);
                if(preference == SETTING & parameter == NIGHT_MODE) baseActivity.recreate();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Скрываем кнопку назад
        fragmentChanger.showBackButton(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Освобождаем ресурсы
        fragmentChanger = null;
        baseActivity = null;
    }
}
