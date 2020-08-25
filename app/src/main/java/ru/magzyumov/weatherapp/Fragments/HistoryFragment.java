package ru.magzyumov.weatherapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.magzyumov.weatherapp.App;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Database.Location.LocationDataSource;
import ru.magzyumov.weatherapp.Database.Location.Locations;
import ru.magzyumov.weatherapp.R;
import ru.magzyumov.weatherapp.Database.Location.LocationDao;
import ru.magzyumov.weatherapp.Database.Location.LocationRecyclerAdapter;
import ru.magzyumov.weatherapp.Database.Location.LocationSource;

public class HistoryFragment extends Fragment implements Constants {

    //Объявляем переменные
    private LocationDao locationDao;
    private LocationDataSource locationSource;
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

        // Инициализируем объект для обращения к базе
        locationDao = App.getInstance().getLocationDao();
        locationSource = new LocationSource(locationDao);

        // Элемент для адаптера
        linearLayoutManager = new LinearLayoutManager(getContext());
        locationRecyclerAdapter = new LocationRecyclerAdapter(locationSource, this);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        initView();

        initRecyclerView(view);

        return view;
    }

    // Инициализация списка
    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.searchHistoryRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(locationRecyclerAdapter);

        //Установка слушателя на позицию
        locationRecyclerAdapter.setOnItemClickListener(new LocationRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String region = ((TextView)view.findViewById(R.id.textViewRegion)).getText().toString();
                String city = ((TextView)view.findViewById(R.id.textViewCity)).getText().toString();

                // Выставляем флаг о том, что нужны обновления данных
                locationSource.setLocationCurrent(region, city, true);

                // Возвращаемся назад
                fragmentChanger.returnFragment();
            }
        });

        //Установка слушателя на меню
        locationRecyclerAdapter.setOnMenuClickListener(new LocationRecyclerAdapter.OnMenuClickListener() {
            @Override
            public void onMenuClick(View view, int position) {
                PopupMenu menu = new PopupMenu(getContext(), view);
                requireActivity().getMenuInflater().inflate(R.menu.menu_popup, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.remove_context:
                                // Не особо красивое решение
                                String[] arr = view.getContentDescription().toString().split("~",2);

                                String region = arr[0];
                                String city = arr[1];

                                locationSource.setLocationSearched(region, city, false);
                                locationRecyclerAdapter.notifyDataSetChanged();
                                return true;
                            case R.id.clear_context:
                                for (Locations locationForUnSearch : locationSource.getHistoryLocations()) {
                                    locationSource.setLocationSearched(locationForUnSearch, false);
                                }
                                locationRecyclerAdapter.notifyDataSetChanged();
                                return true;
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        ContextMenu.ContextMenuInfo menuInfo = item.getMenuInfo();
        int id = item.getItemId();
        switch (id) {
            case R.id.remove_context:
                Locations location = locationRecyclerAdapter.getPressedLocation();
                locationSource.setLocationSearched(location.region, location.city, false);
                locationRecyclerAdapter.notifyDataSetChanged();
                return true;
            case R.id.clear_context:
                for (Locations locationForUnSearch : locationSource.getHistoryLocations()) {
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

        // Освобождаем ресурсы
        fragmentChanger = null;
        locationDao = null;
        locationSource = null;
        fragmentChanger = null;
        locationRecyclerAdapter = null;
    }

    // Небольшие приготовления View
    private void initView(){
        fragmentChanger.setDrawerIndicatorEnabled(false);

        fragmentChanger.changeHeader(getResources().getString(R.string.menu_history));

        fragmentChanger.showBackButton(true);
    }

}
