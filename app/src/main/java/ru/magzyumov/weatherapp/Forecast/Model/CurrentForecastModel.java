package ru.magzyumov.weatherapp.Forecast.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentForecastModel {

    @SerializedName("coord")
    @Expose
    private Coord coord;

    @SerializedName("weather")
    @Expose
    private Weather[] weather;

    @SerializedName("base")
    @Expose
    private String base;

    @SerializedName("main")
    @Expose
    private Main main;

    @SerializedName("visibility")
    @Expose
    private int visibility;

    @SerializedName("wind")
    @Expose
    private Wind wind;

    @SerializedName("clouds")
    @Expose
    private Clouds clouds;

    @SerializedName("dt")
    @Expose
    private long dt;

    @SerializedName("sys")
    @Expose
    private Sys sys;

    @SerializedName("timezone")
    @Expose
    private int timezone;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("cod")
    @Expose
    private int cod;

    public Coord getCoord() { return coord;  }

    public void setCoord(Coord coord) { this.coord = coord; }

    public Weather[] getWeather() { return weather; }

    public void setWeather(Weather[] weather) { this.weather = weather; }

    public String getBase() { return base; }

    public void setBase(String base) { this.base = base; }

    public Main getMain() { return main; }

    public void setMain(Main main) { this.main = main; }

    public int getVisibility() { return visibility; }

    public void setVisibility(int visibility) { this.visibility = visibility; }

    public Wind getWind() { return wind; }

    public void setWind(Wind wind) { this.wind = wind; }

    public Clouds getClouds() { return clouds; }

    public void setClouds(Clouds clouds) { this.clouds = clouds; }

    public long getDt() { return dt; }

    public void setDt(long dt) { this.dt = dt; }

    public Sys getSys() { return sys; }

    public void setSys(Sys sys) { this.sys = sys; }

    public int getTimezone() { return timezone; }

    public void setTimezone(int timezone) { this.timezone = timezone; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }



    public class Clouds {

        @SerializedName("all")
        @Expose
        private int all;

        public int getAll() {
            return all;
        }

        public void setAll(int all) {
            this.all = all;
        }

    }

    public class Coord {

        @SerializedName("lat")
        @Expose
        private float lat;

        @SerializedName("lon")
        @Expose
        private float lon;

        public float getLon() {
            return lon;
        }

        public void setLon(float lon) {
            this.lon = lon;
        }

        public float getLat() {
            return lat;
        }

        public void setLat(float lat) {
            this.lat = lat;
        }
    }

    public class Main {

        @SerializedName("temp")
        @Expose
        private float temp;

        @SerializedName("feels_like")
        @Expose
        private float feels_like;

        @SerializedName("temp_min")
        @Expose
        private float temp_min;

        @SerializedName("temp_max")
        @Expose
        private float temp_max;

        @SerializedName("pressure")
        @Expose
        private int pressure;

        @SerializedName("humidity")
        @Expose
        private int humidity;

        public float getTemp() {
            return temp;
        }

        public void setTemp(float temp) {
            this.temp = temp;
        }

        public float getFeels_like() {
            return feels_like;
        }

        public void setFeels_like(float feels_like) {
            this.feels_like = feels_like;
        }

        public float getTemp_min() {
            return temp_min;
        }

        public void setTemp_min(float temp_min) {
            this.temp_min = temp_min;
        }

        public float getTemp_max() {
            return temp_max;
        }

        public void setTemp_max(float temp_max) {
            this.temp_max = temp_max;
        }

        public int getPressure() {
            return pressure;
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

    }

    public class Sys {

        @SerializedName("type")
        @Expose
        private int type;

        @SerializedName("id")
        @Expose
        private int id;

        @SerializedName("country")
        @Expose
        private String country;

        @SerializedName("sunrise")
        @Expose
        private long sunrise;

        @SerializedName("sunset")
        @Expose
        private long sunset;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public long getSunrise() {
            return sunrise;
        }

        public void setSunrise(long sunrise) {
            this.sunrise = sunrise;
        }

        public long getSunset() {
            return sunset;
        }

        public void setSunset(long sunset) {
            this.sunset = sunset;
        }

    }

    public class Weather {

        @SerializedName("id")
        @Expose
        private int id;

        @SerializedName("main")
        @Expose
        private String main;

        @SerializedName("description")
        @Expose
        private String description;

        @SerializedName("icon")
        @Expose
        private String icon;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

    }

    public class Wind {

        @SerializedName("speed")
        @Expose
        private float speed;

        @SerializedName("deg")
        @Expose
        private int deg;

        public int getDeg() {
            return deg;
        }

        public void setDeg(int deg) {
            this.deg = deg;
        }

        public float getSpeed() { return speed; }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

    }
}

