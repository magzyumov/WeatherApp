package ru.magzyumov.weatherapp.Forecast.Polling;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;

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

    public ServerPolling(Context context){
        this.context = context;
        this.sharedPrefForecast = context.getSharedPreferences(FORECAST, Context.MODE_PRIVATE);
        this.resources = context.getResources();
        this.locationDao = App.getInstance().getLocationDao();
        this.locationSource = new LocationSource(locationDao);
        this.currentLang = getDefault().getLanguage();
        this.responseParser = new ResponseParser(resources);
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
    public void dataReady(CurrentForecast cwRequest, DailyForecastSource dwRequest){
        for (ForecastListener listener:listeners) {
            listener.setCurrentForecast(cwRequest);
            listener.setDailyForecast(dwRequest);
            listener.initListener();
        }
    }

    // Метод записи прогноза в базу
    private void writeForecastResponseToDB(String currentForecast, String dailyForecast, CurrentForecast cwResult){
        if(currentLocation != null){
            currentLocation.currentForecast = currentForecast;
            currentLocation.dailyForecast = dailyForecast;
            currentLocation.temperature = cwResult.getTempForDb();
            currentLocation.date = cwResult.getDate();
            currentLocation.needUpdate = false;
            locationSource.updateLocation(currentLocation);
        }
        writeForecastResponseToPreference(currentForecast, dailyForecast);
    }

    private void writeForecastResponseToPreference(String current, String daily){
        SharedPreferences.Editor editor = sharedPrefForecast.edit();
        editor.putString(CURRENT, current);
        editor.putString(DAILY, daily);
        editor.apply();
    }

    public void build(){
        try {
            String currURL = String.format(CURR_WEATHER_URL, currentCity, currentLang);
            String dailyURL = String.format(DAILY_WEATHER_URL, currentCity, currentLang);
            final URL currUri = new URL(currURL + WEATHER_API_KEY);
            final URL dailyUri = new URL(dailyURL + WEATHER_API_KEY);
            final Handler handler = new Handler(); // Запоминаем основной поток
            new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                public void run() {
                    String currResult = makeRequest(currUri, handler);
                    String dailyResult = makeRequest(dailyUri, handler);
                    // преобразование данных запроса в модель
                    final CurrentForecast cwRequest = responseParser.getCurrentForecast(currResult);
                    final DailyForecastSource dwRequest = responseParser.getDailyForecast(dailyResult);
                    // Возвращаемся к основному потоку
                    if (((cwRequest != null) & (dwRequest != null)) & ((currResult != null)&(dailyResult != null)) ) {
                        handler.post(() -> writeForecastResponseToDB(currResult, dailyResult, cwRequest));
                        handler.post(() -> dataReady(cwRequest, dwRequest));
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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
}
