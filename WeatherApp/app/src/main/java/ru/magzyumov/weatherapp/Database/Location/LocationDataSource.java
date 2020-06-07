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

    List<Locations> getHistoryLocations();

    // Добавляем местоположение
    void addLocation(Locations location);

    // Заменяем местоположение
    void updateLocation(Locations location);

    // Удаляем местоположение из базы
    public void removeLocation(long id);

    // Получаем города для поиска
    List<Map<String, String>> getHashCities();

    // Получаем города для поиска из истории
    List<Map<String, String>> getSearchedLocations();

    // Устанвливаем местоположение текущим
    void setLocationCurrent(String region, String city, boolean needUpdate);
    void setLocationCurrent(Locations futureLocation, boolean needUpdate);

    // Устанвливаем местоположение участвующим в истории поиска
    void setLocationSearched(String region, String city, boolean isSearched);
    void setLocationSearched(Locations futureLocation, boolean isSearched);

    //Получаем текущее местоположение из базы
    Locations getCurrentLocation();

    //Получаем локацию по id
    Locations getLocationById(long id);
}
