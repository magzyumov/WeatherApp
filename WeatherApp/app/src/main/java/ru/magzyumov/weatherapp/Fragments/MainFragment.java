package ru.magzyumov.weatherapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import ru.magzyumov.weatherapp.App;
import ru.magzyumov.weatherapp.BaseActivity;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Database.Location.LocationDataSource;
import ru.magzyumov.weatherapp.Forecast.Display.DailyForecastSourceBuilder;
import ru.magzyumov.weatherapp.Forecast.Display.DailyForecastDataSource;
import ru.magzyumov.weatherapp.Forecast.Polling.ForecastListener;
import ru.magzyumov.weatherapp.Forecast.Polling.ServerPolling;
import ru.magzyumov.weatherapp.Forecast.Model.CurrentForecastModel;
import ru.magzyumov.weatherapp.Forecast.Model.DailyForecastModel;
import ru.magzyumov.weatherapp.Logic;
import ru.magzyumov.weatherapp.R;
import ru.magzyumov.weatherapp.Forecast.Display.DailyForecastAdapter;
import ru.magzyumov.weatherapp.Database.Location.Location;
import ru.magzyumov.weatherapp.Database.Location.LocationDao;
import ru.magzyumov.weatherapp.Database.Location.LocationSource;

import static org.apache.commons.lang3.StringUtils.capitalize;

public class MainFragment extends Fragment implements Constants, ForecastListener {
    private Logic logic;
    private View view;
    private FragmentChanger fragmentChanger;
    private BaseActivity baseActivity;
    private DailyForecastModel dailyForecastModel;
    private CurrentForecastModel currentForecastModel;
    private LocationDao locationDao;
    private LocationDataSource locationSource;
    private Location currentLocation;
    private ServerPolling serverPolling;
    private Gson gson;

    public MainFragment() {
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
        logic = Logic.getInstance(getResources());
        dailyForecastModel = new DailyForecastModel();
        currentForecastModel = new CurrentForecastModel();
        locationDao = App.getInstance().getLocationDao();
        locationSource = new LocationSource(locationDao);
        gson = new Gson();
        serverPolling = new ServerPolling(getContext());
        // Подписываеся на опросчика погодного сервера
        serverPolling.addListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_main, container, false);

        // Активируем Drawer
        fragmentChanger.setDrawerIndicatorEnabled(true);

        // Пока ждем основных данных,
        // установим background
        setBackground();

        currentLocation = locationSource.getCurrentLocation();

        if (currentLocation != null){
            if(currentLocation.needUpdate){
                serverPolling.initialize();
                serverPolling.build();
            }

            if ((currentLocation.currentForecast != null) & (currentLocation.dailyForecast != null)){
                currentForecastModel = gson.fromJson(currentLocation.currentForecast, CurrentForecastModel.class);
                dailyForecastModel = gson.fromJson(currentLocation.dailyForecast, DailyForecastModel.class);
                initActivity();
            }
        } else {
            serverPolling.initialize();
            serverPolling.build();
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Меняем текст в шапке
        fragmentChanger.changeHeader(getResources().getString(R.string.app_name));
        fragmentChanger.changeSubHeader(getResources().getString(R.string.app_name));

    }

