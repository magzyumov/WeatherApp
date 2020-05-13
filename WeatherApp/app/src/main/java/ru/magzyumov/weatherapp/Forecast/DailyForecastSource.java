package ru.magzyumov.weatherapp.Forecast;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Forecast.Model.DailyForecastModel;
import ru.magzyumov.weatherapp.R;

import static org.apache.commons.lang3.StringUtils.capitalize;

public class DailyForecastSource implements DailyForecastDataSource, Constants {
    private DailyForecastModel dailyForecastModel;  // Данные с прогнозом с сервера
    private int length;                             // Длина прогноза
    private List<DailyForecast> dataSource;         // Строим этот источник данных
    private Resources resources;                    // Ресурсы приложения
    private Context context;                        // Контекст приложения
    private Calendar calendar;                      // Календарь
    private SharedPreferences sharedPref;           // Настройки приложения

    //Конструктор класса
    public DailyForecastSource(Resources resources, Context context, DailyForecastModel dailyForecast) {
        this.dailyForecastModel = dailyForecast;
        this.length = dailyForecast.getList().length;
        this.dataSource = new ArrayList<>(length);
        this.resources = resources;
        this.calendar = Calendar.getInstance();
        this.context = context;
        this.sharedPref = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
    }

    public DailyForecastSource init(){
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

        //Забираем инженерные еденицы из настроек
        tempEU = sharedPref.getBoolean(TEMP_EU, false) ? (resources.getString(R.string.fahrenheit)) : (resources.getString(R.string.celsius));
        pressEU = sharedPref.getBoolean(PRESS_EU, false) ? (resources.getString(R.string.pressEUTwo)) : (resources.getString(R.string.pressEUOne));
        windSpeedEU = sharedPref.getBoolean(WIND_EU, false) ? (resources.getString(R.string.windEUTwo)) : (resources.getString(R.string.windEUOne));

        // Берем изображения из ресурсов
        int[] pictures = getImageArray();

        // заполнение источника данных
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM HH:mm", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        for (int i = 0; i < length; i++) {
            calendar.add(Calendar.DATE, (i==0)?(0):(1));
            date = dateFormat.format(calendar.getTime());
            dayName = dayFormat.format(calendar.getTime());
            dayName = capitalize(dayName);

            if (dailyForecastModel != null) {
                calendar.setTimeInMillis(dailyForecastModel.getList()[i].getDt()*1000L);
                date = dateFormat.format(calendar.getTime());
                dayName = dayFormat.format(calendar.getTime());
                dayName = capitalize(dayName);

                image = (true) ? (pictures[(2)+1]) : (pictures[(2)+2]);
                temp = (int)dailyForecastModel.getList()[i].getMain().getTemp();
                windSpeed = (int)dailyForecastModel.getList()[i].getWind().getSpeed();
                pressure = (int) (dailyForecastModel.getList()[i].getMain().getPressure() * HPA);
                humidity = dailyForecastModel.getList()[i].getMain().getHumidity();
            } else {
                image = (true) ? (pictures[(i*2)+1]) : (pictures[(i*2)+2]);
                temp = i;
                windSpeed = i;
                pressure = i;
                humidity = i;
            }

            dataSource.add(new DailyForecast (date, dayName, image, temp, windSpeed, pressure, humidity, tempEU, pressEU, windSpeedEU));
        }
        return this;
    }

    public DailyForecast getDailyForecast(int position) {
        return dataSource.get(position);
    }

    public int size(){
        return dataSource.size();
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