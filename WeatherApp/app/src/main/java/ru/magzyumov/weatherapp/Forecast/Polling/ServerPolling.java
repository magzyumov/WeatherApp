package ru.magzyumov.weatherapp.Forecast.Polling;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import android.os.Handler;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ru.magzyumov.weatherapp.App;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Database.Location.LocationDataSource;
import ru.magzyumov.weatherapp.Forecast.Display.CurrentForecast;
import ru.magzyumov.weatherapp.Forecast.Display.DailyForecastSource;
import ru.magzyumov.weatherapp.Forecast.Display.ResponseParser;
import ru.magzyumov.weatherapp.Forecast.Model.CurrentForecastModel;
import ru.magzyumov.weatherapp.Forecast.Model.DailyForecastModel;
import ru.magzyumov.weatherapp.Database.Location.Location;
import ru.magzyumov.weatherapp.Database.Location.LocationDao;
import ru.magzyumov.weatherapp.Database.Location.LocationSource;
import ru.magzyumov.weatherapp.Forecast.Model.OneCallModel;

import static java.util.Locale.getDefault;

public class ServerPolling implements Constants {

    private Context context;
    private String currentCity;
    private String currentEU;
    private SharedPreferences sharedPrefForecast;
    private SharedPreferences sharedPrefSettings;
    private Resources resources;
    private LocationDao locationDao;
    private LocationDataSource locationSource;
    private Location currentLocation;
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

    // Метод установки текущего города
    public void initialize() {
        currentLocation = locationSource.getCurrentLocation();
        if(currentLocation != null) currentCity = currentLocation.city;
        if (currentCity == null) currentCity = "moskwa";
        currentEU = getDefault().getLanguage();
    }

    public void build(){
        final Handler handler = new Handler();
        currentEU = sharedPrefSettings.getBoolean(EU,false) ? "imperial"  : "metric";
        retrofitClass.getCurrentRequest(currentCity, currentEU, handler);
        retrofitClass.getDailyRequest(currentCity, currentEU, handler);
    }

    // Метод добавления подписчиков на события
    public void addListener(ForecastListener listener) {
        this.listeners.add(listener);
    }

    // Метод удаления подписчиков
    public void removeListener(ForecastListener listener) {
        this.listeners.remove(listener);
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

    public void dataReady(DailyForecastSource dwRequest){
        for (ForecastListener listener:listeners) {
            listener.setDailyForecast(dwRequest);
        }
    }

    // Метод записи прогноза в базу
    public void writeForecastResponseToDB(DailyForecastSource dailyForecast){
        if(currentLocation != null){
            currentLocation.dailyForecast = dailyForecast;
            currentLocation.needUpdate = false;
            locationSource.updateLocation(currentLocation);
        }
        writeForecastResponseToPreference(DAILY, new Gson().toJson(dailyForecast));
    }

    public void writeForecastResponseToDB(CurrentForecast currentForecast){
        if(currentLocation != null){
            currentLocation.currentForecast = currentForecast;
            currentLocation.needUpdate = false;
            locationSource.updateLocation(currentLocation);
        }
        writeForecastResponseToPreference(DAILY, new Gson().toJson(currentForecast));
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

    public void responsePars(DailyForecastModel dailyForecastModel){
        dataReady(responseParser.getDailyForecast(dailyForecastModel));
        writeForecastResponseToDB(responseParser.getDailyForecast(dailyForecastModel));
    }

    public void responsePars(OneCallModel oneCallModel){
        //Nothing
    }

    public Resources getResources(){
        return this.resources;
    }
}
