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
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

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
            int[] imagesFirst = getImageArray(R.array.firstBackLayerPic);

            int backImageFirst = 0;
            int backImageSecond = 0;
            boolean isDay = false;

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

            if (cfResponse.getDt() > cfResponse.getSys().getSunrise()) isDay = true;
            if (cfResponse.getDt() > cfResponse.getSys().getSunset()) isDay = false;

            backImageFirst = isDay ? imagesFirst[0] : imagesFirst[1];
            backImageSecond = getSecondPic(cfResponse.getWeather()[0].getId(), isDay);

            result = new CurrentForecast(city, district, temp, tempEu, image, weather, feeling,
                    feelingEu, windSpeed, windSpeedEu, pressure, pressureEu, humidity, humidityEu,
                    tempForDb, date, backImageFirst, backImageSecond);
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
            int[] pictures = getImageArray(R.array.weather_images_daily);

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
    private int[] getImageArray(int resId){
        TypedArray pictures = resources.obtainTypedArray(resId);
        int length = pictures.length();
        int[] answer = new int[length];
        for(int i = 0; i < length; i++){
            answer[i] = pictures.getResourceId(i, 0);
        }

        return answer;
    }

    // Проверка картинки заднего фона
    private int getSecondPic(int weatherCode, boolean isDay){
        int[] imagesSecond = getImageArray(R.array.secondBackLayerPic);
        int result = 0;
        switch (weatherCode){
            case 500:
                result = imagesSecond[0];
                break;
            case 501:
                result = imagesSecond[1];
                break;
            case 502:
                result = imagesSecond[2];
                break;
            case 600:
                result = imagesSecond[3];
                break;
            case 601:
                result = imagesSecond[4];
                break;
            case 602:
                result = imagesSecond[5];
                break;
            case 615:
                result = imagesSecond[6];
                break;
            case 616:
                result = imagesSecond[7];
                break;
            case 620:
                result = imagesSecond[8];
                break;
            case 801:
                result = isDay ?  imagesSecond[9] : imagesSecond[15];
                break;
            case 802:
                result = isDay ?  imagesSecond[10] : imagesSecond[16];
                break;
            case 803:
            case 804:
                result = isDay ?  imagesSecond[11] : imagesSecond[17];
                break;
            case 700:
            case 790:
                result = isDay ?  imagesSecond[12] : imagesSecond[18];
                break;
            default:
                result = imagesSecond[21];
                break;
        }
        return result;
    }
}
