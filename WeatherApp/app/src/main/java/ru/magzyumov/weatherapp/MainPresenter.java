package ru.magzyumov.weatherapp;

import java.util.Random;

public final class MainPresenter {
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

    public boolean getSettingNightMode(){ return settingNightMode; }
    public void setSettingNightMode(boolean settingNightMode){ this.settingNightMode = settingNightMode; }

    public boolean getSettingTempEu(){ return settingTempEu; }
    public void setSettingTempEu(boolean settingTempEu){ this.settingTempEu = settingTempEu; }

    public boolean getSettingWindEU(){ return settingWindEU; }
    public void setSettingWindEU(boolean settingWindEU){ this.settingWindEU = settingWindEU; }

    public boolean getSettingPressEU(){ return settingPressEU; }
    public void setSettingPressEU(boolean settingPressEU){ this.settingPressEU = settingPressEU; }

    public boolean getSettingNotice(){ return settingNotice; }
    public void setSettingNotice(boolean settingNotice){ this.settingNotice = settingNotice; }

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
