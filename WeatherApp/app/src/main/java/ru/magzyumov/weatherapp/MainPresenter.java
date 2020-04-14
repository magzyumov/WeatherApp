package ru.magzyumov.weatherapp;

public final class MainPresenter {
    //Внутреннее поле, будет хранить единственный экземпляр
    private static MainPresenter instance = null;

    // Поле для синхронизации
    private static final Object syncObj = new Object();

    // Это наш счетчик
    private int counter;

    // Конструктор (вызывать извне его нельзя, поэтому он приватный)
    private MainPresenter(){
        counter = 0;
    }

    // Увеличение счетчика
    public void incrementCounter(){
        counter++;
    }

    public int getCounter(){
        return counter;
    }

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
