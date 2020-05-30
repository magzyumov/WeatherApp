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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import ru.magzyumov.weatherapp.App;
import ru.magzyumov.weatherapp.BaseActivity;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Database.Location.LocationDataSource;
import ru.magzyumov.weatherapp.Dialog.AlertDialogWindow;
import ru.magzyumov.weatherapp.Dialog.DialogListener;
import ru.magzyumov.weatherapp.Dialog.BottomFragmentDialog;
import ru.magzyumov.weatherapp.Forecast.Display.CurrentForecast;
import ru.magzyumov.weatherapp.Forecast.Display.DailyForecastSource;
import ru.magzyumov.weatherapp.Forecast.Display.DailyForecastDataSource;
import ru.magzyumov.weatherapp.Forecast.Display.PicassoLoader;
import ru.magzyumov.weatherapp.Forecast.Polling.ForecastListener;
import ru.magzyumov.weatherapp.Forecast.Polling.ServerPolling;
import ru.magzyumov.weatherapp.Forecast.Display.ResponseParser;
import ru.magzyumov.weatherapp.R;
import ru.magzyumov.weatherapp.Forecast.Display.DailyForecastAdapter;
import ru.magzyumov.weatherapp.Database.Location.Location;
import ru.magzyumov.weatherapp.Database.Location.LocationDao;
import ru.magzyumov.weatherapp.Database.Location.LocationSource;

public class MainFragment extends Fragment implements Constants, ForecastListener {
    private View view;
    private FragmentChanger fragmentChanger;
    private BaseActivity baseActivity;
    private DailyForecastSource dailyForecast;
    private CurrentForecast currentForecast;
    private LocationDao locationDao;
    private LocationDataSource locationSource;
    private Location currentLocation;
    private ServerPolling serverPolling;
    private AlertDialogWindow alertDialog;
    private ResponseParser responseParser;
    private PicassoLoader picassoLoader;

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
        locationDao = App.getInstance().getLocationDao();
        locationSource = new LocationSource(locationDao);
        responseParser = new ResponseParser(getResources());
        serverPolling = new ServerPolling(getContext());
        picassoLoader = new PicassoLoader();

        // Подписываеся на опросчика погодного сервера
        serverPolling.addListener(this);

