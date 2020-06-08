package ru.magzyumov.weatherapp.Forecast.Display;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ru.magzyumov.weatherapp.App;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Database.Location.LocationDao;
import ru.magzyumov.weatherapp.Database.Location.LocationDataSource;
import ru.magzyumov.weatherapp.Database.Location.LocationSource;
import ru.magzyumov.weatherapp.Database.Location.Locations;
import ru.magzyumov.weatherapp.Forecast.Model.CurrentForecastModel;
import ru.magzyumov.weatherapp.Forecast.Model.OneCallModel;
import ru.magzyumov.weatherapp.Forecast.Model.OneCallModel.Current;
import ru.magzyumov.weatherapp.Forecast.Model.OneCallModel.Daily;
import ru.magzyumov.weatherapp.Forecast.Model.OneCallModel.Hourly;
import ru.magzyumov.weatherapp.R;

import static org.apache.commons.lang3.StringUtils.capitalize;

public class ResponseParser implements Constants {
    private Gson gson;
    private Resources resources;
    private SharedPreferences sharedPrefSettings;
    private CurrentForecastModel currentForecastModel;
    private String tempEU;
    private String pressureEU;
    private String windSpeedEU;
    private String humidityEu;

    private Locations currentLocation;
    private LocationDao locationDao;
    private LocationDataSource locationSource;

    // Конструктор для парсинга модели
    public ResponseParser (Resources resources){
        this.gson = new Gson();
        this.resources = resources;
        this.sharedPrefSettings = App.getInstance()
                .getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        this.locationDao = App.getInstance().getLocationDao();
        this.locationSource = new LocationSource(locationDao);
    }

    private void initEU(){
        tempEU = sharedPrefSettings.getBoolean(EU,false) ? (resources.getString(R.string.fahrenheit)) : (resources.getString(R.string.celsius));
        windSpeedEU = sharedPrefSettings.getBoolean(EU,false) ? (resources.getString(R.string.windEUTwo)) : (resources.getString(R.string.windEUOne));
        pressureEU = sharedPrefSettings.getBoolean(EU,false) ? (resources.getString(R.string.pressEUTwo)) : (resources.getString(R.string.pressEUOne));
        humidityEu = resources.getString(R.string.humidityEU);
    }

    public CurrentForecast getCurrentForecast(CurrentForecastModel cfResponse){
        CurrentForecast result = null;
        Locations currentLocation = locationSource.getCurrentLocation();

        initEU();

        if (cfResponse != null) {
            int[] imagesFirst = getImageArray(R.array.firstBackLayerPic);

            int backImageFirst = 0;
            int backImageSecond = 0;
            boolean isDay = false;

            String city = cfResponse.getName();
            String district = (currentLocation != null) ? currentLocation.region : city;
            String temp = String.valueOf((int)cfResponse.getMain().getTemp());
            String image = String.format(IMAGE_URL,cfResponse.getWeather()[0].getIcon());
            String weather = capitalize(cfResponse.getWeather()[0].getDescription());
            String feeling = String.valueOf((int)cfResponse.getMain().getFeels_like());
            String feelingEu = tempEU;
            String windSpeed = String.valueOf((int)cfResponse.getWind().getSpeed());
            String pressure = sharedPrefSettings.getBoolean(EU,false) ?
                    (String.valueOf((cfResponse.getMain().getPressure()))) :
                    (String.valueOf((int)(cfResponse.getMain().getPressure() * HPA)));

            String humidity = String.valueOf(cfResponse.getMain().getHumidity());

            float tempForDb = cfResponse.getMain().getTemp();
            long date = cfResponse.getDt();

            if (cfResponse.getDt() > cfResponse.getSys().getSunrise()) isDay = true;
            if (cfResponse.getDt() > cfResponse.getSys().getSunset()) isDay = false;

            backImageFirst = isDay ? imagesFirst[0] : imagesFirst[1];
            backImageSecond = getSecondPic(cfResponse.getWeather()[0].getId(), isDay);

            result = new CurrentForecast(city, district, temp, tempEU, image, weather, feeling,
                    feelingEu, windSpeed, windSpeedEU, pressure, pressureEU, humidity, humidityEu,
                    tempForDb, date, backImageFirst, backImageSecond);
        }

        return result;
    }

