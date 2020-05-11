package ru.magzyumov.weatherapp.Forecast;

public class DailyForecast {
    private String date;
    private String dayName;
    private int image;
    private String temp;
    private String windSpeed;
    private String pressure;
    private String humidity;
    private String tempEU;
    private String pressEU;
    private String windSpeedEU;

    public DailyForecast (String date, String dayName, int image,
                          float temp, float windSpeed, int pressure, int humidity,
                          String tempEU, String pressEU, String windSpeedEU){
        this.date = date;
        this.dayName = dayName;
        this.image = image;
        this.temp = String.valueOf(temp);
        this.windSpeed = String.valueOf(windSpeed);
        this.pressure = String.valueOf(pressure);
        this.humidity = String.valueOf(humidity);
        this.tempEU = tempEU;
        this.pressEU = pressEU;
        this.windSpeedEU = windSpeedEU;
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

    public String getTemp() { return temp; }

    public String getWindSpeed() { return windSpeed; }

    public String getPressure() { return pressure; }

    public String getHumidity() { return humidity; }

    public String getTempEU() { return tempEU; }

    public String getPressureEU() { return pressEU; }

    public String getWindSpeedEU() { return windSpeedEU; }
}
