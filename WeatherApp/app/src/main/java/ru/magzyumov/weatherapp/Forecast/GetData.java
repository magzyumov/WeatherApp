package ru.magzyumov.weatherapp.Forecast;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;
import ru.magzyumov.weatherapp.BuildConfig;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Forecast.Model.CurrentForecastModel;
import ru.magzyumov.weatherapp.Forecast.Model.DailyForecastModel;
import ru.magzyumov.weatherapp.MainActivity;
import ru.magzyumov.weatherapp.R;

public class GetData implements Constants {

    private CurrentForecastParcel currentForecastParcel;
    private DailyForecastParcel dailyForecastParcel;
    private MainActivity mainActivity;
    private Context context;
    private View view;

    public GetData(CurrentForecastParcel currentForecastParcel, DailyForecastParcel dailyForecastParcel, View view, MainActivity mainActivity){
        //this.currentForecastParcel = currentForecastParcel;
        //this.dailyForecastParcel = dailyForecastParcel;
        this.currentForecastParcel = new CurrentForecastParcel();
        this.dailyForecastParcel = new DailyForecastParcel();
        this.mainActivity = mainActivity;
        this.view = view;
    }

    public CurrentForecastParcel getCurrentForecast (){
        return this.currentForecastParcel;
    }

    public DailyForecastParcel getDailyForecast (){
        return this.dailyForecastParcel;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void buildCurrent(){
        try {
            final Handler handler = new Handler();// Запоминаем основной поток
            final URL uri = new URL(CURR_WEATHER_URL + BuildConfig.WEATHER_API_KEY);
            Thread receiveForecastThread = new Thread(()-> forecastThread(handler, uri, true));
            receiveForecastThread.start();
        } catch (MalformedURLException e) {
            Log.e(TAG, "Fail start current thread", e);
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void buildDaily(){
        try {
            final Handler handler = new Handler();// Запоминаем основной поток
            final URL uri = new URL(DAILY_WEATHER_URL + BuildConfig.WEATHER_API_KEY);
            Thread receiveForecastThread = new Thread(()-> forecastThread(handler, uri,false));
            receiveForecastThread.start();
        } catch (MalformedURLException e) {
            showNoticeToast(e.getMessage());
            Log.e(TAG, "Fail start daily thread", e);
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void forecastThread(Handler handler, URL uri, boolean current){
        HttpsURLConnection urlConnection = null;
        try {
            urlConnection = (HttpsURLConnection) uri.openConnection();
            urlConnection.setRequestMethod("GET"); // установка метода получения данных -GET
            urlConnection.setReadTimeout(REQUEST_TIMEOUT); // установка таймаута - 10 000 миллисекунд
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток
            String result = getLines(in);
            // преобразование данных запроса в модель
            Gson gson = new Gson();
            if (current){
                final CurrentForecastModel weatherRequest = gson.fromJson(result, CurrentForecastModel.class);
                // Возвращаемся к основному потоку
                handler.post(()-> makeWeather(weatherRequest));
            } else {
                final DailyForecastModel weatherRequest = gson.fromJson(result, DailyForecastModel.class);
                // Возвращаемся к основному потоку
                handler.post(()->makeWeather(weatherRequest));
            }
        } catch (Exception e) {
            Log.e(TAG, "Fail connection", e);
            handler.post(()-> showNoticeToast(e.getMessage()));
            e.printStackTrace();
        } finally {
            if (null != urlConnection) {
                urlConnection.disconnect();
            }
        }
    }

    private void makeWeather(CurrentForecastModel weatherRequest){
        currentForecastParcel.setCity(weatherRequest.getName());                      // Забираем название года из прогноза
        currentForecastParcel.setDistrict(weatherRequest.getName());                  // Пока район забираем так же
        currentForecastParcel.setTemp(weatherRequest.getMain().getTemp());            // Забираем температуру
        currentForecastParcel.setTempEu("Te");                                        // Надо подумать как забрать
        currentForecastParcel.setImage(weatherRequest.getWeather()[0].getIcon());     // Забираем иконку с сервера
        currentForecastParcel.setWeather(weatherRequest.getWeather()[0].getMain());   // Состояние погоды
        currentForecastParcel.setFeeling(weatherRequest.getMain().getFeels_like());   // Ощущения
        currentForecastParcel.setFeelingEu("Te");                                     // Надо подумать как забрать
        currentForecastParcel.setWindSpeed(weatherRequest.getWind().getSpeed());      // Скорость ветра
        currentForecastParcel.setWindSpeedEu("Te");                                   // Надо подумать как забрать
        currentForecastParcel.setPressure(weatherRequest.getMain().getPressure());    // Давление
        currentForecastParcel.setPressureEu("Te");                                    // Надо подумать как забрать
        currentForecastParcel.setHumidity(weatherRequest.getMain().getHumidity());    // Влажность
        currentForecastParcel.setHumidityEu("Te");                                    // Надо подумать как забрать
        mainActivity.setCurrentForecastParcel(currentForecastParcel);
    }

    private void makeWeather(DailyForecastModel weatherRequest){
        dailyForecastParcel.setList(weatherRequest.getList());
        mainActivity.setDailyForecastParcel(dailyForecastParcel);
        mainActivity.sendParcel();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showNoticeToast(String message){
        Toast toast = Toast.makeText(context,
                message,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = toast.getView();
        view.setBackgroundResource(R.drawable.border);
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setTextSize(36);
        toast.show();
    }
}
