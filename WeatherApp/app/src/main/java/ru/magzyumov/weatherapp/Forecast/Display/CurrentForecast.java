package ru.magzyumov.weatherapp.Forecast.Display;

public class CurrentForecast {
    private String city;
    private String district;
    private String temp;
    private String tempEu;
    private String image;
    private String weather;
    private String feeling;
    private String feelingEu;
    private String windSpeed;
    private String windSpeedEu;
    private String pressure;
    private String pressureEu;
    private String humidity;
    private String humidityEu;
    private float tempForDb;
    private long date;
    private int backImageFirst;
    private int backImageSecond;

    public CurrentForecast(String city, String district, String temp, String tempEu, String image,
                           String weather, String feeling, String feelingEu, String windSpeed,
                           String windSpeedEu, String pressure, String pressureEu,
                           String humidity, String humidityEu, float tempForDb, long date,
                           int backImageFirst, int backImageSecond) {

        this.city = city;
        this.district = district;
        this.temp = temp;
        this.tempEu = tempEu;
        this.image = image;
        this.weather = weather;
        this.feeling = feeling;
        this.feelingEu = feelingEu;
        this.windSpeed = windSpeed;
        this.windSpeedEu = windSpeedEu;
        this.pressure = pressure;
        this.pressureEu = pressureEu;
        this.humidity = humidity;
        this.humidityEu = humidityEu;
        this.tempForDb = tempForDb;
        this.date = date;
        this.setBackImageFirst(backImageFirst);
        this.setBackImageSecond(backImageSecond);
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

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public float getTempForDb() {
        return tempForDb;
    }

    public void setTempForDb(float temp) {
        this.tempForDb = temp;
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

    public String getFeeling() {
        return feeling;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public String getFeelingEu() {
        return feelingEu;
    }

    public void setFeelingEu(String feelingEu) {
        this.feelingEu = feelingEu;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindSpeedEu() {
        return windSpeedEu;
    }

    public void setWindSpeedEu(String windSpeedEu) {
        this.windSpeedEu = windSpeedEu;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getPressureEu() {
        return pressureEu;
    }

    public void setPressureEu(String pressureEu) {
        this.pressureEu = pressureEu;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getHumidityEu() {
        return humidityEu;
    }

    public void setHumidityEu(String humidityEu) {
        this.humidityEu = humidityEu;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getBackImageFirst() {
        return backImageFirst;
    }

    public void setBackImageFirst(int backImageFirst) {
        this.backImageFirst = backImageFirst;
    }

    public int getBackImageSecond() {
        return backImageSecond;
    }

    public void setBackImageSecond(int backImageSecond) {
        this.backImageSecond = backImageSecond;
    }
}
