package ru.magzyumov.weatherapp.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;
import java.util.Map;

import ru.magzyumov.weatherapp.App;
import ru.magzyumov.weatherapp.BaseActivity;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Database.Location.LocationDao;
import ru.magzyumov.weatherapp.Database.Location.LocationDataSource;
import ru.magzyumov.weatherapp.Database.Location.LocationSource;
import ru.magzyumov.weatherapp.Fragments.FragmentChanger;
import ru.magzyumov.weatherapp.R;

public class BottomFragmentDialog extends BottomSheetDialogFragment implements Constants,
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
    private DialogListener dialogListener;
    private AlertDialogWindow alertDialog;


    public static BottomFragmentDialog newInstance() {
        return new BottomFragmentDialog();
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

        // Устанавливаем стиль
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialogStyle);

        // Инициализируем Alert
        alertDialog = new AlertDialogWindow(getContext(), getString(R.string.citySearch),
                getString(R.string.unknownCity), getString(R.string.ok));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialog, container,
                false);

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

        // Строка поиска
        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        searchView.setOnQueryTextFocusChangeListener(this);

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        baseActivity.setStringPreference(LOCATION, CURRENT_CITY, query);
        if(!contains(arrayListCities, query)) alertDialog.show();
        if (dialogListener != null) dialogListener.onDialogSubmit();
        dismiss();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        arrayAdapterCities.getFilter().filter(query);
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus) listView.setAdapter(arrayAdapterCities);
        if(hasFocus) searchView.setQueryHint(getString(R.string.hintSearchCity));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textViewCity = view.findViewById(android.R.id.text1);
        TextView textViewRegion = view.findViewById(android.R.id.text2);

        String city = textViewCity.getText().toString();
        String region = textViewRegion.getText().toString();

        locationSource.setLocationSearched(region, city, true);
        locationSource.setLocationCurrent(region, city, true);

        searchView.setQuery(city,true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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
        arrayListCities = null;
        arrayListHistory = null;
        dialogListener = null;
        alertDialog = null;
    }

    public void setDialogListener(DialogListener dialogListener){
        this.dialogListener = dialogListener;
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
}

