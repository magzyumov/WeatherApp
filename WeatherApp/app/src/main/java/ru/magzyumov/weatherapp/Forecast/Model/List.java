package ru.magzyumov.weatherapp.Forecast.Model;

public class List {
    private long dt;
    private MainHourly main;
    private Weather[] weather;
    private Clouds clouds;
    private Wind wind;
    private SysHourly sys;
    private String dt_txt;

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public MainHourly getMain() {
        return main;
    }

    public void setMain(MainHourly main) {
        this.main = main;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public SysHourly getSys() {
        return sys;
    }

    public void setSys(SysHourly sys) {
        this.sys = sys;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }
}
