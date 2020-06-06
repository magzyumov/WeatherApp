package ru.magzyumov.weatherapp.Forecast.Polling;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.magzyumov.weatherapp.App;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Database.Location.LocationDataSource;
import ru.magzyumov.weatherapp.Forecast.Display.CurrentForecast;
import ru.magzyumov.weatherapp.Forecast.Display.ForecastSource;
import ru.magzyumov.weatherapp.Forecast.Display.ResponseParser;
import ru.magzyumov.weatherapp.Forecast.Model.CurrentForecastModel;
import ru.magzyumov.weatherapp.Database.Location.Locations;
import ru.magzyumov.weatherapp.Database.Location.LocationDao;
import ru.magzyumov.weatherapp.Database.Location.LocationSource;
import ru.magzyumov.weatherapp.Forecast.Model.OneCallModel;
import ru.magzyumov.weatherapp.R;

import static java.util.Locale.getDefault;

public class ServerPolling implements Constants {

    private Context context;
    private String currentCity;
    private String currentEU;
    private LatLng currentCoordinate;
    private SharedPreferences sharedPrefForecast;
    private SharedPreferences sharedPrefSettings;
    private Resources resources;
    private LocationDao locationDao;
    private LocationDataSource locationSource;
    private Locations currentLocation;
    private List<ForecastListener> listeners = new ArrayList<>();
    private ResponseParser responseParser;
    private RetrofitClass retrofitClass;

    public ServerPolling(Context context){
        this.context = context;
        this.sharedPrefForecast = context.getSharedPreferences(FORECAST, Context.MODE_PRIVATE);
        this.sharedPrefSettings = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        this.resources = context.getResources();
        this.locationDao = App.getInstance().getLocationDao();
        this.locationSource = new LocationSource(locationDao);
        this.currentEU = getDefault().getLanguage();
        this.responseParser = new ResponseParser(resources);
        this.retrofitClass = new RetrofitClass(this);
    }

    // Метод добавления подписчиков на события
    public void addListener(ForecastListener listener) {
        this.listeners.add(listener);
    }

    // Метод удаления подписчиков
    public void removeListener(ForecastListener listener) {
        this.listeners.remove(listener);
    }

    // Метод инициализации текущего города
    public void initialize() {
        currentLocation = locationSource.getCurrentLocation();
        currentCity = (currentLocation != null) ? currentLocation.city : DEFAULT_CITY;
        currentEU = getDefault().getLanguage();
        if (((currentCoordinate = getCoordinateCity(currentCity)) == null)){
            showMsgToListeners(currentCity + " " + getResources().getString(R.string.cityNotFound));
            currentCoordinate = DEFAULT_COORDINATE;
            currentCity = DEFAULT_CITY;
        }
    }

    // Метод построения запросов
    public void build(){
        final Handler handler = new Handler();
        currentEU = sharedPrefSettings.getBoolean(EU,false) ? "imperial"  : "metric";
        retrofitClass.getCurrentRequest(currentCity, currentEU, handler);
        retrofitClass.getOneCallRequest(currentCoordinate.latitude,
                currentCoordinate.longitude,currentEU, handler);
    }

    // Метод отправки сообшений подписчикам
    public void showMsgToListeners(String message){
        for (ForecastListener listener:listeners) {
            listener.showMessage(message);
        }
    }

    // Метод информирования подписчиков о готовности данных
    public void dataReady(CurrentForecast cwRequest){
        for (ForecastListener listener:listeners) {
            listener.setCurrentForecast(cwRequest);
        }
    }

    public void dataReady(ForecastSource hourlyForecast, ForecastSource dailyForecast){
        for (ForecastListener listener:listeners) {
            listener.setForecast(hourlyForecast, dailyForecast);
        }
    }

    // Метод записи прогноза в базу
    public void writeForecastResponseToDB(CurrentForecast currentForecast){
        if(currentLocation != null){
            currentLocation.currentForecast = currentForecast;
            currentLocation.needUpdate = false;
            locationSource.updateLocation(currentLocation);
        }
        writeForecastResponseToPreference(CURRENT, new Gson().toJson(currentForecast));
    }

    public void writeForecastResponseToDB(ForecastSource hourlyForecast, ForecastSource dailyForecast){
        if(currentLocation != null){
            currentLocation.hourlyForecast = hourlyForecast;
            currentLocation.dailyForecast = dailyForecast;
            currentLocation.needUpdate = false;
            locationSource.updateLocation(currentLocation);
        }
        writeForecastResponseToPreference(HOURLY, new Gson().toJson(hourlyForecast));
        writeForecastResponseToPreference(DAILY, new Gson().toJson(dailyForecast));
    }

    private void writeForecastResponseToPreference(String tag, String data){
        SharedPreferences.Editor editor = sharedPrefForecast.edit();
        editor.putString(tag, data);
        editor.apply();
    }

    public void responsePars(CurrentForecastModel currentForecastModel){
        dataReady(responseParser.getCurrentForecast(currentForecastModel));
        writeForecastResponseToDB(responseParser.getCurrentForecast(currentForecastModel));
    }

    public void responsePars(OneCallModel oneCallModel){
        dataReady(responseParser.getHourlyForecast(oneCallModel),
                responseParser.getDailyForecast(oneCallModel));
        writeForecastResponseToDB(responseParser.getHourlyForecast(oneCallModel),
                responseParser.getDailyForecast(oneCallModel));
    }

    public Resources getResources(){
        return this.resources;
    }

    private LatLng getCoordinateCity(String nameCity) {
        LatLng result = null;
        if (Geocoder.isPresent()) {
            try {
                Geocoder gc = new Geocoder(App.getInstance().getApplicationContext());
                List<Address> addresses = gc.getFromLocationName(nameCity, 1);
                for (Address a : addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        result = new LatLng(a.getLatitude(), a.getLongitude());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
