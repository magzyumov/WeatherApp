package ru.magzyumov.weatherapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment implements SearchView.OnQueryTextListener {
    //Объявляем переменные
    private SearchView searchView;
    final MainPresenter presenter = MainPresenter.getInstance();

    public LocationFragment() {
        // Required empty public constructor
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
        presenter.setCurrentCity(query);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainLayout, new MainFragment(),"mainFragment")
                .commit();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) { return false; }
}
