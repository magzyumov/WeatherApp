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

import ru.magzyumov.weatherapp.MainPresenter;
import ru.magzyumov.weatherapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment  {
    private FragmentChanger fragmentChanger;
    final MainPresenter presenter = MainPresenter.getInstance();

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof FragmentChanger) fragmentChanger = (FragmentChanger) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentChanger = null;
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
        initSwitch(view.findViewById(R.id.switchNightMode), MainPresenter.Field.SETTING_NIGHT_MODE);
        initSwitch(view.findViewById(R.id.switchTempEU), MainPresenter.Field.SETTING_TEMP_EU);
        initSwitch(view.findViewById(R.id.switchWindEU), MainPresenter.Field.SETTING_WIND_EU);
        initSwitch(view.findViewById(R.id.switchPressureEU), MainPresenter.Field.SETTING_PRESS_EU);
        initSwitch(view.findViewById(R.id.switchNotice), MainPresenter.Field.SETTING_NOTICE);
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
