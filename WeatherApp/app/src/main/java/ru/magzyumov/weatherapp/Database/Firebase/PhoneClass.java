package ru.magzyumov.weatherapp.Database.Firebase;

public class PhoneClass {
    private String id;
    private String timeStamp;
    private String osVersion;
    private String apiLevel;
    private String name;
    private String token;
    private String batteryLevel;
    private String isCharging;
    private String usbCharge;
    private String acCharge;
    private String latitude;
    private String longitude;

    public PhoneClass() {
    }

    public PhoneClass(String timeStamp, String latitude, String longitude) {
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setTimeStamp(timeStamp);
    }

    public PhoneClass(String id, String timeStamp, String osVersion,
                      String apiLevel, String name, String token) {
        this.setId(id);
        this.setTimeStamp(timeStamp);
        this.setOsVersion(osVersion);
        this.setApiLevel(apiLevel);
        this.setName(name);
        this.setToken(token);
    }

    public PhoneClass(String timeStamp, boolean isCharging, boolean usbCharge,
                      boolean acCharge, String batteryLevel) {
        this.setTimeStamp(timeStamp);
        this.setIsCharging(isCharging);
        this.setUsbCharge(usbCharge);
        this.setAcCharge(acCharge);
        this.setBatteryLevel(batteryLevel);
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getTimeStamp() { return timeStamp; }

    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }

    public String getOsVersion() { return osVersion; }

    public void setOsVersion(String osVersion) { this.osVersion = osVersion; }

    public String getApiLevel() { return apiLevel; }

    public void setApiLevel(String apiLevel) { this.apiLevel = apiLevel; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(String batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getIsCharging() {
        return isCharging;
    }

    public void setIsCharging(boolean isCharging) {
        this.isCharging = String.valueOf(isCharging);
    }

    public String getUsbCharge() {
        return usbCharge;
    }

    public void setUsbCharge(boolean usbCharge) {
        this.usbCharge = String.valueOf(usbCharge);
    }

    public String getAcCharge() {
        return acCharge;
    }

    public void setAcCharge(boolean acCharge) {
        this.acCharge = String.valueOf(acCharge);
    }

    public String getLatitude() { return latitude; }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }
}