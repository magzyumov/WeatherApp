package ru.magzyumov.weatherapp.Database.Firebase;

public class Installation{
    private String id;
    private String timeStamp;
    private String osVersion;
    private String apiLevel;
    private String name;
    private String token;

    public Installation(){}

    public Installation(String id, String timeStamp, String osVersion,
                        String apiLevel, String name, String token) {
        this.setId(id);
        this.setTimeStamp(timeStamp);
        this.setOsVersion(osVersion);
        this.setApiLevel(apiLevel);
        this.setName(name);
        this.setToken(token);
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

}
