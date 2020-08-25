package ru.magzyumov.weatherapp.Database.Firebase;

public class Position{
    private String latitude;
    private String longitude;
    private String timeStamp;

    public Position(){
    }

    public Position(String timeStamp, String latitude, String longitude) {
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setTimeStamp(timeStamp);
    }

    public String getLatitude() { return latitude; }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }

    public String getTimeStamp() { return timeStamp; }

    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }
}
