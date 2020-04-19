package ru.magzyumov.weatherapp;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.android.gms.common.util.WorkSourceUtil.getNames;

public final class PicLogic extends AppCompatActivity {
    //Внутреннее поле, будет хранить единственный экземпляр
    private static PicLogic instance = null;

    // Поле для синхронизации
    private static final Object syncObj = new Object();

    // Метод, который возвращает экземпляр объекта.
    // Если объекта нет, то создаем его.
    public static PicLogic getInstance(){
        // Здесь реализована «ленивая» инициализация объекта,
        // то есть, пока объект не нужен, не создаем его.
        synchronized (syncObj) {
            if (instance == null) {
                instance = new PicLogic();
            }
            return instance;
        }
    }

    //Поля для хранения
    private boolean isDay = false;          //Поле для хранения день или ночь
    private int currentHour = 0;            //Поле для хренения текущего часа
    private String backgroundPicName;       //Поле для хранение картинки для заднего фона
    private String imageViewNowPic;         //Поле для хранения картинки текущей погоды
    private String imageView1HPic;          //Поле для хранения картинки погоды через 1 час
    private String imageView2HPic;          //Поле для хранения картинки погоды через 2 часа
    private String imageView3HPic;          //Поле для хранения картинки погоды через 3 часа
    private String imageView4HPic;          //Поле для хранения картинки погоды через 4 часа
    private String imageView5HPic;          //Поле для хранения картинки погоды через 5 часов
    private String imageView6HPic;          //Поле для хранения картинки погоды через 6 часов
    private String imageView7HPic;          //Поле для хранения картинки погоды через 7 часов
    private String imageView8HPic;          //Поле для хранения картинки погоды через 8 часов
    private String imageView9HPic;          //Поле для хранения картинки погоды через 9 часов
    private String imageView10HPic;         //Поле для хранения картинки погоды через 10 часов
    private String imageView11HPic;         //Поле для хранения картинки погоды через 11 часов
    private String imageView12HPic;         //Поле для хранения картинки погоды через 12 часов
    private String imageView13HPic;         //Поле для хранения картинки погоды через 13 часов
    private String imageView14HPic;         //Поле для хранения картинки погоды через 14 часов
    private String imageView15HPic;         //Поле для хранения картинки погоды через 15 часов
    private String imageView16HPic;         //Поле для хранения картинки погоды через 16 часов
    private String imageView17HPic;         //Поле для хранения картинки погоды через 17 часов
    private String imageView18HPic;         //Поле для хранения картинки погоды через 18 часов
    private String imageView19HPic;         //Поле для хранения картинки погоды через 19 часов
    private String imageView20HPic;         //Поле для хранения картинки погоды через 20 часов
    private String imageView21HPic;          //Поле для хранения картинки погоды через 21 час
    private String imageView22HPic;         //Поле для хранения картинки погоды через 22 часа
    private String imageView23HPic;         //Поле для хранения картинки погоды через 23 часа
    private String imageView24HPic;         //Поле для хранения картинки погоды через 24 часа

    private String imageViewTodayPic;       //Поле для хранения картинки погоды сегодня
    private String imageView1DPic;          //Поле для хранения картинки погоды через 1 день
    private String imageView2DPic;          //Поле для хранения картинки погоды через 2 дня
    private String imageView3DPic;          //Поле для хранения картинки погоды через 3 дня
    private String imageView4DPic;          //Поле для хранения картинки погоды через 4 дня
    private String imageView5DPic;          //Поле для хранения картинки погоды через 5 дней
    private String imageView6DPic;          //Поле для хранения картинки погоды через 6 дней
    private String imageView7DPic;          //Поле для хранения картинки погоды через 7 дней
    private String imageView8DPic;          //Поле для хранения картинки погоды через 8 дней
    private String imageView9DPic;          //Поле для хранения картинки погоды через 9 дней

    // Конструктор (вызывать извне его нельзя, поэтому он приватный)
    private PicLogic(){
        backgroundPicName = "winter_city_night_overcast";
    }

    //Метод обновления данных
    public void refreshData(){
        getBackgroundPic();
        getDataFromServer();
        getLinePicWeather();
    }

    public String getBackgroundPicName() {return this.backgroundPicName;}
    public int getCurrentHour() {return this.currentHour;}


    //Метод будет посылать запрос на сервер потом обрабатывать его
    private void getDataFromServer(){
        Random random = new SecureRandom();
        int temp = 90 - random.nextInt(181);
    }

