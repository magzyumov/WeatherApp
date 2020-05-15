package ru.magzyumov.weatherapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.magzyumov.weatherapp.App;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.R;
import ru.magzyumov.weatherapp.Database.Location.Location;
import ru.magzyumov.weatherapp.Database.Location.LocationDao;
import ru.magzyumov.weatherapp.Database.Location.LocationRecyclerAdapter;
import ru.magzyumov.weatherapp.Database.Location.LocationSource;

public class HistoryFragment extends Fragment implements Constants {

    //Объявляем переменные
    private LocationDao locationDao;
    private LocationSource locationSource;
    private FragmentChanger fragmentChanger;
    private LocationRecyclerAdapter locationRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof FragmentChanger) fragmentChanger = (FragmentChanger) context;
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        // Деактивируем Drawer
        fragmentChanger.setDrawerIndicatorEnabled(false);

        // Инициализируем объект для обращения к базе
        locationDao = App.getInstance().getLocationDao();
        locationSource = new LocationSource(locationDao);

        // Элемент для адаптера
        linearLayoutManager = new LinearLayoutManager(getContext());
        locationRecyclerAdapter = new LocationRecyclerAdapter(locationSource, this);

        //Меняем текст в шапке
        fragmentChanger.changeHeader(getResources().getString(R.string.menu_settings));
        fragmentChanger.changeSubHeader(getResources().getString(R.string.menu_settings));

        //Показываем кнопку назад
        fragmentChanger.showBackButton(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        fragmentChanger.setDrawerIndicatorEnabled(false);   // Деактивируем Drawer

        initRecyclerView(view);

        return view;
    }

    // Инициализация списка
    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.searchHistoryRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(locationRecyclerAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        ContextMenu.ContextMenuInfo menuInfo = item.getMenuInfo();
        int id = item.getItemId();
        switch (id) {
            case R.id.remove_context:
                Location location = locationRecyclerAdapter.getPressedLocation();
                locationSource.setLocationSearched(location.region, location.city, false);
                locationRecyclerAdapter.notifyDataSetChanged();
                return true;
            case R.id.clear_context:
                for (Location locationForUnSearch : locationSource.getHistoryLocations()) {
                    locationSource.setLocationSearched(locationForUnSearch, false);
                }
                locationRecyclerAdapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
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
        fragmentChanger = null;
        locationDao = null;
        locationSource = null;
        fragmentChanger = null;
        locationRecyclerAdapter = null;
    }
}
