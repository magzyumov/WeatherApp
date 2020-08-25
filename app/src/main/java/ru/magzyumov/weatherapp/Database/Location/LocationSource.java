package ru.magzyumov.weatherapp.Database.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Вспомогательный класс, развязывающий зависимость между Room и RecyclerView
public class LocationSource implements LocationDataSource {

    private final LocationDao locationDao;

    // Буфер с данными: сюда будем подкачивать данные из БД
    private List<Locations> locations;

    public LocationSource(LocationDao locationDao){
        this.locationDao = locationDao;
        getLocations();
    }

    // Получить все местоположения
    public List<Locations> getLocations(){
        // Если объекты еще не загружены, загружаем их.
        // Это сделано для того, чтобы не делать запросы к БД каждый раз
        if (locations == null){
            loadLocations();
        }
        return locations;
    }

    // Загружаем местоположения в буфер
    public void loadLocations(){
        locations = locationDao.getAllLocations();
    }

    // Получаем количество записей
    public long getCountLocations(){
        return locationDao.getCountLocations();
    }

    // Получаем количество записей
    // для отображения истории
    public long getCountHistoryLocations(){
        return locationDao.getCountHistoryLocations(true);
    }

    public List<Locations> getHistoryLocations(){
        return locationDao.getSearchedLocations(true);
    }

    // Добавляем местоположение
    public void addLocation(Locations location){
        locationDao.insertLocation(location);
        // После изменения БД надо повторно прочесть данные из буфера
        loadLocations();
    }

    // Заменяем местоположение
    public void updateLocation(Locations location){
        locationDao.updateLocation(location);
        loadLocations();
    }

    // Удаляем местоположение из базы
    public void removeLocation(long id){
        locationDao.deteleLocationById(id);
        loadLocations();
    }

    // Получаем города для поиска
    public List<Map<String, String>> getHashCities(){
        List<Map<String, String>> result = new ArrayList<>();
        Map<String, String> city;

        for (Locations location : locations) {
            city = new HashMap<>();
            city.put("Region", location.region);
            city.put("City", location.city);
            result.add(city);
        }
        return result;
    }

    // Получаем города для поиска из истории
    public List<Map<String, String>> getSearchedLocations(){
        List<Map<String, String>> result = new ArrayList<>();
        Map<String, String> city;

        for (Locations location : locations) {
            if(location.isSearched){
                city = new HashMap<>();
                city.put("Region", location.region);
                city.put("City", location.city);
                result.add(city);
            }
        }
        return result;
    }

    // Устанвливаем местоположение текущим
    public void setLocationCurrent(String region, String city, boolean needUpdate){
        // Сначала снимаем флаг текущего города
        // и удаляем прогноз
        // у старого местоположения
        Locations currentLocation = locationDao.getCurrentLocation(true);
        if (currentLocation != null){
            //currentLocation.currentForecast = null;
            //currentLocation.dailyForecast = null;
            currentLocation.isCurrent = false;
            updateLocation(currentLocation);
        }

        // Теперь выставляем флаг у нового
        Locations futureLocation = locationDao.getLocationByCityName(region, city);
        futureLocation.isCurrent = true;
        futureLocation.needUpdate = needUpdate;
        updateLocation(futureLocation);
    }

    public void setLocationCurrent(Locations futureLocation, boolean needUpdate){
        // Сначала снимаем флаг текущего города
        // и удаляем прогноз
        // у старого местоположения
        Locations currentLocation = locationDao.getCurrentLocation(true);
        if (currentLocation != null){
            //currentLocation.currentForecast = null;
            //currentLocation.dailyForecast = null;
            currentLocation.isCurrent = false;
            updateLocation(currentLocation);
        }

        // Теперь выставляем флаг у нового
        futureLocation.isCurrent = true;
        futureLocation.needUpdate = needUpdate;
        updateLocation(futureLocation);
    }

    // Устанвливаем местоположение
    // участвующим в истории поиска
    public void setLocationSearched(String region, String city, boolean isSearched){
        Locations searchedLocation = locationDao.getLocationByCityName(region, city);
        searchedLocation.isSearched = isSearched;
        updateLocation(searchedLocation);
    }

    public void setLocationSearched(Locations futureLocation, boolean isSearched){
        futureLocation.isSearched = isSearched;
        updateLocation(futureLocation);
    }

    //Получаем текущее местоположение из базы
    public Locations getCurrentLocation(){
        return locationDao.getCurrentLocation(true);
    }

    //Получаем локацию по id
    public Locations getLocationById(long id){
        return locationDao.getLocationById(id);
    }
}