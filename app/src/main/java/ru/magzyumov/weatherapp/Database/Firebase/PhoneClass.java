package ru.magzyumov.weatherapp.Database.Firebase;

public class PhoneClass {
    private Position position;
    private Installation installation;
    private Battery battery;

    public PhoneClass() {}

    public PhoneClass(String id, String timeStamp, String osVersion,
                      String apiLevel, String name, String token) {
        Installation installation = new Installation(id, timeStamp, osVersion,
                apiLevel, name, token);
        this.setInstallation(installation);
    }

    public PhoneClass(String timeStamp, boolean isCharging, boolean usbCharge,
                      boolean acCharge, String batteryLevel) {
        Battery battery = new Battery(timeStamp, isCharging, usbCharge, acCharge, batteryLevel);
        this.setBattery(battery);
    }

    public PhoneClass(String timeStamp, String latitude, String longitude) {
        Position position = new Position(timeStamp, latitude, longitude);
        this.setPosition(position);
    }

    public Position getPosition() { return position; }

    public void setPosition(Position position) { this.position = position; }

    public Installation getInstallation() { return installation; }

    public void setInstallation(Installation installation) { this.installation = installation;}

    public Battery getBattery() { return battery; }

    public void setBattery(Battery battery) { this.battery = battery; }
}