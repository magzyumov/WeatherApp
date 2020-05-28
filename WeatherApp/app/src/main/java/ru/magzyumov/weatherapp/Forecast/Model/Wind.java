package ru.magzyumov.weatherapp.Forecast.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
