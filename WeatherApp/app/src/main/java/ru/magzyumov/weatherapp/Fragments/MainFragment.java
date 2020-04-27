package ru.magzyumov.weatherapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.magzyumov.weatherapp.Forecast.Daily.DailyForecastSource;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Forecast.Daily.DailyForecastSourceBuilder;
import ru.magzyumov.weatherapp.Forecast.Daily.DailyForecastDataSource;
import ru.magzyumov.weatherapp.Forecast.Hourly.HourlyForecastDataSource;
import ru.magzyumov.weatherapp.Forecast.Hourly.HourlyForecastSource;
import ru.magzyumov.weatherapp.Forecast.Hourly.HourlyForecastSourceBuilder;
import ru.magzyumov.weatherapp.Logic;
import ru.magzyumov.weatherapp.MainPresenter;
import ru.magzyumov.weatherapp.R;
import ru.magzyumov.weatherapp.Forecast.Daily.DailyForecastAdapter;
import ru.magzyumov.weatherapp.Forecast.Hourly.HourlyForecastAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements Constants {
    private View view;
    private FragmentChanger fragmentChanger;
    final MainPresenter presenter = MainPresenter.getInstance();
    final Logic logic = Logic.getInstance();

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof FragmentChanger) fragmentChanger = (FragmentChanger) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentChanger = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        //Иницилизируем кнопку-ссылку
        initBottomLink();

        initDailyForecast();
        initHourlyForecast();

        //Обновляем данные
        logic.refreshData();
        makeHeaderTable();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Меняем текст в шапке
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.app_name);

        //Скрываем кнопку назад
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void initDailyForecast() {
        // строим источник данных
        DailyForecastDataSource sourceData = new DailyForecastSourceBuilder().setResources(getResources()).build();

        RecyclerView dailyRecyclerView = view.findViewById(R.id.daily_forecast_recycler_view);
        dailyRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        dailyRecyclerView.setLayoutManager(layoutManager);

        DailyForecastAdapter adapter = new DailyForecastAdapter(sourceData);
        dailyRecyclerView.setAdapter(adapter);

        // Добавим разделитель карточек
        DividerItemDecoration itemDecoration = new DividerItemDecoration( getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator));
        dailyRecyclerView.addItemDecoration(itemDecoration);

        //Установка слушателя
        adapter.SetOnItemClickListener(new DailyForecastAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), String.format("Данные за %s недоступны!", ((TextView)view.findViewById(R.id.textViewDate)).getText()),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initHourlyForecast() {

        // строим источник данных
        HourlyForecastDataSource sourceData = new HourlyForecastSourceBuilder().setResources(getResources()).build();

        RecyclerView hourlyRecyclerView = view.findViewById(R.id.hourly_forecast_recycler_view);
        hourlyRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
        hourlyRecyclerView.setLayoutManager(layoutManager);

        HourlyForecastAdapter adapter = new HourlyForecastAdapter(sourceData);
        hourlyRecyclerView.setAdapter(adapter);
    }

    private void makeHeaderTable(){
        String tempEU;
        TextView textViewCurrent = view.findViewById(R.id.textViewCurrent);
        textViewCurrent.setText(((Integer)presenter.getCurrentTemp()).toString());

        tempEU = (presenter.getSwitch(MainPresenter.Field.SETTING_TEMP_EU)) ? (getString(R.string.celsius)) : (getString(R.string.fahrenheit));
        TextView textViewCurrentEU = view.findViewById(R.id.textViewCurrentEU);
        textViewCurrentEU.setText(tempEU);

        ImageView imageViewCurrent = view.findViewById(R.id.imageViewCurrent);
        imageViewCurrent.setImageResource(R.drawable.bkn_d_line_light);

        getCurrCity(presenter.getCurrentCity());

        //Ставим background картинку
        LinearLayout linearLayout = view.findViewById(R.id.linearLayoutPic);
        linearLayout.setBackgroundResource(getResources().
                getIdentifier(logic.getBackgroundPicName(),"drawable", getActivity().
                        getApplicationContext().getPackageName()));

        TextView textViewWindSpeed = view.findViewById(R.id.textViewWindSpeed);
        TextView textViewPressure = view.findViewById(R.id.textViewPressure);
        TextView textViewHumidity = view.findViewById(R.id.textViewHumidity);

        textViewWindSpeed.setText("7 м/с");
        textViewPressure.setText("748 мм.рт.ст");
        textViewHumidity.setText("10 %");
    }

    private void getCurrCity(String city){
        String cityStr = (city==null)?(getResources().getString(R.string.currentCityName)):city;
        TextView currCity = view.findViewById(R.id.textViewCurrentCity);
        currCity.setText(cityStr);
    }

    private void initBottomLink(){
        TextView textView = view.findViewById(R.id.textViewProvider);
        textView.setText(Html.fromHtml("<a href=" + PROVIDER_URL + "><font color=#AAA>" + getString(R.string.provider) + "</font></a>"));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(PROVIDER_URL));
                startActivity(browser);
            }
        });
    }
}
