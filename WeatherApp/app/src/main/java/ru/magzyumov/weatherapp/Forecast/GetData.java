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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Forecast.Model.CurrentForecastModel;
import ru.magzyumov.weatherapp.Forecast.Model.DailyForecastModel;
import ru.magzyumov.weatherapp.MainActivity;
import ru.magzyumov.weatherapp.R;

import static ru.magzyumov.weatherapp.BuildConfig.WEATHER_API_KEY;

public class GetData implements Constants {

    private CurrentForecastParcel currentForecastParcel;
    private DailyForecastParcel dailyForecastParcel;
    private MainActivity mainActivity;
    private Context context;

    public GetData(CurrentForecastParcel currentForecastParcel, DailyForecastParcel dailyForecastParcel, Context context, MainActivity mainActivity){
        this.currentForecastParcel = currentForecastParcel;
        this.dailyForecastParcel = dailyForecastParcel;
        this.mainActivity = mainActivity;
        this.context = context;
    }

    public void build(){
        try {
            final URL currUri = new URL(CURR_WEATHER_URL + WEATHER_API_KEY);
            final URL dailyUri = new URL(DAILY_WEATHER_URL + WEATHER_API_KEY);
            final Handler handler = new Handler(); // Запоминаем основной поток
            new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                public void run() {
                    HttpsURLConnection urlConnection = null;
                    try {
                        String currResult = makeRequest(currUri, urlConnection);
                        String dailyResult = makeRequest(dailyUri, urlConnection);
                        // преобразование данных запроса в модель
                        Gson gson = new Gson();
                        final CurrentForecastModel cwRequest = gson.fromJson(currResult, CurrentForecastModel.class);
                        final DailyForecastModel dwRequest = gson.fromJson(dailyResult, DailyForecastModel.class);
                        // Возвращаемся к основному потоку
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                makeWeather(cwRequest, dwRequest);
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "Fail connection", e);
                        e.printStackTrace();
                    } finally {
                        if (null != urlConnection) {
                            urlConnection.disconnect();
                        }
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            Log.e(TAG, "Fail URI", e);
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void makeWeather(CurrentForecastModel cwRequest, DailyForecastModel dwRequest){
        currentForecastParcel.setCity(cwRequest.getName());                      // Забираем название года из прогноза
        currentForecastParcel.setDistrict(cwRequest.getName());                  // Пока район забираем так же
        currentForecastParcel.setTemp(cwRequest.getMain().getTemp());            // Забираем температуру
        currentForecastParcel.setTempEu("Te");                                        // Надо подумать как забрать
        currentForecastParcel.setImage(cwRequest.getWeather()[0].getIcon());     // Забираем иконку с сервера
        currentForecastParcel.setWeather(cwRequest.getWeather()[0].getMain());   // Состояние погоды
        currentForecastParcel.setFeeling(cwRequest.getMain().getFeels_like());   // Ощущения
        currentForecastParcel.setFeelingEu("Te");                                     // Надо подумать как забрать
        currentForecastParcel.setWindSpeed(cwRequest.getWind().getSpeed());      // Скорость ветра
        currentForecastParcel.setWindSpeedEu("Te");                                   // Надо подумать как забрать
        currentForecastParcel.setPressure(cwRequest.getMain().getPressure());          // Давление
        currentForecastParcel.setPressureEu("Te");                                    // Надо подумать как забрать
        currentForecastParcel.setHumidity(cwRequest.getMain().getHumidity());    // Влажность
        currentForecastParcel.setHumidityEu("Te");                                    // Надо подумать как забрать

        dailyForecastParcel.setList(dwRequest.getList());
        mainActivity.setDailyForecastParcel(dailyForecastParcel);
        mainActivity.setCurrentForecastParcel(currentForecastParcel);
        mainActivity.sendParcel(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String makeRequest(URL uri, HttpsURLConnection urlConnection) throws IOException {
        urlConnection = (HttpsURLConnection) uri.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(REQUEST_TIMEOUT);
        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String result = getLines(in);
        return result;
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
