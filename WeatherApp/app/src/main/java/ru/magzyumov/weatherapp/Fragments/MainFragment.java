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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import ru.magzyumov.weatherapp.App;
import ru.magzyumov.weatherapp.BaseActivity;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Database.Location.LocationDataSource;
import ru.magzyumov.weatherapp.Dialog.AlertDialogWindow;
import ru.magzyumov.weatherapp.Dialog.DialogListener;
import ru.magzyumov.weatherapp.Dialog.BottomFragmentDialog;
import ru.magzyumov.weatherapp.Forecast.Display.CurrentForecast;
import ru.magzyumov.weatherapp.Forecast.Display.ForecastAdapter;
import ru.magzyumov.weatherapp.Forecast.Display.ForecastDataSource;
import ru.magzyumov.weatherapp.Forecast.Display.ForecastSource;
import ru.magzyumov.weatherapp.Forecast.Display.PicassoLoader;
import ru.magzyumov.weatherapp.Forecast.Polling.ForecastListener;
import ru.magzyumov.weatherapp.Forecast.Polling.ServerPolling;
import ru.magzyumov.weatherapp.Forecast.Display.ResponseParser;
import ru.magzyumov.weatherapp.R;
import ru.magzyumov.weatherapp.Database.Location.Location;
import ru.magzyumov.weatherapp.Database.Location.LocationDao;
import ru.magzyumov.weatherapp.Database.Location.LocationSource;

public class MainFragment extends Fragment implements Constants, ForecastListener {
    private FragmentChanger fragmentChanger;
    private BaseActivity baseActivity;
    private View view;
    private ImageView bottomSheetArrow;
    private TextView textViewByDays;
    private TextView textViewByHours;
    private CurrentForecast currentForecast;
    private ForecastSource dailyForecast;
    private ForecastSource hourlyForecast;
    private Location currentLocation;
    private LocationDao locationDao;
    private LocationDataSource locationSource;
    private ServerPolling serverPolling;
    private ResponseParser responseParser;
    private PicassoLoader picassoLoader;
    private LinearLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView hourlyRecyclerView;
    private RecyclerView dailyRecyclerView;
    private AlertDialogWindow alertDialog;

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

        fragmentChanger.setDrawerIndicatorEnabled(true);

        checkStatus();

        initBottomSheet();

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
    public void setForecast(ForecastSource hourlyForecast, ForecastSource dailyForecast) {
        this.dailyForecast = dailyForecast;
        this.hourlyForecast = hourlyForecast;
        setHourlyForecast();
        setDailyForecast();
    }

    @Override
    public void showMessage(String message) {
        alertDialog.show(message);
    }

