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
import android.widget.RadioButton;

import ru.magzyumov.weatherapp.App;
import ru.magzyumov.weatherapp.BaseActivity;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Database.Location.LocationDao;
import ru.magzyumov.weatherapp.Database.Location.LocationDataSource;
import ru.magzyumov.weatherapp.Database.Location.LocationSource;
import ru.magzyumov.weatherapp.Database.Location.Locations;
import ru.magzyumov.weatherapp.R;

public class SettingsFragment extends Fragment implements Constants {
    private View view;
    private BaseActivity baseActivity;
    private FragmentChanger fragmentChanger;
    private Locations currentLocation;
    private LocationDao locationDao;
    private LocationDataSource locationSource;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationDao = App.getInstance().getLocationDao();
        locationSource = new LocationSource(locationDao);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_settings, container, false);

        fragmentChanger.setDrawerIndicatorEnabled(false);

        fragmentChanger.changeHeader(getResources().getString(R.string.menu_settings));

        fragmentChanger.showBackButton(true);

        //Инициализируем переключатели
        initRadio(view.findViewById(R.id.onNightMode), SETTING, NIGHT_MODE, false, true);
        initRadio(view.findViewById(R.id.offNightMode), SETTING, NIGHT_MODE, true, true);
        initRadio(view.findViewById(R.id.metricEU), SETTING, EU, true, true);
        initRadio(view.findViewById(R.id.imperialEU), SETTING, EU, false, true);
        initRadio(view.findViewById(R.id.onNotice), SETTING, NOTICE, false, true);
        initRadio(view.findViewById(R.id.offNotice), SETTING, NOTICE, true, false);

        currentLocation = locationSource.getCurrentLocation();

        return view;
    }

    private void initRadio(RadioButton view, String preference, String parameter, boolean inverted, boolean active)  {
        RadioButton radioButton = view;
        radioButton.setChecked((inverted) ? !baseActivity.getBooleanPreference(preference, parameter)
                : baseActivity.getBooleanPreference(preference, parameter));
        radioButton.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (active) {
                    baseActivity.setBooleanPreference(preference, parameter,
                            (inverted) ? !isChecked : isChecked);
                    if(preference == SETTING & parameter == NIGHT_MODE) baseActivity.recreate();
                    if(preference == SETTING & parameter == EU) updateLocation();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        fragmentChanger.showBackButton(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        view = null;
        fragmentChanger = null;
        baseActivity = null;
        locationDao = null;
        locationSource = null;
        currentLocation = null;
    }

    private void updateLocation(){
        if(currentLocation != null) {
            currentLocation.needUpdate = true;
            locationSource.updateLocation(currentLocation);
        }
    }
}
