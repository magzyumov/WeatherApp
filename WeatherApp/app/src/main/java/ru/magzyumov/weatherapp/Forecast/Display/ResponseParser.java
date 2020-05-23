package ru.magzyumov.weatherapp.Forecast.Display;

import android.content.res.Resources;
import android.content.res.TypedArray;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Forecast.Model.CurrentForecastModel;
import ru.magzyumov.weatherapp.Forecast.Model.DailyForecastModel;
import ru.magzyumov.weatherapp.Forecast.Model.ForecastList;
import ru.magzyumov.weatherapp.R;

import static org.apache.commons.lang3.StringUtils.capitalize;

public class ResponseParser implements Constants {
    private Gson gson;
    private Resources resources;
    private CurrentForecastModel currentForecastModel;
    private DailyForecastModel dailyForecastModel;

    // Конструктор для парсинга модели
    public ResponseParser (Resources resources){
        this.gson = new Gson();
        this.resources = resources;
    }

    public CurrentForecast getCurrentForecast(String response){
        CurrentForecast result = null;
        CurrentForecastModel cfResponse = gson.fromJson(response, CurrentForecastModel.class);

        if (cfResponse != null) {
            String city = cfResponse.getName();
            String district = cfResponse.getName();
            String temp = String.valueOf((int)cfResponse.getMain().getTemp());
            String tempEu = "";
            String image = cfResponse.getWeather()[0].getIcon();
            String weather = capitalize(cfResponse.getWeather()[0].getDescription());
            String feeling = String.valueOf((int)cfResponse.getMain().getFeels_like());
            String feelingEu = "";
            String windSpeed = String.valueOf((int)cfResponse.getWind().getSpeed());
            String windSpeedEu = "";
            String pressure = String.valueOf((int)(cfResponse.getMain().getPressure() * HPA));
            String pressureEu = "";
            String humidity = String.valueOf(cfResponse.getMain().getHumidity());
            String humidityEu = "";
            float tempForDb = cfResponse.getMain().getTemp();
            long date = cfResponse.getDt();

            result = new CurrentForecast(city, district, temp, tempEu, image, weather, feeling,
                    feelingEu, windSpeed, windSpeedEu, pressure, pressureEu, humidity, humidityEu, tempForDb, date);
        }

        return result;
    }

    public DailyForecastSource getDailyForecast(String response){
        DailyForecastSource result = null;

        String date;
        String dayName;
        String tempEU;
        String pressEU;
        String windSpeedEU;
        int image;
        int temp;
        int windSpeed;
        int pressure;
        int humidity;

        DailyForecastModel dfResponse = gson.fromJson(response, DailyForecastModel.class);

        if(dfResponse != null){
            result = new DailyForecastSource(dfResponse.getList().length);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM HH:mm", Locale.getDefault());
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

            Calendar calendar = Calendar.getInstance();

            //Забираем инженерные еденицы из настроек
            tempEU = resources.getString(R.string.celsius);
            pressEU = resources.getString(R.string.pressEUOne);
            windSpeedEU = resources.getString(R.string.windEUOne);

            // Берем изображения из ресурсов
            int[] pictures = getImageArray();

            for (ForecastList list : dfResponse.getList()) {
                calendar.setTimeInMillis(list.getDt()*1000L);
                date = dateFormat.format(calendar.getTime());
                dayName = dayFormat.format(calendar.getTime());
                dayName = capitalize(dayName);

                //image = list.getWeather()[0].getIcon();
                image = (true) ? (pictures[(2)+1]) : (pictures[(2)+2]);
                temp = (int)list.getMain().getTemp();
                windSpeed = (int)list.getWind().getSpeed();
                pressure = (int) (list.getMain().getPressure() * HPA);
                humidity = list.getMain().getHumidity();

                result.getDataSource().add(new DailyForecast (date, dayName, image, temp, windSpeed, pressure, humidity, tempEU, pressEU, windSpeedEU));
            }
        }

        return result;
    }

    // Механизм вытаскивания идентификаторов картинок (к сожалению просто массив не работает)
    private int[] getImageArray(){
        TypedArray pictures = resources.obtainTypedArray(R.array.weather_images_daily);
        int length = pictures.length();
        int[] answer = new int[length];
        for(int i = 0; i < length; i++){
            answer[i] = pictures.getResourceId(i, 0);
        }
        return answer;
    }
}
