package ru.magzyumov.weatherapp.Forecast;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ru.magzyumov.weatherapp.BaseActivity;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Forecast.Model.CurrentForecastModel;
import ru.magzyumov.weatherapp.Forecast.Model.DailyForecastModel;
import ru.magzyumov.weatherapp.MainActivity;
import ru.magzyumov.weatherapp.R;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static ru.magzyumov.weatherapp.BuildConfig.WEATHER_API_KEY;

public class GetData implements Constants {

    private CurrentForecastParcel currentForecastParcel;
    private DailyForecastParcel dailyForecastParcel;
    private MainActivity mainActivity;
    private Context context;
    private SharedPreferences sharedPref;           // Настройки приложения
    private Resources resources;
    private BaseActivity baseActivity;

    public GetData(CurrentForecastParcel currentForecastParcel, DailyForecastParcel dailyForecastParcel, Context context, MainActivity mainActivity){
        this.currentForecastParcel = currentForecastParcel;
        this.dailyForecastParcel = dailyForecastParcel;
        this.mainActivity = mainActivity;
        this.context = context;
        this.sharedPref = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        this.resources = mainActivity.getResources();
        if (mainActivity instanceof BaseActivity) baseActivity = mainActivity;
    }

    public void build(){
        try {
            final URL currUri = new URL(CURR_WEATHER_URL + WEATHER_API_KEY);
            final URL dailyUri = new URL(DAILY_WEATHER_URL + WEATHER_API_KEY);
            final Handler handler = new Handler(); // Запоминаем основной поток
            new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                public void run() {
                    String currResult = makeRequest(currUri, handler);
                    String dailyResult = makeRequest(dailyUri, handler);
                    baseActivity.setStringPreference(FORECAST, LAST_CURRENT, currResult);
                    baseActivity.setStringPreference(FORECAST, LAST_DAILY, dailyResult);
                    // преобразование данных запроса в модель
                    Gson gson = new Gson();
                    final CurrentForecastModel cwRequest = gson.fromJson(currResult, CurrentForecastModel.class);
                    final DailyForecastModel dwRequest = gson.fromJson(dailyResult, DailyForecastModel.class);
                    // Возвращаемся к основному потоку
                    if ((cwRequest != null) & (dwRequest != null) ) {
                        handler.post(() -> makeWeather(cwRequest, dwRequest));
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void makeWeather(CurrentForecastModel cwRequest, DailyForecastModel dwRequest){
        String tempEU;
        String pressEU;
        String windSpeedEU;
        String humidityEU;

        //Забираем инженерные еденицы из настроек
        tempEU = sharedPref.getBoolean(TEMP_EU, false) ? (resources.getString(R.string.celsius)) : (resources.getString(R.string.fahrenheit));
        pressEU = sharedPref.getBoolean(PRESS_EU, false) ? (resources.getString(R.string.pressEUTwo)) : (resources.getString(R.string.pressEUOne));
        windSpeedEU = sharedPref.getBoolean(WIND_EU, false) ? (resources.getString(R.string.windEUTwo)) : (resources.getString(R.string.windEUOne));
        humidityEU = resources.getString(R.string.humidityEU);

        currentForecastParcel.setCity(cwRequest.getName());                      // Забираем название года из прогноза
        currentForecastParcel.setDistrict(cwRequest.getName());                  // Пока район забираем так же
        currentForecastParcel.setTemp(cwRequest.getMain().getTemp());            // Забираем температуру
        currentForecastParcel.setTempEu(tempEU);                                        // Надо подумать как забрать
        currentForecastParcel.setImage(cwRequest.getWeather()[0].getIcon());     // Забираем иконку с сервера
        currentForecastParcel.setWeather(capitalize(cwRequest.getWeather()[0].getDescription()));   // Состояние погоды
        currentForecastParcel.setFeeling(cwRequest.getMain().getFeels_like());   // Ощущения
        currentForecastParcel.setFeelingEu(tempEU);                                     // Надо подумать как забрать
        currentForecastParcel.setWindSpeed(cwRequest.getWind().getSpeed());      // Скорость ветра
        currentForecastParcel.setWindSpeedEu(windSpeedEU);                                   // Надо подумать как забрать
        currentForecastParcel.setPressure((int) (cwRequest.getMain().getPressure() * HPA));          // Давление
        currentForecastParcel.setPressureEu(pressEU);                                    // Надо подумать как забрать
        currentForecastParcel.setHumidity(cwRequest.getMain().getHumidity());    // Влажность
        currentForecastParcel.setHumidityEu(humidityEU);                                    // Надо подумать как забрать

        dailyForecastParcel.setList(dwRequest.getList());
        mainActivity.setDailyForecastParcel(dailyForecastParcel);
        mainActivity.setCurrentForecastParcel(currentForecastParcel);
        mainActivity.initActivity();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
                result = getLines(in);
            //Город не найден
            } else if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                handler.post(() -> showNoticeToast(context.getResources().getString(R.string.cityNotFound)));
            //Не верный API ключ
            } else if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                handler.post(() -> showNoticeToast(context.getResources().getString(R.string.invalidKey)));
            //Другая ошибка связи
            } else {
                String response = urlConnection.getResponseMessage();
                handler.post(() -> showNoticeToast(response));
            }
        //Нет интернета
        } catch (SocketTimeoutException e) {
            handler.post(() -> showNoticeToast(context.getResources().getString(R.string.connectionTimeout)));
        //Другая ошибка
        } catch (Exception e) {
            handler.post(() -> showNoticeToast(e.getMessage()));
            e.printStackTrace();
        } finally {
            if (null != urlConnection) {
                urlConnection.disconnect();
            }
        }
        return result;
    }


    private String getLines(BufferedReader in) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = in.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        return stringBuilder.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showNoticeToast(String message){
        Toast toast = Toast.makeText(context,
                message,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = toast.getView();
        view.setBackgroundResource(R.drawable.border);
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setTextSize(36);
        toast.show();
    }
}
