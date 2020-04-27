package ru.magzyumov.weatherapp.Forecast.Hourly;

import android.content.res.Resources;
import android.content.res.TypedArray;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import ru.magzyumov.weatherapp.R;

public class HourlyForecastSource implements HourlyForecastDataSource {
    private int length;                         //Длина прогноза
    private List<HourlyForecast> dataSource;    // Строим этот источник данных
    private Resources resources;                // Ресурсы приложения
    private Calendar calendar;                  //Календарь

    //Конструктор класса
    public HourlyForecastSource(Resources resources, int length) {
        this.length = length;
        this.dataSource = new ArrayList<>(length);
        this.resources = resources;
        this.calendar = Calendar.getInstance();
    }

    public HourlyForecastSource init(){
        String time;
        int temp;
        int image;
        int cnt = 0;

        calendar.setTimeInMillis (System.currentTimeMillis());

        // Берем изображения из ресурсов
        int[] pictures = getImageArray();

        // заполнение источника данных
        for (int i = 0; i < length-15; i++) {
            if((calendar.get(Calendar.HOUR_OF_DAY) + i) == 23) cnt = i;
            time = String.format("%02d:00", (((calendar.get(Calendar.HOUR_OF_DAY)+i) >= 24) ? (i-cnt-1) : (calendar.get(Calendar.HOUR_OF_DAY)+i)));
            image = (true) ? (pictures[(i*2)+1]) : (pictures[(i*2)+2]);
            temp = (12+i);
            dataSource.add(new HourlyForecast(time, image, temp));
        }
        return this;
    }

    public HourlyForecast getHourlyForecast(int position) {
        return dataSource.get(position);
    }

    public int size(){
        return dataSource.size();
    }

    // Механизм вытаскивания идентификаторов картинок (к сожалению просто массив не работает)
    private int[] getImageArray(){
        TypedArray pictures = resources.obtainTypedArray(R.array.weather_images_hourly);
        int length = pictures.length();
        int[] answer = new int[length];
        for(int i = 0; i < length; i++){
            answer[i] = pictures.getResourceId(i, 0);
        }
        return answer;
    }
}