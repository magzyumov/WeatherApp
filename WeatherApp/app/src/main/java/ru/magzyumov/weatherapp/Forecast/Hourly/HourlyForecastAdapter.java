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
    private HourlyForecastDataSource dataSource;

    public HourlyForecastAdapter(HourlyForecastDataSource dataSource){
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public HourlyForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_forecast_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyForecastAdapter.ViewHolder viewHolder, int position) {
        HourlyForecast hourlyForecast = dataSource.getHourlyForecast(position);
        viewHolder.setData(hourlyForecast.getTime(), hourlyForecast.getImage(), hourlyForecast.getTemp());
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTime;
        private ImageView imageViewWeather;
        private TextView textViewTemp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            imageViewWeather = itemView.findViewById(R.id.imageViewWeather);
            textViewTemp = itemView.findViewById(R.id.textViewTemp);
        }

        public void setData(String time, int picture, String temp){
            getTextViewTime().setText(time);
            getImageViewWeather().setImageResource(picture);
            getTextViewTemp().setText(temp);
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