package ru.magzyumov.weatherapp.Forecast.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DailyForecastModel {

    @SerializedName("cod")
    @Expose
    private int cod;

    @SerializedName("message")
    @Expose
    private int message;

    @SerializedName("cnt")
    @Expose
    private int cnt;

    @SerializedName("list")
    @Expose
    private ForecastList[] list;

    @SerializedName("city")
    @Expose
    private City city;

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public ForecastList[] getList() {
        return list;
    }

    public void setList(ForecastList[] list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public DailyForecastModel(){
        this.list = new ForecastList[5];
    }

    public class ForecastList {

        @SerializedName("dt")
        @Expose
        private long dt;

        @SerializedName("main")
        @Expose
        private Main main;

        @SerializedName("weather")
        @Expose
        private Weather[] weather;

        @SerializedName("clouds")
        @Expose
        private Clouds clouds;

        @SerializedName("wind")
        @Expose
        private Wind wind;

        @SerializedName("sys")
        @Expose
        private Sys sys;

        @SerializedName("dt_txt")
        @Expose
        private String dt_txt;

        public long getDt() { return this.dt; }

        public void setDt(long dt) { this.dt = dt; }

        public Main getMain() { return main; }

        public void setMain(Main main) { this.main = main; }

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

        public Sys getSys() {
            return sys;
        }

        public void setSys(Sys sys) {
            this.sys = sys;
        }

        public String getDt_txt() {
            return dt_txt;
        }

        public void setDt_txt(String dt_txt) {
            this.dt_txt = dt_txt;
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

            @SerializedName("sea_level")
            @Expose
            private float sea_level;

            @SerializedName("grnd_level")
            @Expose
            private float grnd_level;

            @SerializedName("humidity")
            @Expose
            private int humidity;

            @SerializedName("temp_kf")
            @Expose
            private float temp_kf;

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

            public float getSea_level() {
                return sea_level;
            }

            public void setSea_level(float sea_level) {
                this.sea_level = sea_level;
            }

            public float getGrnd_level() {
                return grnd_level;
            }

            public void setGrnd_level(float grnd_level) {
                this.grnd_level = grnd_level;
            }

            public int getHumidity() {
                return humidity;
            }

            public void setHumidity(int humidity) {
                this.humidity = humidity;
            }

            public float getTemp_kf() {
                return temp_kf;
            }

            public void setTemp_kf(float temp_kf) {
                this.temp_kf = temp_kf;
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

        public class Sys {

            @SerializedName("pod")
            @Expose
            private String pod;

            public String getPod() {
                return pod;
            }

            public void setPod(String pod) {
                this.pod = pod;
            }
        }

    }

    public class City {

        @SerializedName("id")
        @Expose
        private int id;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("coord")
        @Expose
        private Coord coord;

        @SerializedName("country")
        @Expose
        private String country;

        @SerializedName("timezone")
        @Expose
        private int timezone;

        @SerializedName("sunrise")
        @Expose
        private long sunrise;

        @SerializedName("sunset")
        @Expose
        private long sunset;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Coord getCoord() {
            return coord;
        }

        public void setCoord(Coord coord) {
            this.coord = coord;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public int getTimezone() {
            return timezone;
        }

        public void setTimezone(int timezone) {
            this.timezone = timezone;
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

        public class Coord {

            @SerializedName("lat")
            @Expose
            private float lat;

            @SerializedName("lon")
            @Expose
            private float lon;

            public float getLon() { return lon; }

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
    }
}
