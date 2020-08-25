package ru.magzyumov.weatherapp.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.magzyumov.weatherapp.App;
import ru.magzyumov.weatherapp.BaseActivity;
import ru.magzyumov.weatherapp.BuildConfig;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Database.Firebase.PhoneClass;
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
import ru.magzyumov.weatherapp.GeoMapThreads;
import ru.magzyumov.weatherapp.R;
import ru.magzyumov.weatherapp.Database.Location.Locations;
import ru.magzyumov.weatherapp.Database.Location.LocationDao;
import ru.magzyumov.weatherapp.Database.Location.LocationSource;

import static android.content.Context.LOCATION_SERVICE;

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
    private Locations currentLocation;
    private LocationDao locationDao;
    private LocationDataSource locationSource;
    private ServerPolling serverPolling;
    private ResponseParser responseParser;
    private PicassoLoader picassoLoader;
    private FrameLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView hourlyRecyclerView;
    private RecyclerView dailyRecyclerView;
    private AlertDialogWindow alertDialog;
    private Handler handler;
    private GeoMapThreads geoMapThreads;
    private LinearLayout linearLayoutHeaderText;

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

        // Инициализируем поток для определения координат
        geoMapThreads = new GeoMapThreads(GEOMAP);
        handler = new Handler();

        // Подписываеся на опросчика погодного сервера
        serverPolling.addListener(this);

        // Инициализируем Alert
        alertDialog = new AlertDialogWindow(getContext(), getString(R.string.messageFromServer), getString(R.string.ok));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_main, container, false);

        fragmentChanger.setDrawerIndicatorEnabled(true);

        linearLayoutHeaderText = view.findViewById(R.id.linearLayoutHeaderText);
        hourlyRecyclerView = view.findViewById(R.id.hourly_forecast_recycler_view);
        dailyRecyclerView = view.findViewById(R.id.daily_forecast_recycler_view);
        bottomSheet = view.findViewById(R.id.bottom_sheet);

        requestPermissions();

        checkStatus();

        initBottomSheet();

        initBottomLink();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fragmentChanger.changeHeader(getResources().getString(R.string.app_name));
    }

    @Override
    public void setCurrentForecast(CurrentForecast currentForecast) {
        this.currentForecast = currentForecast;
        linearLayoutHeaderText.setVisibility(View.VISIBLE);
        setCurrentForecast();
        setBackground();
    }

    @Override
    public void setForecast(ForecastSource hourlyForecast, ForecastSource dailyForecast) {
        this.dailyForecast = dailyForecast;
        this.hourlyForecast = hourlyForecast;
        bottomSheet.setVisibility(View.VISIBLE);
        setHourlyForecast();
        setDailyForecast();
    }

    @Override
    public void showMessage(String message) {
        alertDialog.show(message);
    }

    @Override
    public void initListener() {
        linearLayoutHeaderText.setVisibility(View.VISIBLE);
        bottomSheet.setVisibility(View.VISIBLE);

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
        linearLayoutHeaderText = null;
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
        currentDistrict.setText(currentForecast.getDistrict());

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

        hourlyRecyclerView.setVisibility(View.GONE);
        hourlyRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hourlyRecyclerView.setLayoutManager(layoutManager);

        ForecastAdapter adapter = new ForecastAdapter(sourceData);
        hourlyRecyclerView.setAdapter(adapter);

        // Добавим разделитель карточек
        if (BuildConfig.FLAVOR.equals("adminVersion")){
            DividerItemDecoration itemDecoration = new DividerItemDecoration(
                    hourlyRecyclerView.getContext(), layoutManager.getOrientation());
            itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator));
            if(hourlyRecyclerView.getItemDecorationCount() == 0) hourlyRecyclerView.addItemDecoration(itemDecoration);
        }
    }

    private void setDailyForecast() {

        ForecastDataSource sourceData = dailyForecast;

        dailyRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        dailyRecyclerView.setLayoutManager(layoutManager);

        ForecastAdapter adapter = new ForecastAdapter(sourceData);
        dailyRecyclerView.setAdapter(adapter);

        // Добавим разделитель карточек
        if (BuildConfig.FLAVOR.equals("adminVersion")){
            DividerItemDecoration itemDecoration = new DividerItemDecoration(
                    dailyRecyclerView.getContext(), layoutManager.getOrientation());
            itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator));
            if(dailyRecyclerView.getItemDecorationCount() == 0) dailyRecyclerView.addItemDecoration(itemDecoration);
        }
    }

    private void setBackground(){
        //Ставим background картинку
        FrameLayout mainLayout = view.findViewById(R.id.mainFragment);
        mainLayout.setBackgroundResource(currentForecast.getBackImageFirst());

        FrameLayout secondLayout = view.findViewById(R.id.secondLayer);
        secondLayout.setBackgroundResource(currentForecast.getBackImageSecond());
    }

    private void initBottomSheet(){
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

        String windSpeedEU = baseActivity.getBooleanPreference(SETTING, EU) ? (getString(R.string.windEUTwo)) : (getString(R.string.windEUOne));
        TextView textViewWindSpeedEU = view.findViewById(R.id.textViewWindSpeedEUBottom);
        textViewWindSpeedEU.setText(windSpeedEU);

        String pressureEU = baseActivity.getBooleanPreference(SETTING, EU) ? (getString(R.string.pressEUTwo)) : (getString(R.string.pressEUOne));
        TextView textViewPressureEU = view.findViewById(R.id.textViewPressureEUBottom);
        textViewPressureEU.setText(pressureEU);

    }

    private void initBottomLink(){
        TextView textView = view.findViewById(R.id.textViewProvider);
        textView.setVisibility(View.GONE);
        textView.setText(Html.fromHtml("<a href=" + PROVIDER_URL + "><font color=#AAA>" + getString(R.string.provider) + "</font></a>"));
        textView.setOnClickListener(v -> {
            Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(PROVIDER_URL));
            startActivity(browser);
        });
    }

    private void checkStatus(){
        currentLocation = locationSource.getCurrentLocation();

        linearLayoutHeaderText.setVisibility(View.GONE);
        bottomSheet.setVisibility(View.GONE);

        if (currentLocation != null){
            if(currentLocation.needUpdate){
                serverPolling.initialize();
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
            } else if (previousForecastCurrent != null & previousForecastDaily != null){
                // Если данных нет в базе забираем данные с SharedPreference прошлого города
                currentForecast = responseParser.getCurrentForecast(previousForecastCurrent);
                hourlyForecast = responseParser.getHourlyForecast(previousForecastHourly);
                dailyForecast = responseParser.getDailyForecast(previousForecastDaily);
                initListener();
            }
        } else {
            serverPolling.initialize();
        }

    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 10000, 10, locationListener);
        }
    }

    private void requestPermissions() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        } else {
            requestLocationPermissions();
        }
    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    GEO_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GEO_PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                            grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                requestLocation();
            }
        }
    }

    private void writePositionToFirebase(String latitude, String longitude){
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_PATTERN, Locale.getDefault());
        Date date = new Date();

        String id = Settings.Secure.getString(requireContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        String timeStamp = dateFormat.format(date);

        DatabaseReference firebaseDB = FirebaseDatabase.getInstance().getReference();
        PhoneClass phonePosition = new PhoneClass(timeStamp, latitude, longitude);
        firebaseDB.child(PHONES).child(id).child(POSITION).setValue(phonePosition.getPosition());
    }

    private void writePositionToDB(double latitude, double longitude, String location){
        Locations currentLocation = locationSource.getLocationById(0);
        currentLocation.region = location;
        currentLocation.latitude = latitude;
        currentLocation.longitude = longitude;
        locationSource.updateLocation(currentLocation);
    }

    private void getNameCity(LatLng location) {
        if (Geocoder.isPresent()) {
            try {
                Geocoder geocoder = new Geocoder(requireContext());
                List<Address> addresses = geocoder.getFromLocation (location.latitude,
                        location.longitude, 1);
                if(addresses.size()>0) handler.post(() -> writePositionToDB(location.latitude,
                        location.longitude, addresses.get(0).getAddressLine(0)));
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double lat = location.getLatitude();
            String latitude = Double.toString(lat);

            double lng = location.getLongitude();
            String longitude = Double.toString(lng);

            LatLng currentPosition = new LatLng(lat, lng);

            geoMapThreads.postTask(GEOMAP, () -> getNameCity(currentPosition));

            writePositionToFirebase(latitude, longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

}


