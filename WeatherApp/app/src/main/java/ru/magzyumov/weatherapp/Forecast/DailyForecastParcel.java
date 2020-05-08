package ru.magzyumov.weatherapp.Forecast;

import android.os.Parcel;
import android.os.Parcelable;


import ru.magzyumov.weatherapp.Forecast.Model.City;
import ru.magzyumov.weatherapp.Forecast.Model.DailyForecastModel;
import ru.magzyumov.weatherapp.Forecast.Model.List;
import ru.magzyumov.weatherapp.Forecast.Model.MainDaily;
import ru.magzyumov.weatherapp.Forecast.Model.WindDaily;

public class DailyForecastParcel extends DailyForecastModel implements Parcelable {
    private int cod;
    private int message;
    private int cnt;
    private List[] list;
    private City city;

    public DailyForecastParcel() {

    }

    public DailyForecastParcel(Parcel in) {
        super();
        setCod(in.readInt());
        message = in.readInt();
        cnt = in.readInt();
    }

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

    public List[] getList() {
        return list;
    }

    public void setList(List[] list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cod);
        dest.writeInt(message);
        dest.writeInt(cnt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DailyForecastParcel> CREATOR = new Creator<DailyForecastParcel>() {
        @Override
        public DailyForecastParcel createFromParcel(Parcel in) {
            return new DailyForecastParcel(in);
        }

        @Override
        public DailyForecastParcel[] newArray(int size) {
            return new DailyForecastParcel[size];
        }
    };
}
