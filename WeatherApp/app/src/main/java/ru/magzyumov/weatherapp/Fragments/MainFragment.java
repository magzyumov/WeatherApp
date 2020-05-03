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
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import ru.magzyumov.weatherapp.BaseActivity;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Forecast.CurrentForecast;
import ru.magzyumov.weatherapp.Forecast.DailyForecastSourceBuilder;
import ru.magzyumov.weatherapp.Forecast.DailyForecastDataSource;
import ru.magzyumov.weatherapp.Logic;
import ru.magzyumov.weatherapp.R;
import ru.magzyumov.weatherapp.Forecast.DailyForecastAdapter;

public class MainFragment extends Fragment implements Constants {
    private View view;
    Logic logic;
    private CurrentForecast currentForecast;
    private FragmentChanger fragmentChanger;
    private BaseActivity baseActivity;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof FragmentChanger) fragmentChanger = (FragmentChanger) context;
        if(context instanceof BaseActivity) baseActivity = (BaseActivity) context;
        logic = Logic.getInstance(getResources());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentChanger = null;
        baseActivity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        //Иницилизируем кнопку-ссылку
        initBottomLink();

        //Забираем посылку с погодой
        currentForecast = getArguments().getParcelable("currentForecast");

        initDailyForecast();

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
        DailyForecastDataSource sourceData = new DailyForecastSourceBuilder().setResources(getResources()).setContext(baseActivity).build();

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
                Snackbar.make(view, String.format("Данные за %s недоступны!", ((TextView)view.findViewById(R.id.textViewDate)).getText()),Snackbar.LENGTH_LONG)
                        .setAction( "OK" , new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast. makeText (getContext(), "Кнопка в Snackbar нажата" ,
                                        Toast.LENGTH_LONG).show();
                            }
                        }).show();
            }

        });
    }

    private void makeHeaderTable(){
        TextView currentCity = view.findViewById(R.id.textViewCity);
        currentCity.setText(currentForecast.getCity());

        TextView currentDistrict = view.findViewById(R.id.textViewDistrict);
        currentDistrict.setText(currentForecast.getDistrict());

        TextView textViewCurrent = view.findViewById(R.id.textViewCurrentTemp);
        textViewCurrent.setText(currentForecast.getTemp().toString());

        ImageView imageViewCurrent = view.findViewById(R.id.imageViewCurrent);
        imageViewCurrent.setImageResource(R.drawable.bkn_d_line_light);

        TextView currWeather = view.findViewById(R.id.textViewCurrentWeather);
        currWeather.setText(currentForecast.getWeather());

        TextView currFeelingTemp = view.findViewById(R.id.textViewCurrentFeelingTemp);
        currFeelingTemp.setText(currentForecast.getFeeling().toString());

        TextView currFeelingTempEu = view.findViewById(R.id.textViewCurrentFeelingTempEu);
        currFeelingTempEu.setText(currentForecast.getFeelingEu());

        TextView textViewWindSpeed = view.findViewById(R.id.textViewWindSpeed);
        textViewWindSpeed.setText(currentForecast.getWindSpeed());
        TextView textViewWindSpeedEU = view.findViewById(R.id.textViewWindSpeedEU);
        textViewWindSpeedEU.setText(currentForecast.getWindSpeedEu());

        TextView textViewPressure = view.findViewById(R.id.textViewPressure);
        textViewPressure.setText(currentForecast.getPressure());
        TextView textViewPressureEU = view.findViewById(R.id.textViewPressureEU);
        textViewPressureEU.setText(currentForecast.getPressureEu());

        TextView textViewHumidity = view.findViewById(R.id.textViewHumidity);
        textViewHumidity.setText(currentForecast.getHumidity());
        TextView textViewHumidityEU = view.findViewById(R.id.textViewHumidityEU);
        textViewHumidityEU.setText(currentForecast.getHumidityEu());

        //Ставим background картинку
        FrameLayout mainLayout = view.findViewById(R.id.mainFragment);
        mainLayout.setBackgroundResource(logic.getMainLayerPic());

        FrameLayout secondLayout = view.findViewById(R.id.secondLayer);
        secondLayout.setBackgroundResource(logic.getSecondLayerPic());

        TextView textViewCurrentEU = view.findViewById(R.id.textViewCurrentTempEU);
        textViewCurrentEU.setText((baseActivity.getPreference(SETTING, TEMP_EU)) ? (getString(R.string.celsius)) : (getString(R.string.fahrenheit)));
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
