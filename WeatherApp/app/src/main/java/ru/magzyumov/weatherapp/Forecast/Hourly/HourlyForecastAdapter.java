package ru.magzyumov.weatherapp.Forecast.Hourly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.magzyumov.weatherapp.R;

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder> {

    private final int TYPE_STATIC_FORECAST = 0;
    private final int TYPE_DYNAMIC_FORECAST = 1;
    private HourlyForecastDataSource dataSource;

    public HourlyForecastAdapter(HourlyForecastDataSource dataSource){
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public HourlyForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case TYPE_STATIC_FORECAST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_forecast_static_item, parent, false);
                break;
            case TYPE_DYNAMIC_FORECAST:
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_forecast_item, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        // условие для определения айтем какого типа выводить в конкретной позиции
        if (position == 0) return TYPE_STATIC_FORECAST;
        return TYPE_DYNAMIC_FORECAST;
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyForecastAdapter.ViewHolder viewHolder, int position) {
        // Получаем тип айтема в данной позиции для заполнения его данными
        int type = getItemViewType(position);
        HourlyForecast hourlyForecast = dataSource.getHourlyForecast(position);

        switch (type) {
            case TYPE_STATIC_FORECAST:
                viewHolder.setData(hourlyForecast.getWindSpeed(), hourlyForecast.getPressure(), hourlyForecast.getHumidity());
                break;
            case TYPE_DYNAMIC_FORECAST:
            default:
                viewHolder.setData(hourlyForecast.getTime(), hourlyForecast.getImage(), hourlyForecast.getTemp());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewWindSpeed;
        private TextView textViewPressure;
        private TextView textViewHumidity;
        private TextView textViewTime;
        private ImageView imageViewWeather;
        private TextView textViewTemp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWindSpeed = itemView.findViewById(R.id.textViewWindSpeed);
            textViewPressure = itemView.findViewById(R.id.textViewPressure);
            textViewHumidity = itemView.findViewById(R.id.textViewHumidity);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            imageViewWeather = itemView.findViewById(R.id.imageViewWeather);
            textViewTemp = itemView.findViewById(R.id.textViewTemp);
        }

        public void setData(String windSpeed, String pressure, String humidity){
            getTextViewWindSpeed().setText(windSpeed);
            getTextViewPressure().setText(pressure);
            getTextViewHumidity().setText(humidity);
        }

        public void setData(String time, int picture, String temp){
            getTextViewTime().setText(time);
            getImageViewWeather().setImageResource(picture);
            getTextViewTemp().setText(temp);
        }

        //Определение элементов для статичной view
        private TextView getTextViewWindSpeed(){
            return textViewWindSpeed;
        }

        private TextView getTextViewPressure(){
            return textViewPressure;
        }

        private TextView getTextViewHumidity(){
            return textViewHumidity;
        }

        //Определение элементов для основной view
        private TextView getTextViewTime() {
            return textViewTime;
        }

        private ImageView getImageViewWeather() {
            return imageViewWeather;
        }

        private TextView getTextViewTemp() {
            return textViewTemp;
        }
    }
}