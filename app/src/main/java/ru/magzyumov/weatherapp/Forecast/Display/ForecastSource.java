package ru.magzyumov.weatherapp.Forecast.Display;

import java.util.ArrayList;
import java.util.List;

public class ForecastSource implements ForecastDataSource {
    private List<Forecast> dataSource;

    public ForecastSource(int length){
        setDataSource(new ArrayList<>(length));
    }

    public List<Forecast> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<Forecast> dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Forecast getForecast(int position) {
        return dataSource.get(position);
    }

    @Override
    public int size() {
        return dataSource.size();
    }
}
