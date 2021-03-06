package ru.magzyumov.weatherapp.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import ru.magzyumov.weatherapp.App;
import ru.magzyumov.weatherapp.BaseActivity;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Database.Location.LocationDataSource;
import ru.magzyumov.weatherapp.Dialog.AlertDialogWindow;
import ru.magzyumov.weatherapp.R;
import ru.magzyumov.weatherapp.Database.Location.LocationDao;
import ru.magzyumov.weatherapp.Database.Location.LocationSource;

public class LocationFragment extends Fragment implements Constants,
        SearchView.OnQueryTextListener, SearchView.OnFocusChangeListener,
        ListView.OnItemClickListener {

    //Объявляем переменные
    private ListView listView;
    private SearchView searchView;
    private List<Map<String, String>> arrayListCities;
    private List<Map<String, String>> arrayListHistory;
    private SimpleAdapter arrayAdapterCities;
    private SimpleAdapter arrayAdapterHistory;
    private LocationDao locationDao;
    private LocationDataSource locationSource;
    private BaseActivity baseActivity;
    private FragmentChanger fragmentChanger;
    private AlertDialogWindow alertDialog;

    public LocationFragment() {
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

        // Инициализируем Alert
        alertDialog = new AlertDialogWindow(getContext(), getString(R.string.citySearch),
                getString(R.string.unknownCity), getString(R.string.ok));

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        initView();

        //Инициализируем города для поиска
        listView = view.findViewById(R.id.listView);
        arrayListCities = locationSource.getHashCities();
        arrayListHistory = locationSource.getSearchedLocations();

        arrayAdapterCities = new SimpleAdapter(getContext(), arrayListCities, android.R.layout.simple_list_item_2,
                new String[] {"Region", "City"},
                new int[] {android.R.id.text2, android.R.id.text1, });

        arrayAdapterHistory = new SimpleAdapter(getContext(), arrayListHistory, android.R.layout.simple_list_item_2,
                new String[] {"Region", "City"},
                new int[] {android.R.id.text2, android.R.id.text1, });

        listView.setAdapter((arrayListHistory.size()==0)?arrayAdapterCities:arrayAdapterHistory);
        listView.setOnItemClickListener(this);

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
        baseActivity.setStringPreference(LOCATION, CURRENT_CITY, query);
        if(!contains(arrayListCities, query)) alertDialog.show();
        fragmentChanger.returnFragment();
        return false;
    }

    private boolean contains(List<Map<String, String>> arrayList, String checkValue){
        for (Map<String, String> entry : arrayList) {
            for (String key : entry.keySet()) {
                String value = entry.get(key);
                if (value.equals(checkValue)) return true;
            }
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        arrayAdapterCities.getFilter().filter(query);
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textViewCity = view.findViewById(android.R.id.text1);
        TextView textViewRegion = view.findViewById(android.R.id.text2);

        String city = textViewCity.getText().toString();
        String region = textViewRegion.getText().toString();

        if(region.equals("")){
            alertDialog.show(getString(R.string.locationNotFound));
            return;
        }

        searchView.setQuery(city,true);
        locationSource.setLocationSearched(region, city, true);
        locationSource.setLocationCurrent(region, city, true);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus) listView.setAdapter(arrayAdapterCities);
        if(hasFocus) searchView.setQueryHint(getString(R.string.hintSearchCity));

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
        arrayAdapterCities = null;
        arrayAdapterHistory = null;
        arrayListHistory = null;
        arrayListCities = null;
        alertDialog = null;
    }

    // Небольшие приготовления View
    private void initView(){
        fragmentChanger.setDrawerIndicatorEnabled(false);

        fragmentChanger.changeHeader(getResources().getString(R.string.menu_location));

        fragmentChanger.showBackButton(true);
    }
}
