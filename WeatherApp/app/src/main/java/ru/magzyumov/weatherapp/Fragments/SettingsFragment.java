package ru.magzyumov.weatherapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.material.switchmaterial.SwitchMaterial;

import ru.magzyumov.weatherapp.BaseActivity;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.MainPresenter;
import ru.magzyumov.weatherapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements Constants {
    private FragmentChanger fragmentChanger;
    private BaseActivity baseActivity;
    final MainPresenter presenter = MainPresenter.getInstance();

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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.menu_settings);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.menu_settings);

        //Показываем кнопку назад
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        switchButton.setChecked(baseActivity.getPreference(preference, parameter));
        switchButton.setOnCheckedChangeListener(new SwitchMaterial.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                baseActivity.setPreference(preference, parameter, isChecked);
                if(preference == SETTING & parameter == NIGHT_MODE) baseActivity.recreate();
            }
        });
    }
}