    private void getLinePicWeather(){
        imageViewNowPic = (!isDay) ? ("bkn_d_line_light") : ("bkn_d_line_dark");
        imageView1HPic = (!isDay) ? ("bkn_minus_ra_d_line_light") : ("bkn_minus_ra_d_line_dark");
        imageView2HPic = (!isDay) ? ("bkn_minus_ra_n_line_light") : ("bkn_minus_ra_n_line_dark");
        imageView3HPic = (!isDay) ? ("bkn_minus_sn_d_line_light") : ("bkn_minus_sn_d_line_dark");
        imageView4HPic = (!isDay) ? ("bkn_minus_sn_n_line_light") : ("bkn_minus_sn_n_line_dark");
        imageView5HPic = (!isDay) ? ("bkn_n_line_light") : ("bkn_n_line_dark");
        imageView6HPic = (!isDay) ? ("bkn_plus_ra_d_line_light") : ("bkn_plus_ra_d_line_dark");
        imageView7HPic = (!isDay) ? ("bkn_plus_ra_n_line_light") : ("bkn_plus_ra_n_line_dark");
        imageView8HPic = (!isDay) ? ("bkn_plus_sn_d_line_light") : ("bkn_plus_sn_d_line_dark");
        imageView9HPic = (!isDay) ? ("bkn_plus_sn_n_line_light") : ("bkn_plus_sn_n_line_dark");
        imageView10HPic = (!isDay) ? ("bkn_ra_d_line_light") : ("bkn_ra_d_line_dark");
        imageView11HPic = (!isDay) ? ("bkn_ra_n_line_light") : ("bkn_ra_n_line_dark");
        imageView12HPic = (!isDay) ? ("bkn_sn_d_line_light") : ("bkn_sn_d_line_dark");
        imageView13HPic = (!isDay) ? ("bkn_sn_n_line_light") : ("bkn_sn_n_line_dark");
        imageView14HPic = (!isDay) ? ("bkn_d_line_light") : ("bkn_d_line_dark");
        imageView15HPic = (!isDay) ? ("bkn_minus_ra_d_line_light") : ("bkn_minus_ra_d_line_dark");
        imageView16HPic = (!isDay) ? ("bkn_minus_ra_n_line_light") : ("bkn_minus_ra_n_line_dark");
        imageView17HPic = (!isDay) ? ("bkn_minus_sn_d_line_light") : ("bkn_minus_sn_d_line_dark");
        imageView18HPic = (!isDay) ? ("bkn_plus_sn_n_line_light") : ("bkn_plus_sn_n_line_dark");
        imageView19HPic = (!isDay) ? ("bkn_n_line_light") : ("bkn_n_line_dark");
        imageView20HPic = (!isDay) ? ("bkn_plus_ra_n_line_light") : ("bkn_plus_ra_n_line_dark");
        imageView21HPic = (!isDay) ? ("bkn_ra_d_line_light") : ("bkn_ra_d_line_dark");
        imageView22HPic = (!isDay) ? ("bkn_ra_n_line_light") : ("bkn_ra_n_line_dark");
        imageView23HPic = (!isDay) ? ("bkn_sn_d_line_light") : ("bkn_sn_d_line_dark");
        imageView24HPic = (!isDay) ? ("bkn_sn_n_line_light") : ("bkn_sn_n_line_dark");
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
            case Calendar.SEPTEMBER:
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            default:
                picName.append("autumn_city_");
                break;
        }

        if(((currentHour >= 6) & (currentHour <= 8)) || ((currentHour >= 19) & (currentHour <= 20))){
            this.isDay = false;
            picName.append("dawn_");
        }

        if((currentHour >= 9) & (currentHour <= 18)){
            this.isDay = true;
            picName.append("day_");
        }

        if((currentHour >= 21) || (currentHour <= 5)){
            this.isDay = false;
            picName.append("night_");
        }

        picName.append(dayClear ? ("clear") : ("overcast"));

