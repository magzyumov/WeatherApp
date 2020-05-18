package ru.magzyumov.weatherapp.Database.Location;

import java.util.List;
import java.util.Map;

public interface LocationDataSource {

    // Загружаем местоположения в буфер
    void loadLocations();

    // Получаем количество записей
    long getCountLocations();

    // Получаем количество записей
    // для отображения истории
    long getCountHistoryLocations();

    List<Location> getHistoryLocations();

    // Добавляем местоположение
    void addLocation(Location location);

    // Установим температуру для текущего местоположения
    void setLocationTemperature(String region, String city, float temperature);

    // Установим дату прогноза для текущего местоположения
    void setLocationDate(String region, String city, long date);

    // Заменяем местоположение
    void updateLocation(Location location);

    // Удаляем местоположение из базы
    public void removeLocation(long id);

    // Получаем города для поиска
    List<Map<String, String>> getHashCities();

    // Получаем города для поиска из истории
    List<Map<String, String>> getSearchedLocations();

    // Устанвливаем местоположение текущим
    void setLocationCurrent(String region, String city, boolean needUpdate);
    void setLocationCurrent(Location futureLocation, boolean needUpdate);

    // Устанвливаем местоположение участвующим в истории поиска
    void setLocationSearched(String region, String city, boolean isSearched);
    void setLocationSearched(Location futureLocation, boolean isSearched);

    //Получаем текущее местоположение из базы
    Location getCurrentLocation();
}
