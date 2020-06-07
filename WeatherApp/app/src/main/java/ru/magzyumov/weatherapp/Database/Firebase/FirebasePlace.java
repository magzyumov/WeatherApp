package ru.magzyumov.weatherapp.Database.Firebase;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class FirebasePlace {
    private String id;
    private String name;
    private String address;
    private String latitude;
    private String longitude;

    public FirebasePlace() {
    }

    public FirebasePlace(String id, String name, String address, String latitude, String longitude) {
        this.setId(id);
        this.setName(name);
        this.setAddress(address);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
