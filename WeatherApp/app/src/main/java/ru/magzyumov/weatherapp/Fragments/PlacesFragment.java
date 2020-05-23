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
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

import ru.magzyumov.weatherapp.App;
import ru.magzyumov.weatherapp.BaseActivity;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Database.Location.LocationDataSource;
import ru.magzyumov.weatherapp.Dialog.AlertDialogWindow;
import ru.magzyumov.weatherapp.R;
import ru.magzyumov.weatherapp.Database.Location.LocationDao;
import ru.magzyumov.weatherapp.Database.Location.LocationSource;

import static ru.magzyumov.weatherapp.BuildConfig.PLACES_API_KEY;

public class PlacesFragment extends Fragment implements Constants,
        SearchView.OnQueryTextListener, SearchView.OnFocusChangeListener {

    //Объявляем переменные
    private SearchView searchView;
    private LocationDao locationDao;
    private LocationDataSource locationSource;
    private BaseActivity baseActivity;
    private FragmentChanger fragmentChanger;
    private AlertDialogWindow alertDialog;

    private PlacesClient placesClient;
    private AutocompleteSupportFragment autocompleteFragment;


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

        if (!Places.isInitialized()) {
            Places.initialize(getContext(), PLACES_API_KEY);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);

        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autoCompleteFragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.e(TAG, place.getId() + " " + place.getName() + " " + place.getAddress()  + " " + place.getLatLng());
                fragmentChanger.returnFragment();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        initView();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_location, menu);

        // Поиск пункта меню поиска
        MenuItem search = menu.findItem(R.id.searchView);

        // Строка поиска
        searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setOnQueryTextFocusChangeListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }


    @Override
    public boolean onQueryTextChange(String query) {

        return false;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //if(hasFocus) listView.setAdapter(arrayAdapterCities);
        //if(hasFocus) searchView.setQueryHint(getString(R.string.hintSearchCity));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Скрываем кнопку назад
        fragmentChanger.showBackButton(false);

        // Снимаем фокус с поля поиска
        searchView.clearFocus();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Освободдаем ресурсы
        fragmentChanger = null;
        baseActivity = null;
        locationDao = null;
        locationSource = null;
        alertDialog = null;
        autocompleteFragment = null;
    }

    // Небольшие приготовления View
    private void initView(){
        // Деактивируем Drawer
        fragmentChanger.setDrawerIndicatorEnabled(false);

        //Меняем текст в шапке
        fragmentChanger.changeHeader(getResources().getString(R.string.menu_location));
        fragmentChanger.changeSubHeader(getResources().getString(R.string.menu_location));

        //Показываем кнопку назад
        fragmentChanger.showBackButton(true);
    }
}
