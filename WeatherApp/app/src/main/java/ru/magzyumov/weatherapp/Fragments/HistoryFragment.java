package ru.magzyumov.weatherapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.magzyumov.weatherapp.App;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.R;
import ru.magzyumov.weatherapp.room.database.Location.LocationDao;
import ru.magzyumov.weatherapp.room.database.Location.LocationRecyclerAdapter;
import ru.magzyumov.weatherapp.room.database.Location.LocationSource;

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
        // Инициализируем объект для обращения к базе
        locationDao = App.getInstance().getLocationDao();
        locationSource = new LocationSource(locationDao);

        // Элемент для адаптера
        linearLayoutManager = new LinearLayoutManager(getContext());
        locationRecyclerAdapter = new LocationRecyclerAdapter(locationSource, this);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Меняем текст в шапке
        fragmentChanger.changeHeader(getResources().getString(R.string.menu_history));
        fragmentChanger.changeSubHeader(getResources().getString(R.string.menu_history));

        //Показываем кнопку назад
        fragmentChanger.showBackButton(true);
    }
}