    public void setDailyForecast() {
        // строим источник данных
        DailyForecastDataSource sourceData = new DailyForecastSourceBuilder()
                .setResources(getResources())
                .setContext(baseActivity)
                .setDataFromServer(dailyForecastModel)
                .build();

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
        adapter.setOnItemClickListener(new DailyForecastAdapter.OnItemClickListener() {
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

    public void setCurrentForecast(){

        // Вставляем данные в поля
        TextView currentCity = view.findViewById(R.id.textViewCity);
        currentCity.setText(currentForecastModel.getName());

        TextView currentDistrict = view.findViewById(R.id.textViewDistrict);
        currentDistrict.setText(currentForecastModel.getName());

        TextView textViewCurrent = view.findViewById(R.id.textViewCurrentTemp);
        textViewCurrent.setText(String.valueOf((int)currentForecastModel.getMain().getTemp()));

        ImageView imageViewCurrent = view.findViewById(R.id.imageViewCurrent);
        imageViewCurrent.setImageResource(R.drawable.bkn_d_line_light);

        TextView currWeather = view.findViewById(R.id.textViewCurrentWeather);
        currWeather.setText(capitalize(currentForecastModel.getWeather()[0].getDescription()));

        TextView currFeelingTemp = view.findViewById(R.id.textViewCurrentFeelingTemp);
        currFeelingTemp.setText(String.valueOf((int)currentForecastModel.getMain().getFeels_like()));

        TextView textViewWindSpeed = view.findViewById(R.id.textViewWindSpeed);
        textViewWindSpeed.setText(String.valueOf((int)currentForecastModel.getWind().getSpeed()));

        TextView textViewPressure = view.findViewById(R.id.textViewPressure);
        textViewPressure.setText(String.valueOf((int)(currentForecastModel.getMain().getPressure() * HPA)));

        TextView textViewHumidity = view.findViewById(R.id.textViewHumidity);
        textViewHumidity.setText(String.valueOf(currentForecastModel.getMain().getHumidity()));

        //TODO: Надо подумать как забрать единицы измерения;
        TextView currFeelingTempEu = view.findViewById(R.id.textViewCurrentFeelingTempEu);
        //TODO: currFeelingTempEu.setText(currentForecastParcel.getFeelingEu());

        TextView textViewWindSpeedEU = view.findViewById(R.id.textViewWindSpeedEU);
        //TODO: textViewWindSpeedEU.setText(currentForecastParcel.getWindSpeedEu());

        TextView textViewPressureEU = view.findViewById(R.id.textViewPressureEU);
        //TODO: textViewPressureEU.setText(currentForecastParcel.getPressureEu());

        TextView textViewHumidityEU = view.findViewById(R.id.textViewHumidityEU);
        //TODO: textViewHumidityEU.setText(currentForecastParcel.getHumidityEu());

        TextView textViewCurrentEU = view.findViewById(R.id.textViewCurrentTempEU);
        textViewCurrentEU.setText((baseActivity.getBooleanPreference(SETTING, TEMP_EU)) ? (getString(R.string.fahrenheit)) : (getString(R.string.celsius)));
    }

    public void setBackground(){
        // TODO: Это надо сделать как-то по другому
        logic.refreshData();

        //Ставим background картинку
        FrameLayout mainLayout = view.findViewById(R.id.mainFragment);
        mainLayout.setBackgroundResource(logic.getMainLayerPic());

        FrameLayout secondLayout = view.findViewById(R.id.secondLayer);
        secondLayout.setBackgroundResource(logic.getSecondLayerPic());
    }

    private void initBottomLink(){
        TextView textView = view.findViewById(R.id.textViewProvider);
        textView.setVisibility(View.VISIBLE);
        textView.setText(Html.fromHtml("<a href=" + PROVIDER_URL + "><font color=#AAA>" + getString(R.string.provider) + "</font></a>"));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(PROVIDER_URL));
                startActivity(browser);
            }
        });
    }

    @Override
    public void setCurrentForecastModel(CurrentForecastModel currentForecastModel) {
        this.currentForecastModel = currentForecastModel;
    }

    @Override
    public void setDailyForecastModel(DailyForecastModel dailyForecastModel) {
        this.dailyForecastModel = dailyForecastModel;
    }

    @Override
    public void showMessage(String message) {
        Toast toast = Toast.makeText(getContext(),
                message,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = toast.getView();
        view.setBackgroundResource(R.drawable.border);
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setTextSize(36);
        toast.show();
    }

    @Override
    public void initActivity() {

        // Обновляем данные в верхнней части
        setCurrentForecast();

        // Обновляем прогноз
        setDailyForecast();

        //Иницилизируем кнопку-ссылку
        initBottomLink();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Отписываемся от опросчика погоды
        serverPolling.removeListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentChanger = null;
        baseActivity = null;
        dailyForecastModel = null;
        currentForecastModel = null;
        serverPolling = null;
        locationDao = null;
        locationSource = null;
        gson = null;
    }
}
