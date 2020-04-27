package ru.magzyumov.weatherapp.Forecast.Hourly;

public class HourlyForecast {
    private String time;
    private int image;
    private String temp;

    public HourlyForecast(String time, int image, int temp){
        this.time = time;
        this.image = image;
        this.temp = String.valueOf(temp) + " \u2103";
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
}