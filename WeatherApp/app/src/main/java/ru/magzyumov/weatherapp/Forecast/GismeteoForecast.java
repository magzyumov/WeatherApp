package ru.magzyumov.weatherapp.Forecast;

public class GismeteoForecast {

    private String kind;                    // Тип погодных данных
    private Date date;                      // Дата и время данных
    private Temperature temperature;        // Температура
    private Description description;        // Описание погоды
    private Humidity humidity;              // Влажность
    private Pressure pressure;              // Атмосферное давление
    private Cloudiness cloudiness;          // Облачность
    private Storm storm;                    // Вероятность грозы
    private Precipitation precipitation;    // Осадки
    private int phenomenon;                 // Код погодного явления
    private String icon;                    // Иконка погоды
    private int gm;                         // Геомагнитное поле
    private Wind wind;                      // Ветер

    // Дата и время данных
    public class Date {
        private String utc;                 // По стандарту UTC
        private int unix;                   // В формате UNIX по стандарту UTC
        private String local;               // По локальному времени географического объекта
        private int time_zone_offset;       // Разница в минутах между локальным временем географического объекта и временем по UTC
    }

    // Температура
    public class Temperature {
        private Air air;                    // Воздух
        private Comfort comfort;            // Комфорт (по ощущению)
        private Water water;                // Вода

        // Воздух
        public class Air {
            private Float C;                // В градусах Цельсия
        }

        // Комфорт (по ощущению)
        public class Comfort {
            private Float C;                // В градусах Цельсия
        }

        // Вода
        public class Water {
            private Float C;                // В градусах Цельсия
        }
    }

    // Описание погоды
    public class Description {
        private String full;                // Полное описание
    }

    // Влажность
    public class Humidity {
        private int percent;                // В процентах
    }

    // Атмосферное давление
    public class Pressure {
        private int mm_hg_atm;              // В мм ртутного столба
    }

    // Облачность
    public class Cloudiness {
        private int percent;                // В процентах
        private int type;                   // По шкале от 0 до 3
    }

    // Гроза
    public class Storm {
        private boolean prediction;         // Вероятность грозы
    }

    //  Осадки
    public class Precipitation {
        private int type;                   // Тип осадков
        private Float amount;               // Количество осадков в мм.
        private int intensity;              // Интенсивность осадков
    }

    // Ветер
    public class Wind {
        private Direction direction;        // Направление
        private Float speed;                // В метрах в секунду

        // Направление
        public class Direction{
            private int scale_8;            // По шкале от 1 до 8
            private int degree;             // В градусах
        }
    }
}
