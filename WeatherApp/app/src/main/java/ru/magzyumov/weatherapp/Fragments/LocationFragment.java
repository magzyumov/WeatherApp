package ru.magzyumov.weatherapp.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import ru.magzyumov.weatherapp.BaseActivity;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.R;

public class LocationFragment extends Fragment implements Constants, SearchView.OnQueryTextListener {
    //Объявляем переменные
    private SearchView searchView;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> arrayList;
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

        //Инициализируем историю поиска
        listView = view.findViewById(R.id.listViewCity);
        arrayList = new ArrayList<>(baseActivity.getStringSetPreference(LOCATION, SEARCH_HISTORY));
        arrayAdapter = new ArrayAdapter<String>(baseActivity, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Меняем текст в шапке
        fragmentChanger.changeHeader(getResources().getString(R.string.menu_location));
        fragmentChanger.changeSubHeader(getResources().getString(R.string.menu_location));

        //Показываем кнопку назад
        fragmentChanger.showBackButton(true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        baseActivity.setStringPreference(LOCATION, CURRENT_CITY, query);
        baseActivity.setStringSetPreference(LOCATION, SEARCH_HISTORY, query);
        fragmentChanger.returnFragment();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        arrayAdapter.getFilter().filter(query);
        return false;
    }
}
