package ru.magzyumov.weatherapp.Forecast.Model;


public class List {
    private long dt;
    private MainDaily main;
    private Weather[] weather;
    private Clouds clouds;
    private WindDaily wind;
    private SysHourly sys;
    private String dt_txt;

    public long getDt() { return this.dt; }

    public void setDt(long dt) { this.dt = dt; }

    public MainDaily getMain() { return main; }

    public void setMain(MainDaily main) { this.main = main; }

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

    public WindDaily getWind() {
        return wind;
    }

    public void setWind(WindDaily wind) {
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
