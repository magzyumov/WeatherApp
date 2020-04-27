package ru.magzyumov.weatherapp.Forecast.Hourly;

public class HourlyForecast {
    private String windSpeed;
    private String pressure;
    private String humidity;
    private String time;
    private int image;
    private String temp;

    public HourlyForecast(String time, int image, int temp){
        this.time = time;
        this.image = image;
        this.temp = String.valueOf(temp) + " \u2103";
    }

    public HourlyForecast(int windSpeed, int pressure, int humidity){
        this.windSpeed = String.valueOf(windSpeed) + " м/с";;
        this.pressure = String.valueOf(pressure) + " мм.рт.ст";
        this.humidity = String.valueOf(humidity) + " %";
    }

    public String getTime() {
        return time;
    }

    public int getImage() {
        return image;
    }

    public String getTemp() {
        return temp;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getPressure() {
        return pressure;
    }

    public String getHumidity() {
        return humidity;
    }
}