package ru.magzyumov.weatherapp;

import java.util.Random;

public final class MainPresenter {

    //Набор типов полей
    public enum Field {
        CURRENT_TEMP,
        SETTING_NIGHT_MODE,
        SETTING_TEMP_EU,
        SETTING_WIND_EU,
        SETTING_PRESS_EU,
        SETTING_NOTICE
    }

    //Внутреннее поле, будет хранить единственный экземпляр
    private static MainPresenter instance = null;

    // Поле для синхронизации
    private static final Object syncObj = new Object();

    //Поля для хранения
    private int currentTemp;           // Поле для хранения температуры
    private boolean settingNightMode;  //Поле для хранения темного режима
    private boolean settingTempEu;     //Поле для хранения единиц измерения температуры
    private boolean settingWindEU;     //Поле для хранения единиц измерения силы ветра
    private boolean settingPressEU;    //Поле для хранения единиц измерения давления
    private boolean settingNotice;     //Поле для настроек уведомления

    // Конструктор (вызывать извне его нельзя, поэтому он приватный)
    private MainPresenter(){
        currentTemp = 56;
        settingNightMode = false;
        settingTempEu = true;
        settingWindEU = false;
        settingPressEU = false;
        settingNotice = true;
    }

    // Генерируем температуру
    public void generateCurrentTemp(){
        Random random = new Random();
        currentTemp = random.nextInt(50);
    }

    public int getCurrentTemp(){ return currentTemp; }

    public void setSwitch(boolean switchPosition, Field field){
        switch (field) {
            case SETTING_NIGHT_MODE:
                setSettingNightMode(switchPosition);
                break;
            case SETTING_TEMP_EU:
                setSettingTempEu(switchPosition);
                break;
            case SETTING_WIND_EU:
                setSettingWindEU(switchPosition);
                break;
            case SETTING_PRESS_EU:
                setSettingPressEU(switchPosition);
                break;
            case SETTING_NOTICE:
                setSettingNotice(switchPosition);
                break;
            default:
                break;
        }
    }

    public boolean getSwitch(Field field){
        switch (field) {
            case SETTING_NIGHT_MODE:
                return getSettingNightMode();
            case SETTING_TEMP_EU:
                return getSettingTempEu();
            case SETTING_WIND_EU:
                return getSettingWindEU();
            case SETTING_PRESS_EU:
                return getSettingPressEU();
            case SETTING_NOTICE:
                return getSettingNotice();
            default:
                return false;
        }
    }

    private boolean getSettingNightMode(){ return settingNightMode; }
    private void setSettingNightMode(boolean settingNightMode){ this.settingNightMode = settingNightMode; }

    private boolean getSettingTempEu(){ return settingTempEu; }
    private void setSettingTempEu(boolean settingTempEu){ this.settingTempEu = settingTempEu; }

    private boolean getSettingWindEU(){ return settingWindEU; }
    private void setSettingWindEU(boolean settingWindEU){ this.settingWindEU = settingWindEU; }

    private boolean getSettingPressEU(){ return settingPressEU; }
    private void setSettingPressEU(boolean settingPressEU){ this.settingPressEU = settingPressEU; }

    private boolean getSettingNotice(){ return settingNotice; }
    private void setSettingNotice(boolean settingNotice){ this.settingNotice = settingNotice; }

    // Метод, который возвращает экземпляр объекта.
    // Если объекта нет, то создаем его.
    public static MainPresenter getInstance(){
        // Здесь реализована «ленивая» инициализация объекта,
        // то есть, пока объект не нужен, не создаем его.
        synchronized (syncObj) {
            if (instance == null) {
                instance = new MainPresenter();
            }
            return instance;
        }
    }
}
