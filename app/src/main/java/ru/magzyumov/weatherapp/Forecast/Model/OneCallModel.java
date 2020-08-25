package ru.magzyumov.weatherapp.Forecast.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OneCallModel {

    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lon")
    @Expose
    private double lon;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("timezone_offset")
    @Expose
    private long timezoneOffset;
    @SerializedName("current")
    @Expose
    private Current current;
    @SerializedName("minutely")
    @Expose
    private Minutely[] minutely = null;
    @SerializedName("hourly")
    @Expose
    private Hourly[] hourly = null;
    @SerializedName("daily")
    @Expose
    private Daily[] daily = null;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public long getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(long timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Minutely[] getMinutely() {
        return minutely;
    }

    public void setMinutely(Minutely[] minutely) {
        this.minutely = minutely;
    }

    public Hourly[] getHourly() {
        return hourly;
    }

    public void setHourly(Hourly[] hourly) {
        this.hourly = hourly;
    }

    public Daily[] getDaily() {
        return daily;
    }

    public void setDaily(Daily[] daily) {
        this.daily = daily;
    }

    public class Current {

        @SerializedName("dt")
        @Expose
        private long dt;
        @SerializedName("sunrise")
        @Expose
        private long sunrise;
        @SerializedName("sunset")
        @Expose
        private long sunset;
        @SerializedName("temp")
        @Expose
        private double temp;
        @SerializedName("feels_like")
        @Expose
        private double feelsLike;
        @SerializedName("pressure")
        @Expose
        private long pressure;
        @SerializedName("humidity")
        @Expose
        private long humidity;
        @SerializedName("dew_point")
        @Expose
        private double dewPoint;
        @SerializedName("uvi")
        @Expose
        private double uvi;
        @SerializedName("clouds")
        @Expose
        private long clouds;
        @SerializedName("visibility")
        @Expose
        private long visibility;
        @SerializedName("wind_speed")
        @Expose
        private double windSpeed;
        @SerializedName("wind_deg")
        @Expose
        private long windDeg;
        @SerializedName("weather")
        @Expose
        private Weather[] weather = null;

        public long getDt() {
            return dt;
        }

        public void setDt(long dt) {
            this.dt = dt;
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

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getFeelsLike() {
            return feelsLike;
        }

        public void setFeelsLike(double feelsLike) {
            this.feelsLike = feelsLike;
        }

        public long getPressure() {
            return pressure;
        }

        public void setPressure(long pressure) {
            this.pressure = pressure;
        }

        public long getHumidity() {
            return humidity;
        }

        public void setHumidity(long humidity) {
            this.humidity = humidity;
        }

        public double getDewPoint() {
            return dewPoint;
        }

        public void setDewPoint(double dewPoint) {
            this.dewPoint = dewPoint;
        }

        public double getUvi() {
            return uvi;
        }

        public void setUvi(double uvi) {
            this.uvi = uvi;
        }

        public long getClouds() {
            return clouds;
        }

        public void setClouds(long clouds) {
            this.clouds = clouds;
        }

        public long getVisibility() {
            return visibility;
        }

        public void setVisibility(long visibility) {
            this.visibility = visibility;
        }

        public double getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(double windSpeed) {
            this.windSpeed = windSpeed;
        }

        public long getWindDeg() {
            return windDeg;
        }

        public void setWindDeg(long windDeg) {
            this.windDeg = windDeg;
        }

        public Weather[] getWeather() {
            return weather;
        }

        public void setWeather(Weather[] weather) {
            this.weather = weather;
        }

    }

    public class Daily {

        @SerializedName("dt")
        @Expose
        private long dt;
        @SerializedName("sunrise")
        @Expose
        private long sunrise;
        @SerializedName("sunset")
        @Expose
        private long sunset;
        @SerializedName("temp")
        @Expose
        private Temp temp;
        @SerializedName("feels_like")
        @Expose
        private FeelsLike feelsLike;
        @SerializedName("pressure")
        @Expose
        private long pressure;
        @SerializedName("humidity")
        @Expose
        private long humidity;
        @SerializedName("dew_point")
        @Expose
        private double dewPoint;
        @SerializedName("wind_speed")
        @Expose
        private double windSpeed;
        @SerializedName("wind_deg")
        @Expose
        private long windDeg;
        @SerializedName("weather")
        @Expose
        private Weather[] weather = null;
        @SerializedName("clouds")
        @Expose
        private long clouds;
        @SerializedName("rain")
        @Expose
        private double rain;
        @SerializedName("uvi")
        @Expose
        private double uvi;

        public long getDt() {
            return dt;
        }

        public void setDt(long dt) {
            this.dt = dt;
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

        public Temp getTemp() {
            return temp;
        }

        public void setTemp(Temp temp) {
            this.temp = temp;
        }

        public FeelsLike getFeelsLike() {
            return feelsLike;
        }

        public void setFeelsLike(FeelsLike feelsLike) {
            this.feelsLike = feelsLike;
        }

        public long getPressure() {
            return pressure;
        }

        public void setPressure(long pressure) {
            this.pressure = pressure;
        }

        public long getHumidity() {
            return humidity;
        }

        public void setHumidity(long humidity) {
            this.humidity = humidity;
        }

        public double getDewPoint() {
            return dewPoint;
        }

        public void setDewPoint(double dewPoint) {
            this.dewPoint = dewPoint;
        }

        public double getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(double windSpeed) {
            this.windSpeed = windSpeed;
        }

        public long getWindDeg() {
            return windDeg;
        }

        public void setWindDeg(long windDeg) {
            this.windDeg = windDeg;
        }

        public Weather[] getWeather() {
            return weather;
        }

        public void setWeather(Weather[] weather) {
            this.weather = weather;
        }

        public long getClouds() {
            return clouds;
        }

        public void setClouds(long clouds) {
            this.clouds = clouds;
        }

        public double getRain() {
            return rain;
        }

        public void setRain(double rain) {
            this.rain = rain;
        }

        public double getUvi() {
            return uvi;
        }

        public void setUvi(double uvi) {
            this.uvi = uvi;
        }

    }

    public class FeelsLike {

        @SerializedName("day")
        @Expose
        private double day;
        @SerializedName("night")
        @Expose
        private double night;
        @SerializedName("eve")
        @Expose
        private double eve;
        @SerializedName("morn")
        @Expose
        private double morn;

        public double getDay() {
            return day;
        }

        public void setDay(double day) {
            this.day = day;
        }

        public double getNight() {
            return night;
        }

        public void setNight(double night) {
            this.night = night;
        }

        public double getEve() {
            return eve;
        }

        public void setEve(double eve) {
            this.eve = eve;
        }

        public double getMorn() {
            return morn;
        }

        public void setMorn(double morn) {
            this.morn = morn;
        }

    }

    public class Hourly {

        @SerializedName("dt")
        @Expose
        private long dt;
        @SerializedName("temp")
        @Expose
        private double temp;
        @SerializedName("feels_like")
        @Expose
        private double feelsLike;
        @SerializedName("pressure")
        @Expose
        private long pressure;
        @SerializedName("humidity")
        @Expose
        private long humidity;
        @SerializedName("dew_point")
        @Expose
        private double dewPoint;
        @SerializedName("clouds")
        @Expose
        private long clouds;
        @SerializedName("wind_speed")
        @Expose
        private double windSpeed;
        @SerializedName("wind_deg")
        @Expose
        private long windDeg;
        @SerializedName("weather")
        @Expose
        private Weather[] weather = null;
        @SerializedName("rain")
        @Expose
        private Rain rain;

        public long getDt() {
            return dt;
        }

        public void setDt(long dt) {
            this.dt = dt;
        }

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getFeelsLike() {
            return feelsLike;
        }

        public void setFeelsLike(double feelsLike) {
            this.feelsLike = feelsLike;
        }

        public long getPressure() {
            return pressure;
        }

        public void setPressure(long pressure) {
            this.pressure = pressure;
        }

        public long getHumidity() {
            return humidity;
        }

        public void setHumidity(long humidity) {
            this.humidity = humidity;
        }

        public double getDewPoint() {
            return dewPoint;
        }

        public void setDewPoint(double dewPoint) {
            this.dewPoint = dewPoint;
        }

        public long getClouds() {
            return clouds;
        }

        public void setClouds(long clouds) {
            this.clouds = clouds;
        }

        public double getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(double windSpeed) {
            this.windSpeed = windSpeed;
        }

        public long getWindDeg() {
            return windDeg;
        }

        public void setWindDeg(long windDeg) {
            this.windDeg = windDeg;
        }

        public Weather[] getWeather() {
            return weather;
        }

        public void setWeather(Weather[] weather) {
            this.weather = weather;
        }

        public Rain getRain() {
            return rain;
        }

        public void setRain(Rain rain) {
            this.rain = rain;
        }

    }

    public class Minutely {

        @SerializedName("dt")
        @Expose
        private long dt;
        @SerializedName("precipitation")
        @Expose
        private long precipitation;

        public long getDt() {
            return dt;
        }

        public void setDt(long dt) {
            this.dt = dt;
        }

        public long getPrecipitation() {
            return precipitation;
        }

        public void setPrecipitation(long precipitation) {
            this.precipitation = precipitation;
        }

    }

    public class Rain {

        @SerializedName("1h")
        @Expose
        private double _1h;

        public double get1h() {
            return _1h;
        }

        public void set1h(double _1h) {
            this._1h = _1h;
        }

    }

    public class Temp {

        @SerializedName("day")
        @Expose
        private double day;
        @SerializedName("min")
        @Expose
        private double min;
        @SerializedName("max")
        @Expose
        private double max;
        @SerializedName("night")
        @Expose
        private double night;
        @SerializedName("eve")
        @Expose
        private double eve;
        @SerializedName("morn")
        @Expose
        private double morn;

        public double getDay() {
            return day;
        }

        public void setDay(double day) {
            this.day = day;
        }

        public double getMin() {
            return min;
        }

        public void setMin(double min) {
            this.min = min;
        }

        public double getMax() {
            return max;
        }

        public void setMax(double max) {
            this.max = max;
        }

        public double getNight() {
            return night;
        }

        public void setNight(double night) {
            this.night = night;
        }

        public double getEve() {
            return eve;
        }

        public void setEve(double eve) {
            this.eve = eve;
        }

        public double getMorn() {
            return morn;
        }

        public void setMorn(double morn) {
            this.morn = morn;
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

        public int getId() { return id; }

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
}