    public CurrentForecast getCurrentForecast(OneCallModel response){

        Current cfResponse = response.getCurrent();
        CurrentForecast result = null;

        initEU();

        if (cfResponse != null) {
            int[] imagesFirst = getImageArray(R.array.firstBackLayerPic);

            int backImageFirst = 0;
            int backImageSecond = 0;
            boolean isDay = false;

            String city = "";
            String district = city;
            String temp = String.valueOf((int)cfResponse.getTemp());
            String image = String.format(IMAGE_URL,cfResponse.getWeather()[0].getIcon());
            String weather = capitalize(cfResponse.getWeather()[0].getDescription());
            String feeling = String.valueOf((int)cfResponse.getFeelsLike());
            String feelingEu = tempEU;
            String windSpeed = String.valueOf((int)cfResponse.getWindSpeed());
            String pressure = sharedPrefSettings.getBoolean(EU,false) ?
                    (String.valueOf((cfResponse.getPressure()))) :
                    (String.valueOf((int)(cfResponse.getPressure() * HPA)));

            String humidity = String.valueOf(cfResponse.getHumidity());

            float tempForDb = (float) cfResponse.getTemp();
            long date = cfResponse.getDt();

            if (cfResponse.getDt() > cfResponse.getSunrise()) isDay = true;
            if (cfResponse.getDt() > cfResponse.getSunset()) isDay = false;

            backImageFirst = isDay ? imagesFirst[0] : imagesFirst[1];
            backImageSecond = getSecondPic(cfResponse.getWeather()[0].getId(), isDay);

            result = new CurrentForecast(city, district, temp, tempEU, image, weather, feeling,
                    feelingEu, windSpeed, windSpeedEU, pressure, pressureEU, humidity, humidityEu,
                    tempForDb, date, backImageFirst, backImageSecond);
        }

        return result;
    }

    public CurrentForecast getCurrentForecast(String response){
        initEU();

        return gson.fromJson(response, CurrentForecast.class);
    }

    public ForecastSource getDailyForecast(OneCallModel response){
        Daily[] dfResponse = response.getDaily();
        ForecastSource result = null;

        initEU();

        String date;
        String image;
        int temp;
        int windSpeed;
        int pressure;
        int humidity;

        if(dfResponse != null){
            result = new ForecastSource(dfResponse.length);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM", Locale.getDefault());

            Calendar calendar = Calendar.getInstance();

            for (Daily list : dfResponse) {
                calendar.setTimeInMillis(list.getDt()*1000L);
                date = dateFormat.format(calendar.getTime());

                image = String.format(IMAGE_URL, list.getWeather()[0].getIcon());
                temp = (int)list.getTemp().getDay();
                windSpeed = (int)list.getWindSpeed();

                pressure = sharedPrefSettings.getBoolean(EU,false) ?
                        (int)(list.getPressure()) :
                        (int)(list.getPressure() * HPA);

                humidity = (int)list.getHumidity();

                result.getDataSource().add(new Forecast(date, image, temp, windSpeed, pressure, humidity, tempEU, pressureEU, windSpeedEU));
            }
        }
        return result;
    }

    public ForecastSource getDailyForecast(String response){
        return gson.fromJson(response, ForecastSource.class);
    }

    public ForecastSource getHourlyForecast(OneCallModel response){
        Hourly[] dfResponse = response.getHourly();
        ForecastSource result = null;

        initEU();

        String date;
        String image;
        int temp;
        int windSpeed;
        int pressure;
        int humidity;

        if(dfResponse != null){
            result = new ForecastSource(dfResponse.length);

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

            Calendar calendar = Calendar.getInstance();

            for (Hourly list : dfResponse) {
                calendar.setTimeInMillis(list.getDt()*1000L);
                date = dateFormat.format(calendar.getTime());

                image = String.format(IMAGE_URL, list.getWeather()[0].getIcon());
                temp = (int)list.getTemp();
                windSpeed = (int)list.getWindSpeed();

                pressure = sharedPrefSettings.getBoolean(EU,false) ?
                        (int)(list.getPressure()) :
                        (int)(list.getPressure() * HPA);

                humidity = (int)list.getHumidity();

                result.getDataSource().add(new Forecast(date, image, temp, windSpeed, pressure, humidity, tempEU, pressureEU, windSpeedEU));
            }
        }
        return result;
    }

    public ForecastSource getHourlyForecast(String response){
        return gson.fromJson(response, ForecastSource.class);
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

    private String getNameCity(double latitude, double longitude) {
        String result = null;

        if (Geocoder.isPresent()) {
            try {
                Geocoder gc = new Geocoder(App.getInstance().getApplicationContext());
                List<Address> addresses = gc.getFromLocation (latitude, longitude, 1);
                for (Address a : addresses) {
                    for (int i = 0; i < a.getMaxAddressLineIndex() ; i++) {
                    }

                    if (a.hasLatitude() && a.hasLongitude()) {
                        result = a.getAddressLine(0);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private LatLng getCoordinateCity(String nameCity) {
        LatLng ll = null;
        if (Geocoder.isPresent()) {
            try {
                Geocoder gc = new Geocoder(App.getInstance().getApplicationContext());
                List<Address> addresses = gc.getFromLocationName(nameCity, 1);
                for (Address a : addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        ll = new LatLng(a.getLatitude(), a.getLongitude());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ll;
    }
}
