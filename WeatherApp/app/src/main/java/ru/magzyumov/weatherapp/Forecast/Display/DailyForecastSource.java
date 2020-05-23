package ru.magzyumov.weatherapp.Forecast.Display;

import java.util.ArrayList;
import java.util.List;

public class DailyForecastSource implements DailyForecastDataSource {
    private List<DailyForecast> dataSource;

    public DailyForecastSource(int length){
        setDataSource(new ArrayList<>(length));
    }

    public List<DailyForecast> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<DailyForecast> dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DailyForecast getDailyForecast(int position) {
        return dataSource.get(position);
    }

    @Override
    public int size() {
        return dataSource.size();
    }
}
