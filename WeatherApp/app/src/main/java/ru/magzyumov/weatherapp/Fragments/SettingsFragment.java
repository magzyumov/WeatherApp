package ru.magzyumov.weatherapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    public void onDetach() {
        super.onDetach();
        fragmentChanger = null;
        baseActivity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Меняем текст в шапке
        fragmentChanger.changeHeader(getResources().getString(R.string.menu_settings));
        fragmentChanger.changeSubHeader(getResources().getString(R.string.menu_settings));

        //Показываем кнопку назад
        fragmentChanger.showBackButton(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Инициализируем переключатели
        initSwitch(view.findViewById(R.id.switchNightMode), SETTING, NIGHT_MODE);
        initSwitch(view.findViewById(R.id.switchTempEU), SETTING, TEMP_EU);
        initSwitch(view.findViewById(R.id.switchWindEU), SETTING, WIND_EU);
        initSwitch(view.findViewById(R.id.switchPressureEU), SETTING, PRESS_EU);
        initSwitch(view.findViewById(R.id.switchNotice), SETTING, NOTICE);
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
}