    @Override
    public void initListener() {
        setCurrentForecast();

        setHourlyForecast();

        setDailyForecast();

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
        view = null;
        fragmentChanger = null;
        baseActivity = null;
        dailyForecast = null;
        hourlyForecast = null;
        currentForecast = null;
        serverPolling = null;
        locationDao = null;
        locationSource = null;
        alertDialog = null;
        picassoLoader = null;
        bottomSheetArrow = null;
        textViewByDays = null;
        textViewByHours = null;
        currentLocation = null;
        picassoLoader = null;
        bottomSheet = null;
        bottomSheetBehavior = null;
        hourlyRecyclerView = null;
        dailyRecyclerView = null;
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

    private void setHourlyForecast() {

        ForecastDataSource sourceData = hourlyForecast;

        hourlyRecyclerView = view.findViewById(R.id.hourly_forecast_recycler_view);
        hourlyRecyclerView.setVisibility(View.GONE);
        hourlyRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hourlyRecyclerView.setLayoutManager(layoutManager);

        ForecastAdapter adapter = new ForecastAdapter(sourceData);
        hourlyRecyclerView.setAdapter(adapter);

        // Добавим разделитель карточек
        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                hourlyRecyclerView.getContext(), layoutManager.getOrientation());
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator));
        if(hourlyRecyclerView.getItemDecorationCount() == 0) hourlyRecyclerView.addItemDecoration(itemDecoration);
    }

    private void setDailyForecast() {

        ForecastDataSource sourceData = dailyForecast;

        dailyRecyclerView = view.findViewById(R.id.daily_forecast_recycler_view);
        dailyRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        dailyRecyclerView.setLayoutManager(layoutManager);

        ForecastAdapter adapter = new ForecastAdapter(sourceData);
        dailyRecyclerView.setAdapter(adapter);

        // Добавим разделитель карточек

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                dailyRecyclerView.getContext(), layoutManager.getOrientation());
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator));
        if(dailyRecyclerView.getItemDecorationCount() == 0) dailyRecyclerView.addItemDecoration(itemDecoration);
    }

    private void setBackground(){
        //Ставим background картинку
        FrameLayout mainLayout = view.findViewById(R.id.mainFragment);
        mainLayout.setBackgroundResource(currentForecast.getBackImageFirst());

        FrameLayout secondLayout = view.findViewById(R.id.secondLayer);
        secondLayout.setBackgroundResource(currentForecast.getBackImageSecond());
    }

    private void initBottomSheet(){
        bottomSheet = view.findViewById(R.id.bottom_sheet);

        bottomSheetArrow = view.findViewById(R.id.imageViewDetailedData);
        bottomSheetArrow.setOnClickListener(bottomSheetArrowClickListener);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback);

        textViewByHours = view.findViewById(R.id.textViewByHours);
        textViewByHours.setOnClickListener(textViewByHoursClickListener);

        textViewByDays = view.findViewById(R.id.textViewByDays);
        textViewByDays.setOnClickListener(textViewByDaysClickListener);

        textViewByDays.setTextColor(getResources().getColor(R.color.colorBlack));
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
            String previousForecastHourly = baseActivity.getStringPreference(FORECAST, HOURLY);

            // Смотрим, есть ли данные по этому городу в базе
            if ((currentLocation.currentForecast != null) & (currentLocation.dailyForecast != null)){
                currentForecast = currentLocation.currentForecast;
                hourlyForecast = currentLocation.hourlyForecast;
                dailyForecast = currentLocation.dailyForecast;
                initListener();
            } else if (!previousForecastCurrent.equals("") & !previousForecastDaily.equals("")){
                // Если данных нет в базе забираем данные с SharedPreference прошлого города
                currentForecast = responseParser.getCurrentForecast(previousForecastCurrent);
                hourlyForecast = responseParser.getHourlyForecast(previousForecastHourly);
                dailyForecast = responseParser.getDailyForecast(previousForecastDaily);
                initListener();
            }
        } else {
            serverPolling.initialize();
            serverPolling.build();
        }

    }

    private DialogListener dialogListener =
            new DialogListener() {
        @Override
        public void onDialogSubmit() {
            checkStatus();
        }
        public void onDialogReject() {
            // Nothing
        }
    };

    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback =
            new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if ((newState == BottomSheetBehavior.STATE_DRAGGING)
                    || (newState == BottomSheetBehavior.STATE_EXPANDED)) {
                bottomSheetArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_36dp);
            } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_36dp);
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    private View.OnClickListener bottomSheetArrowClickListener =
            new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
    };

    private TextView.OnClickListener textViewByHoursClickListener =
            new TextView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textViewByHours.setTextColor(getResources().getColor(R.color.colorBlack));
                    textViewByDays.setTextColor(getResources().getColor(R.color.colorWhite));
                    hourlyRecyclerView.setVisibility(View.VISIBLE);
                    dailyRecyclerView.setVisibility(View.GONE);
                }
            };

    private TextView.OnClickListener textViewByDaysClickListener =
            new TextView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textViewByDays.setTextColor(getResources().getColor(R.color.colorBlack));
                    textViewByHours.setTextColor(getResources().getColor(R.color.colorWhite));
                    hourlyRecyclerView.setVisibility(View.GONE);
                    dailyRecyclerView.setVisibility(View.VISIBLE);
                }
            };
}


