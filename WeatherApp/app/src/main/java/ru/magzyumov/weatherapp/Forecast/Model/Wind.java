package ru.magzyumov.weatherapp.Forecast.Model;

public class Wind {
    private float speed;
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