        this.backgroundPicName = picName.toString();
    }

    public void makeNowPic(){
        StringBuilder picName = new StringBuilder();
        this.isDay = false;
        this.imageViewNowPic = "";

        picName.append(isDay ? ("line_dark") : ("line_light"));
    }

    public void makeTodayPic(){
        StringBuilder picName = new StringBuilder();
        this.imageViewTodayPic = "";
    }

    //Набор для возврата имен мелких картинок погоды
    //Набор типов полей
    public enum Field {
        IMAGE_VIEW_NOW_PIC,
        IMAGE_VIEW_1H_PIC,
        IMAGE_VIEW_2H_PIC,
        IMAGE_VIEW_3H_PIC,
        IMAGE_VIEW_4H_PIC,
        IMAGE_VIEW_5H_PIC,
        IMAGE_VIEW_6H_PIC,
        IMAGE_VIEW_7H_PIC,
        IMAGE_VIEW_8H_PIC,
        IMAGE_VIEW_9H_PIC,
        IMAGE_VIEW_10H_PIC,
        IMAGE_VIEW_11H_PIC,
        IMAGE_VIEW_12H_PIC,
        IMAGE_VIEW_13H_PIC,
        IMAGE_VIEW_14H_PIC,
        IMAGE_VIEW_15H_PIC,
        IMAGE_VIEW_16H_PIC,
        IMAGE_VIEW_17H_PIC,
        IMAGE_VIEW_18H_PIC,
        IMAGE_VIEW_19H_PIC,
        IMAGE_VIEW_20H_PIC,
        IMAGE_VIEW_21H_PIC,
        IMAGE_VIEW_22H_PIC,
        IMAGE_VIEW_23H_PIC,
        IMAGE_VIEW_24H_PIC;
    }

    public String getLinePic(Field field){
        switch (field) {
            case IMAGE_VIEW_NOW_PIC:
                return getImageViewNowPic();
            case IMAGE_VIEW_1H_PIC:
                return getImageView1HPic();
            case IMAGE_VIEW_2H_PIC:
                return getImageView2HPic();
            case IMAGE_VIEW_3H_PIC:
                return getImageView3HPic();
            case IMAGE_VIEW_4H_PIC:
                return getImageView4HPic();
            case IMAGE_VIEW_5H_PIC:
                return getImageView5HPic();
            case IMAGE_VIEW_6H_PIC:
                return getImageView6HPic();
            case IMAGE_VIEW_7H_PIC:
                return getImageView7HPic();
            case IMAGE_VIEW_8H_PIC:
                return getImageView8HPic();
            case IMAGE_VIEW_9H_PIC:
                return getImageView9HPic();
            case IMAGE_VIEW_10H_PIC:
                return getImageView10HPic();
            case IMAGE_VIEW_11H_PIC:
                return getImageView11HPic();
            case IMAGE_VIEW_12H_PIC:
                return getImageView12HPic();
            case IMAGE_VIEW_13H_PIC:
                return getImageView13HPic();
            case IMAGE_VIEW_14H_PIC:
                return getImageView14HPic();
            case IMAGE_VIEW_15H_PIC:
                return getImageView15HPic();
            case IMAGE_VIEW_16H_PIC:
                return getImageView16HPic();
            case IMAGE_VIEW_17H_PIC:
                return getImageView17HPic();
            case IMAGE_VIEW_18H_PIC:
                return getImageView18HPic();
            case IMAGE_VIEW_19H_PIC:
                return getImageView19HPic();
            case IMAGE_VIEW_20H_PIC:
                return getImageView20HPic();
            case IMAGE_VIEW_21H_PIC:
                return getImageView21HPic();
            case IMAGE_VIEW_22H_PIC:
                return getImageView22HPic();
            case IMAGE_VIEW_23H_PIC:
                return getImageView23HPic();
            case IMAGE_VIEW_24H_PIC:
            default:
                return getImageView24HPic();
        }
    }

    public String getImageViewNowPic() { return imageViewNowPic; }
    public String getImageView1HPic() { return imageView1HPic; }
    public String getImageView2HPic() { return imageView2HPic; }
    public String getImageView3HPic() { return imageView3HPic; }
    public String getImageView4HPic() { return imageView4HPic; }
    public String getImageView5HPic() { return imageView5HPic; }
    public String getImageView6HPic() { return imageView6HPic; }
    public String getImageView7HPic() { return imageView7HPic; }
    public String getImageView8HPic() { return imageView8HPic; }
    public String getImageView9HPic() { return imageView9HPic; }
    public String getImageView10HPic() { return imageView10HPic; }
    public String getImageView11HPic() { return imageView11HPic; }
    public String getImageView12HPic() { return imageView12HPic; }
    public String getImageView13HPic() { return imageView13HPic; }
    public String getImageView14HPic() { return imageView14HPic; }
    public String getImageView15HPic() { return imageView15HPic; }
    public String getImageView16HPic() { return imageView16HPic; }
    public String getImageView17HPic() { return imageView17HPic; }
    public String getImageView18HPic() { return imageView18HPic; }
    public String getImageView19HPic() { return imageView19HPic; }
    public String getImageView20HPic() { return imageView20HPic; }
    public String getImageView21HPic() { return imageView21HPic; }
    public String getImageView22HPic() { return imageView22HPic; }
    public String getImageView23HPic() { return imageView23HPic; }
    public String getImageView24HPic() { return imageView24HPic; }
}
