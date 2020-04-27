package ru.magzyumov.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public final class Logic extends AppCompatActivity {
    //Внутреннее поле, будет хранить единственный экземпляр
    private static Logic instance = null;

    // Поле для синхронизации
    private static final Object syncObj = new Object();

    // Метод, который возвращает экземпляр объекта.
    // Если объекта нет, то создаем его.
    public static Logic getInstance(){
        // Здесь реализована «ленивая» инициализация объекта,
        // то есть, пока объект не нужен, не создаем его.
        synchronized (syncObj) {
            if (instance == null) {
                instance = new Logic();
            }
            return instance;
        }
    }

    //Поля для хранения
    private boolean isDay = false;          //Поле для хранения день или ночь
    private int currentHour = 0;            //Поле для хренения текущего часа
    private String backgroundPicName;       //Поле для хранение картинки для заднего фона


    // Конструктор (вызывать извне его нельзя, поэтому он приватный)
    private Logic(){
        backgroundPicName = "winter_city_night_overcast";
    }

    //Метод обновления данных
    public void refreshData(){
        getBackgroundPic();
        getDataFromServer();
    }

    public String getBackgroundPicName() {return this.backgroundPicName;}
    public int getCurrentHour() {return this.currentHour;}


    //Метод будет посылать запрос на сервер потом обрабатывать его
    private void getDataFromServer(){
        Random random = new SecureRandom();
        int temp = 90 - random.nextInt(181);
    }

    private void getBackgroundPic(){
        boolean dayClear = false;
        StringBuilder picName = new StringBuilder();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        int month = calendar.get(Calendar.MONTH);
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        switch (month) {
            case Calendar.DECEMBER:
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
                picName.append("winter_city_");
                break;
            case Calendar.MARCH:
            case Calendar.APRIL:
            case Calendar.MAY:
                picName.append("spring_city_");
                break;
            case Calendar.JUNE:
            case Calendar.JULY:
            case Calendar.AUGUST:
                picName.append("summer_city_");
                break;
            default:
                picName.append("autumn_city_");
                break;
        }

        if(((currentHour >= 6) & (currentHour <= 8)) || ((currentHour >= 19) & (currentHour <= 20))){
            isDay = false;
            picName.append("dawn_");
        }

        if((currentHour >= 9) & (currentHour <= 18)){
            isDay = true;
            picName.append("day_");
        }

        if((currentHour >= 21) || (currentHour <= 5)){
            isDay = false;
            picName.append("night_");
        }

        picName.append(dayClear ? ("clear") : ("overcast"));

        this.backgroundPicName = picName.toString();
    }
}
