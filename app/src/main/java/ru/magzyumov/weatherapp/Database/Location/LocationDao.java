package ru.magzyumov.weatherapp.Database.Location;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// Описание объекта, обрабатывающего данные
// @Dao - доступ к данным
// В этом классе описывается, как будет происходить обработка данных
@Dao
public interface LocationDao {

    // Метод для добавления города в базу данных
    // @Insert - признак добавления
    // onConflict - что делать, если такая запись уже есть
    // В данном случае просто заменим её
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocation(Locations location);

    // Метод для замены данных местоположения
    @Update
    void updateLocation(Locations location);

    // Удаляем данные местоположения
    @Delete
    void deleteLocation(Locations location);

    // Удаляем данные местоположения, зная ключ
    @Query("DELETE FROM Locations WHERE id = :id")
    void deteleLocationById(long id);

    // Забираем данные по всем местоположениям
    @Query("SELECT * FROM Locations")
    List<Locations> getAllLocations();

    // Получаем данные местоположения по id
    @Query("SELECT * FROM Locations WHERE id = :id")
    Locations getLocationById(long id);

    // Получаем данные местоположения по
    // по названию города и региона
    @Query("SELECT * FROM Locations WHERE region = :region AND city = :city")
    Locations getLocationByCityName(String region, String city);

    // Получаем текущее местоположение
    @Query("SELECT * FROM Locations WHERE isCurrent = :isCurrent")
    Locations getCurrentLocation(boolean isCurrent);

    // Забираем местоположения которые были в поиске
    @Query("SELECT * FROM Locations WHERE isSearched = :isSearched")
    List<Locations> getSearchedLocations(boolean isSearched);

    //Получаем количество записей в таблице
    @Query("SELECT COUNT() FROM Locations")
    long getCountLocations();

    //Получаем количество записей в таблице
    @Query("SELECT COUNT() FROM Locations WHERE isSearched = :isSearched")
    long getCountHistoryLocations(boolean isSearched);

    //Вставляем кучу даннх
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBigData(List<Locations> data);
}
