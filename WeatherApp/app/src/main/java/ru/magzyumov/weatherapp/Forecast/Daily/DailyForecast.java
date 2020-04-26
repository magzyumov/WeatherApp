package ru.magzyumov.weatherapp.Forecast.Daily;

public class DailyForecast {
    private String date;
    private String dayName;
    private int image;
    private String tempDay;
    private String tempNight;

    public DailyForecast (String date, String dayName, int image, int tempDay, int tempNight){
        this.date = date;
        this.dayName = dayName;
        this.image = image;
        this.tempDay = String.valueOf(tempDay) + " \u2103";
        this.tempNight = String.valueOf(tempNight) + " \u2103";
    }

    public String getDate() {
        return date;
    }

    public String getDayName() {
        return dayName;
    }

    public int getImage() {
        return image;
    }

    public String getTempDay() {
        return tempDay;
    }

    public String getTempNight() {
        return tempNight;
    }
}
