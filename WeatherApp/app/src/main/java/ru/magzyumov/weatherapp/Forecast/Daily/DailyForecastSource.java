package ru.magzyumov.weatherapp.Forecast.Daily;

import android.content.res.Resources;
import android.content.res.TypedArray;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import ru.magzyumov.weatherapp.R;

public class DailyForecastSource implements DailyForecastDataSource {
    private int length;             //Длина прогноза
    private List<DailyForecast> dataSource;   // строим этот источник данных
    private Resources resources;    // ресурсы приложения
    private Calendar calendar;      //Календарь

    //Конструктор класса
    public DailyForecastSource(Resources resources, int length) {
        this.length = length;
        this.dataSource = new ArrayList<>(length);
        this.resources = resources;
        this.calendar = Calendar.getInstance();
    }

    public DailyForecastSource init(){
        String date;
        String dayName;
        int image;
        int tempDay;
        int tempNight;

        // Берем изображения из ресурсов
        int[] pictures = getImageArray();

        // заполнение источника данных
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        for (int i = 0; i < length; i++) {
            calendar.add(Calendar.DATE, (i==0)?(0):(1));
            date = dateFormat.format(calendar.getTime());
            dayName = dayFormat.format(calendar.getTime());

            image = (true) ? (pictures[(i*2)+1]) : (pictures[(i*2)+2]);

            tempDay = 12+i;
            tempNight = 5+i;

            dataSource.add(new DailyForecast (date, dayName, image, tempDay, tempNight));
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