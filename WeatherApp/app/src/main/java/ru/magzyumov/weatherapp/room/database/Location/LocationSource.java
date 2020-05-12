package ru.magzyumov.weatherapp.room.database.Location;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Вспомогательный класс, развязывающий зависимость между Room и RecyclerView
public class LocationSource {

    private final LocationDao locationDao;

    // Буфер с данными: сюда будем подкачивать данные из БД
    private List<Location> locations;

    public LocationSource(LocationDao locationDao){
        this.locationDao = locationDao;
        getLocations();
    }

    // Получить все местоположения
    public List<Location> getLocations(){
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

    // Добавляем местоположение
    public void addLocation(Location location){
        locationDao.insertLocation(location);
        // После изменения БД надо повторно прочесть данные из буфера
        loadLocations();
    }

    // Заменяем местоположение
    public void updateLocation(Location location){
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

        for (Location location : locations) {
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

        for (Location location : locations) {
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
    public void setLocationCurrent(String region, String city){
        // Сначала снимаем флаг текущего города
        // и удаляем прогноз
        // у старого местоположения
        for (Location location : locations) {
            if(location.isCurrent){
                location.forecast = null;
                location.isCurrent = false;
                updateLocation(location);
                break;
            }
        }
        // Теперь выставляем флаг у нового
        for (Location location : locations) {
            if(location.region.equals(region) & location.city.equals(city)){
                location.isCurrent = true;
                updateLocation(location);
                break;
            }
        }
    }

    // Устанвливаем местоположение
    // участвующим в истории поиска
    public void setLocationSearched(String region, String city){
        for (Location location : locations) {
            if(location.region.equals(region) & location.city.equals(city)){
                location.isSearched = true;
                updateLocation(location);
                break;
            }
        }
    }

    // Удаляем местоположение
    // из истории поиска
    public void delLocationFromSearch(String region, String city){
        for (Location location : locations) {
            if(location.region.equals(region) & location.city.equals(city)){
                location.isSearched = false;
                updateLocation(location);
                break;
            }
        }
    }

    //Получаем текущее местоположение из базы
    public Location getCurrentLocation(){
        Location result = new Location();
        for (Location location : locations) {
            if(location.isCurrent){
                result = location;
                break;
            }
        }
        return result;
    }

    //Добавляем множественные данные
    public void insertBigData(List<Location> data){
        locationDao.insertBigData(data);
    }
}