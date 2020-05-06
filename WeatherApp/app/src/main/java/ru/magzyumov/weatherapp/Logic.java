package ru.magzyumov.weatherapp;

import android.content.res.Resources;
import android.content.res.TypedArray;

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
    public static Logic getInstance(Resources resources){
        // Здесь реализована «ленивая» инициализация объекта,
        // то есть, пока объект не нужен, не создаем его.
        synchronized (syncObj) {
            if (instance == null) {
                instance = new Logic(resources);
            }
            return instance;
        }
    }

    //Поля для хранения
    private boolean isDay = false;          //Поле для хранения день или ночь
    private int currentHour = 0;            //Поле для хренения текущего часа
    private int mainLayerPic;        //Поле для хранение картинки для заднего фона
    private int secondLayerPic;      //Поле для хранение картинки для заднего фона
    private Resources resources;


    // Конструктор (вызывать извне его нельзя, поэтому он приватный)
    private Logic(Resources resources){
        this.resources = resources;
    }

    //Метод обновления данных
    public void refreshData(){
        getBackgroundPic();
        getDataFromServer();
    }

    public int getMainLayerPic() {return this.mainLayerPic;}
    public int getSecondLayerPic() { return this.secondLayerPic;}
    public int getCurrentHour() {return this.currentHour;}

    //Метод будет посылать запрос на сервер потом обрабатывать его
    private void getDataFromServer(){
        Random random = new SecureRandom();
        int temp = 90 - random.nextInt(181);
    }

    private void getBackgroundPic(){
        int[] mainBackLayerPic;
        int[] secondBackLayerPic;
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());

        currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        mainBackLayerPic = getLayerImageArray(R.array.mainBackLayerPic);
        secondBackLayerPic = getLayerImageArray(R.array.secondBackLayerPic);

        if((currentHour >= 8) & (currentHour <= 19)){
            isDay = true;
        }

        if((currentHour >= 20) || (currentHour <= 7)){
            isDay = false;
        }

        this.mainLayerPic = isDay ? mainBackLayerPic[0] : mainBackLayerPic[1];
        this.secondLayerPic = isDay ? secondBackLayerPic[0] : secondBackLayerPic[1];
    }

    // Механизм вытаскивания идентификаторов картинок (к сожалению просто массив не работает)
    private int[] getLayerImageArray(int resId){
        TypedArray pictures = resources.obtainTypedArray(resId);
        int length = pictures.length();
        int[] answer = new int[length];
        for(int i = 0; i < length; i++){
            answer[i] = pictures.getResourceId(i, 0);
        }
        return answer;
    }
}
