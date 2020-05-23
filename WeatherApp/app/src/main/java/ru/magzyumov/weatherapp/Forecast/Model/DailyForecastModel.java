package ru.magzyumov.weatherapp.Forecast.Model;

public class DailyForecastModel {
    private int cod;
    private int message;
    private int cnt;
    private ForecastList[] list;
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
}
