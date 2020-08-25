package ru.magzyumov.weatherapp.Database.Firebase;

public class Battery{
    private String timeStamp;
    private String batteryLevel;
    private String isCharging;
    private String usbCharge;
    private String acCharge;

    public Battery(){}

    public Battery(String timeStamp, boolean isCharging, boolean usbCharge,
                   boolean acCharge, String batteryLevel) {
        this.setTimeStamp(timeStamp);
        this.setIsCharging(isCharging);
        this.setUsbCharge(usbCharge);
        this.setAcCharge(acCharge);
        this.setBatteryLevel(batteryLevel);
    }

    public String getTimeStamp() { return timeStamp; }

    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }

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
}
