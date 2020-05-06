package ru.magzyumov.weatherapp.Forecast;

import android.os.Parcel;
import android.os.Parcelable;

import ru.magzyumov.weatherapp.Forecast.Model.CurrForecast;

public class CurrentForecast implements Parcelable {
    private String city;
    private String district;
    private Float temp;
    private  String tempEu;
    private String image;
    private String weather;
    private Float feeling;
    private String feelingEu;
    private int windSpeed;
    private String windSpeedEu;
    private int pressure;
    private String pressureEu;
    private int humidity;
    private String humidityEu;

    private CurrForecast currentForecast;

    public CurrentForecast (CurrForecast currentForecast){
        setCity(currentForecast.getName());                      // Забираем название года из прогноза
        setDistrict(currentForecast.getName());                  // Пока район забираем так же
        setTemp(currentForecast.getMain().getTemp());            // Забираем температуру
        setTempEu("Temporaly");                                  // Надо подумать как забрать
        setImage(currentForecast.getWeather()[0].getIcon());     // Забираем иконку с сервера
        setWeather(currentForecast.getWeather()[0].getMain());   // Состояние погоды
        setFeeling(currentForecast.getMain().getFeels_like());   // Ощущения
        setFeelingEu("Temporaly");                               // Нвдо подумать как забрать
        setWindSpeed(currentForecast.getWind().getSpeed());      // Скорость ветра
        setWindSpeedEu("Temporaly");                             // Нвдо подумать как забрать
        setPressure(currentForecast.getMain().getPressure());    // Давление
        setPressureEu("Temporaly");                              // Нвдо подумать как забрать
        setHumidity(currentForecast.getMain().getHumidity());    // Влажность
        setHumidityEu("Temporaly");                              // Нвдо подумать как забрать
    }

    // Пока сделаем пустой конструктор
    // и будем заполнять данные спонтанно
    public CurrentForecast(){
        setCity("UFA");
        setDistrict("UFA");
        setTemp(23f);
        setTempEu("\u2103");
        setImage("day_clear");
        setWeather("Облачно");
        setFeeling(26f);
        setFeelingEu("\u2103");
        setWindSpeed(4);
        setWindSpeedEu("м/c");
        setPressure(748);
        setPressureEu("мм.рт.ст");
        setHumidity(39);
        setHumidityEu("%");
    }


    protected CurrentForecast(Parcel in) {
        setCity(in.readString());
        setDistrict(in.readString());
        setTemp(in.readFloat());
        setTempEu(in.readString());
        setImage(in.readString());
        setWeather(in.readString());
        setFeeling(in.readFloat());
        setFeelingEu(in.readString());
        setWindSpeed(in.readInt());
        setWindSpeedEu(in.readString());
        setPressure(in.readInt());
        setPressureEu(in.readString());
        setHumidity(in.readInt());
        setHumidityEu(in.readString());
    }

    public static final Creator<CurrentForecast> CREATOR = new Creator<CurrentForecast>() {
        @Override
        public CurrentForecast createFromParcel(Parcel in) {
            return new CurrentForecast(in);
        }

        @Override
        public CurrentForecast[] newArray(int size) {
            return new CurrentForecast[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getCity());
        dest.writeString(getDistrict());
        dest.writeFloat(getTemp());
        dest.writeString(getTempEu());
        dest.writeString(getImage());
        dest.writeString(getWeather());
        dest.writeFloat(getFeeling());
        dest.writeString(getFeelingEu());
        dest.writeString(getWindSpeed());
        dest.writeString(getWindSpeedEu());
        dest.writeString(getPressure());
        dest.writeString(getPressureEu());
        dest.writeString(getHumidity());
        dest.writeString(getHumidityEu());
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Float getTemp() {
        return temp;
    }

    public void setTemp(Float temp) {
        this.temp = temp;
    }

    public String getTempEu() {
        return tempEu;
    }

    public void setTempEu(String tempEu) {
        this.tempEu = tempEu;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Float getFeeling() {
        return feeling;
    }

    public void setFeeling(Float feeling) {
        this.feeling = feeling;
    }

    public String getFeelingEu() {
        return feelingEu;
    }

    public void setFeelingEu(String feelingEu) {
        this.feelingEu = feelingEu;
    }

    public String getWindSpeed() {
        return String.valueOf(windSpeed);
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindSpeedEu() {
        return windSpeedEu;
    }

    public void setWindSpeedEu(String windSpeedEu) {
        this.windSpeedEu = windSpeedEu;
    }

    public String getPressure() {
        return String.valueOf(pressure);
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public String getPressureEu() {
        return pressureEu;
    }

    public void setPressureEu(String pressureEu) {
        this.pressureEu = pressureEu;
    }

    public String getHumidity() {
        return String.valueOf(humidity);
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getHumidityEu() {
        return humidityEu;
    }

    public void setHumidityEu(String humidityEu) {
        this.humidityEu = humidityEu;
    }
}
