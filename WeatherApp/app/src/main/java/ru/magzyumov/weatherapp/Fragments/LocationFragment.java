package ru.magzyumov.weatherapp.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import ru.magzyumov.weatherapp.BaseActivity;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.R;

public class LocationFragment extends Fragment implements Constants, SearchView.OnQueryTextListener {
    //Объявляем переменные
    private SearchView searchView;
    private BaseActivity baseActivity;
    private FragmentChanger fragmentChanger;

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
    public void onDetach() {
        super.onDetach();
        fragmentChanger = null;
        baseActivity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        //Инициализируем строку поиска
        searchView = view.findViewById(R.id.searchViewCity);
        searchView.setOnQueryTextListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Меняем текст в шапке
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.menu_location);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.menu_location);

        //Показываем кнопку назад
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        baseActivity.setStringPreference(LOCATION, CURRENT_CITY, query);
        fragmentChanger.changeFragment("mainFragment", null, false);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) { return false; }
}
