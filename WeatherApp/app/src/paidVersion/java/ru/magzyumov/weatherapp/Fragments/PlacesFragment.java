package ru.magzyumov.weatherapp.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

import ru.magzyumov.weatherapp.App;
import ru.magzyumov.weatherapp.BaseActivity;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Database.Firebase.FirebasePlace;
import ru.magzyumov.weatherapp.Database.Location.LocationDataSource;
import ru.magzyumov.weatherapp.Database.Location.Locations;
import ru.magzyumov.weatherapp.R;
import ru.magzyumov.weatherapp.Database.Location.LocationDao;
import ru.magzyumov.weatherapp.Database.Location.LocationSource;

import static ru.magzyumov.weatherapp.BuildConfig.PLACES_API_KEY;

public class PlacesFragment extends Fragment implements Constants {

    //Объявляем переменные
    private LocationDao locationDao;
    private LocationDataSource locationSource;
    private BaseActivity baseActivity;
    private FragmentChanger fragmentChanger;
    private AutocompleteSupportFragment autocompleteFragment;
    private DatabaseReference mDatabase;


    public PlacesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof FragmentChanger) fragmentChanger = (FragmentChanger) context;
        if(context instanceof BaseActivity) baseActivity = (BaseActivity) context;
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        // Инициализируем объект для обращения к базе
        locationDao = App.getInstance().getLocationDao();
        locationSource = new LocationSource(locationDao);

        // Инициализируем Google Places
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), PLACES_API_KEY);
        }

        // Инициализация Google Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);

        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autoCompleteFragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));

        autocompleteFragment.setActivityMode(AutocompleteActivityMode.FULLSCREEN);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                String key = mDatabase.child("locations").push().getKey();
                FirebasePlace location = new FirebasePlace(place.getId(), place.getName(), place.getAddress(),
                        String.valueOf(place.getLatLng().latitude), String.valueOf(place.getLatLng().longitude));

                Locations currentLocation = locationSource.getCurrentLocation();
                if(currentLocation != null){
                    currentLocation.isCurrent = false;
                    locationSource.updateLocation(currentLocation);
                }
                Locations futureLocation = new Locations(place.getLatLng().latitude,
                        place.getLatLng().longitude, place.getName(), place.getAddress());
                locationSource.addLocation(futureLocation);

                mDatabase.child("locations").child(place.getId()).setValue(location);
                fragmentChanger.returnFragment();
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        initView();

        return view;
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

        // Освободдаем ресурсы
        fragmentChanger = null;
        baseActivity = null;
        locationDao = null;
        locationSource = null;
        autocompleteFragment = null;
    }

    // Небольшие приготовления View
    private void initView(){
        fragmentChanger.setDrawerIndicatorEnabled(false);

        fragmentChanger.changeHeader(getResources().getString(R.string.menu_location));

        fragmentChanger.showBackButton(true);
    }
}