        // Инициализируем Alert
        alertDialog = new AlertDialogWindow(getContext(), getString(R.string.messageFromServer), getString(R.string.ok));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_main, container, false);

        // Активируем Drawer
        fragmentChanger.setDrawerIndicatorEnabled(true);

        checkStatus();

        //Иницилизируем кнопку-ссылку
        initBottomLink();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Меняем текст в шапке
        fragmentChanger.changeHeader(getResources().getString(R.string.app_name));
        fragmentChanger.changeSubHeader(getResources().getString(R.string.app_name));
    }

    @Override
    public void setCurrentForecast(CurrentForecast currentForecast) {
        this.currentForecast = currentForecast;
        setCurrentForecast();
        setBackground();
    }

    @Override
    public void setDailyForecast(DailyForecastSource dailyForecast) {
        this.dailyForecast = dailyForecast;
        setDailyForecast();
    }

    @Override
    public void showMessage(String message) {
        alertDialog.show(message);
    }

    @Override
    public void initListener() {
        // Обновляем прогноз
        setDailyForecast();

        // Обновляем данные в верхнней части
        setCurrentForecast();

        // Обновляем задний фон
        setBackground();
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
        dailyForecast = null;
        currentForecast = null;
        serverPolling = null;
        locationDao = null;
        locationSource = null;
        alertDialog = null;
        picassoLoader = null;
    }

    private void setCurrentForecast(){

        // Вставляем данные в поля
        TextView currentCity = view.findViewById(R.id.textViewCity);
        currentCity.setText(currentForecast.getCity());

        // Устанавливаем OnClickListener
        currentCity.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomFragmentDialog bottomFragmentDialog = BottomFragmentDialog.newInstance();
                bottomFragmentDialog.setDialogListener(dialogListener);
                bottomFragmentDialog.show(fragmentChanger.getFragmentTransaction(),"dialog_fragment");
            }
        });

        TextView currentDistrict = view.findViewById(R.id.textViewDistrict);
        currentDistrict.setText(currentForecast.getCity());

        TextView textViewCurrent = view.findViewById(R.id.textViewCurrentTemp);
        textViewCurrent.setText(currentForecast.getTemp());

        ImageView imageViewCurrent = view.findViewById(R.id.imageViewCurrent);
        picassoLoader.load(currentForecast.getImage(), imageViewCurrent);

        TextView currWeather = view.findViewById(R.id.textViewCurrentWeather);
        currWeather.setText(currentForecast.getWeather());

        TextView currFeelingTemp = view.findViewById(R.id.textViewCurrentFeelingTemp);
        currFeelingTemp.setText(currentForecast.getFeeling());

        TextView textViewWindSpeed = view.findViewById(R.id.textViewWindSpeed);
        textViewWindSpeed.setText(currentForecast.getWindSpeed());

        TextView textViewPressure = view.findViewById(R.id.textViewPressure);
        textViewPressure.setText(currentForecast.getPressure());

        TextView textViewHumidity = view.findViewById(R.id.textViewHumidity);
        textViewHumidity.setText(currentForecast.getHumidity());

        TextView currFeelingTempEu = view.findViewById(R.id.textViewCurrentFeelingTempEu);
        currFeelingTempEu.setText(currentForecast.getFeelingEu());

        TextView textViewWindSpeedEU = view.findViewById(R.id.textViewWindSpeedEU);
        textViewWindSpeedEU.setText(currentForecast.getWindSpeedEu());

        TextView textViewPressureEU = view.findViewById(R.id.textViewPressureEU);
        textViewPressureEU.setText(currentForecast.getPressureEu());

        TextView textViewHumidityEU = view.findViewById(R.id.textViewHumidityEU);
        textViewHumidityEU.setText(currentForecast.getHumidityEu());

        TextView textViewCurrentEU = view.findViewById(R.id.textViewCurrentTempEU);
        textViewCurrentEU.setText(currentForecast.getTempEu());
    }

    private void setDailyForecast() {

        DailyForecastDataSource sourceData = dailyForecast;

        RecyclerView dailyRecyclerView = view.findViewById(R.id.daily_forecast_recycler_view);
        dailyRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        dailyRecyclerView.setLayoutManager(layoutManager);

        DailyForecastAdapter adapter = new DailyForecastAdapter(sourceData);
        dailyRecyclerView.setAdapter(adapter);

        // Добавим разделитель карточек
        DividerItemDecoration itemDecoration = new DividerItemDecoration( getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator));
        if(dailyRecyclerView.getItemDecorationCount() == 0) dailyRecyclerView.addItemDecoration(itemDecoration);

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

    private void setBackground(){
        //Ставим background картинку
        FrameLayout mainLayout = view.findViewById(R.id.mainFragment);
        mainLayout.setBackgroundResource(currentForecast.getBackImageFirst());

        FrameLayout secondLayout = view.findViewById(R.id.secondLayer);
        secondLayout.setBackgroundResource(currentForecast.getBackImageSecond());
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

    private void checkStatus(){
        currentLocation = locationSource.getCurrentLocation();

        if (currentLocation != null){
            if(currentLocation.needUpdate){
                serverPolling.initialize();
                serverPolling.build();
            }

            // Смотрим есть ли данные в SharedPreference
            String previousForecastCurrent = baseActivity.getStringPreference(FORECAST, CURRENT);
            String previousForecastDaily = baseActivity.getStringPreference(FORECAST, DAILY);

            // Смотрим, есть ли данные по этому городу в базе
            if ((currentLocation.currentForecast != null) & (currentLocation.dailyForecast != null)){
                //currentForecast = responseParser.getCurrentForecast(currentLocation.currentForecast);
                //dailyForecast = responseParser.getDailyForecast(currentLocation.dailyForecast);
                currentForecast = currentLocation.currentForecast;
                dailyForecast = currentLocation.dailyForecast;
                initListener();
            } else if (!previousForecastCurrent.equals("") & !previousForecastDaily.equals("")){
                // Если данных нет в базе забираем данные с SharedPreference прошлого города
                currentForecast = responseParser.getCurrentForecast(previousForecastCurrent);
                dailyForecast = responseParser.getDailyForecast(previousForecastDaily);
                initListener();
            }
        } else {
            serverPolling.initialize();
            serverPolling.build();
        }

    }

    private DialogListener dialogListener = new DialogListener() {
        @Override
        public void onDialogSubmit() {
            checkStatus();
        }
        public void onDialogReject() {
            // Nothing
        }
    };
}


