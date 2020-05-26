package ru.magzyumov.weatherapp.Forecast.Polling;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import ru.magzyumov.weatherapp.App;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Database.Location.LocationDataSource;
import ru.magzyumov.weatherapp.Forecast.Display.CurrentForecast;
import ru.magzyumov.weatherapp.Forecast.Display.DailyForecastSource;
import ru.magzyumov.weatherapp.Forecast.Display.ResponseParser;
import ru.magzyumov.weatherapp.R;
import ru.magzyumov.weatherapp.Database.Location.Location;
import ru.magzyumov.weatherapp.Database.Location.LocationDao;
import ru.magzyumov.weatherapp.Database.Location.LocationSource;

import static java.util.Locale.getDefault;
import static ru.magzyumov.weatherapp.BuildConfig.WEATHER_API_KEY;

public class ServerPolling implements Constants {

    private Context context;
    private String currentCity;
    private String currentLang;
    private SharedPreferences sharedPrefForecast;
    private Resources resources;
    private LocationDao locationDao;
    private LocationDataSource locationSource;
    private Location currentLocation;
    private List<ForecastListener> listeners = new ArrayList<>();
    private ResponseParser responseParser;
    private ConnectionThreads connectionThreads;

    public ServerPolling(Context context){
        this.context = context;
        this.sharedPrefForecast = context.getSharedPreferences(FORECAST, Context.MODE_PRIVATE);
        this.resources = context.getResources();
        this.locationDao = App.getInstance().getLocationDao();
        this.locationSource = new LocationSource(locationDao);
        this.currentLang = getDefault().getLanguage();
        this.responseParser = new ResponseParser(resources);
        if(connectionThreads == null) {
            this.connectionThreads = new ConnectionThreads(CURRENT, DAILY);
        }
    }

    // Метод установки текущего города
    public void initialize() {
        currentLocation = locationSource.getCurrentLocation();
        if(currentLocation != null) currentCity = currentLocation.city;
        if (currentCity == null) currentCity = "moskwa";
        currentLang = getDefault().getLanguage();
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
    private void writeForecastResponseToDB(String currentForecast, CurrentForecast cwResult){
        if(currentLocation != null){
            currentLocation.currentForecast = currentForecast;
            currentLocation.temperature = cwResult.getTempForDb();
            currentLocation.date = cwResult.getDate();
            currentLocation.needUpdate = false;
            locationSource.updateLocation(currentLocation);
        }
        writeForecastResponseToPreference(CURRENT, currentForecast);
    }

    private void writeForecastResponseToDB(String dailyForecast){
        if(currentLocation != null){
            currentLocation.dailyForecast = dailyForecast;
            currentLocation.needUpdate = false;
            locationSource.updateLocation(currentLocation);
        }
        writeForecastResponseToPreference(DAILY, dailyForecast);
    }

    private void writeForecastResponseToPreference(String tag, String data){
        SharedPreferences.Editor editor = sharedPrefForecast.edit();
        editor.putString(tag, data);
        editor.apply();
    }

    public void build(){
        final Handler handler = new Handler(); // Запоминаем основной поток
        connectionThreads.postTask(CURRENT, ()-> currentForecastRequest(handler));
        connectionThreads.postTask(DAILY, ()-> dailyForecastRequest(handler));
    }

    private String makeRequest(URL uri, Handler handler) {
        HttpsURLConnection urlConnection = null;
        String result = null;
        try {
            urlConnection = (HttpsURLConnection) uri.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(REQUEST_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);

            //Если коннекшн в норме
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                result = convertStreamToString(in);
            //Город не найден
            } else if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                handler.post(() -> showMsgToListeners(context.getResources().getString(R.string.cityNotFound)));
            //Не верный API ключ
            } else if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                handler.post(() -> showMsgToListeners(context.getResources().getString(R.string.invalidKey)));
            //Другая ошибка связи
            } else {
                String response = urlConnection.getResponseMessage();
                handler.post(() -> showMsgToListeners(response));
            }
        //Нет интернета
        } catch (SocketTimeoutException e) {
            handler.post(() -> showMsgToListeners(context.getResources().getString(R.string.connectionTimeout)));
        //Другая ошибка
        } catch (Exception e) {
            handler.post(() -> showMsgToListeners(e.getMessage()));
            e.printStackTrace();
        } finally {
            if (null != urlConnection) {
                urlConnection.disconnect();
            }
        }
        return result;
    }

    private String convertStreamToString(BufferedReader in) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = in.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        return stringBuilder.toString();
    }

    private void dailyForecastRequest(Handler handler){
        try{
            String dailyURL = String.format(DAILY_WEATHER_URL, currentCity, currentLang);
            final URL dailyUri = new URL(dailyURL + WEATHER_API_KEY);
            //final Handler handler = new Handler(); // Запоминаем основной поток

            String dailyResult = makeRequest(dailyUri, handler);
            final DailyForecastSource dwRequest = responseParser.getDailyForecast(dailyResult);
            if ( (dwRequest != null) & (dailyResult != null) ) {
                handler.post(() -> writeForecastResponseToDB(dailyResult));
                handler.post(() -> dataReady(dwRequest));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void currentForecastRequest(Handler handler){
        try{
            String currURL = String.format(CURR_WEATHER_URL, currentCity, currentLang);
            final URL currUri = new URL(currURL + WEATHER_API_KEY);
            //final Handler handler = new Handler(); // Запоминаем основной поток

            String currResult = makeRequest(currUri, handler);
            final CurrentForecast cwRequest = responseParser.getCurrentForecast(currResult);
            if ( (cwRequest != null) & (currResult != null) ) {
                handler.post(() -> writeForecastResponseToDB(currResult, cwRequest));
                handler.post(() -> dataReady(cwRequest));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
