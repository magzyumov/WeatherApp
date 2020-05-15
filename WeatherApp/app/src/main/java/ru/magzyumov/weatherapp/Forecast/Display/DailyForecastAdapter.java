package ru.magzyumov.weatherapp.Forecast.Display;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.R;

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.ViewHolder> implements Constants {
    private DailyForecastDataSource dataSource;
    private OnItemClickListener itemClickListener;

    public DailyForecastAdapter(DailyForecastDataSource dataSource){
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public DailyForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        if (itemClickListener != null) {
            viewHolder.setOnClickListener(itemClickListener);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DailyForecastAdapter.ViewHolder viewHolder, int position) {
        DailyForecast dailyForecast = dataSource.getDailyForecast(position);
        viewHolder.setData(dailyForecast.getDate(), dailyForecast.getDayName(), dailyForecast.getImage(),
                dailyForecast.getTemp(), dailyForecast.getTempEU(),
                dailyForecast.getWindSpeed(), dailyForecast.getWindSpeedEU(),
                dailyForecast.getPressure(), dailyForecast.getPressureEU(),
                dailyForecast.getHumidity());
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    //Интерфейс для обработки нажатий как в ListView
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    //Сеттер слушателя нажатий
    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDate;
        private TextView textViewDayName;
        private ImageView imageViewWeather;
        private TextView textViewTemp;
        private TextView textViewTempEU;
        private TextView textViewPressure;
        private TextView textViewPressureEU;
        private TextView textViewWindSpeed;
        private TextView textViewWindSpeedEU;
        private TextView textViewHumidity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewDayName = itemView.findViewById(R.id.textViewDayName);
            imageViewWeather = itemView.findViewById(R.id.imageViewWeather);
            textViewTemp = itemView.findViewById(R.id.textViewTemp);
            textViewTempEU = itemView.findViewById(R.id.textViewTempEU);
            textViewPressure = itemView.findViewById(R.id.textViewPressure);
            textViewPressureEU = itemView.findViewById(R.id.textViewPressureEU);
            textViewWindSpeed = itemView.findViewById(R.id.textViewWindSpeed);
            textViewWindSpeedEU = itemView.findViewById(R.id.textViewWindSpeedEU);
            textViewHumidity = itemView.findViewById(R.id.textViewHumidity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener != null){
                        itemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }

        public void setOnClickListener(final OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Получаем позицию адаптера
                    int adapterPosition = getAdapterPosition();
                    // Проверяем ее на корректность
                    if (adapterPosition == RecyclerView.NO_POSITION) return;
                    listener.onItemClick(v, adapterPosition);
                }
            });
        }

        public void setData(String date, String dayName, int picture, String temp,
                            String tempEU, String windSpeed, String windSpeedEU,
                            String pressure, String pressureEU, String humidity){

            getTextViewDate().setText(date);
            getTextViewDayName().setText(dayName);
            getImageViewWeather().setImageResource(picture);
            getTextViewTemp().setText(temp);
            getTextViewTempEU().setText(tempEU);
            getTextViewWindSpeed().setText(windSpeed);
            getTextViewWindSpeedEU().setText(windSpeedEU);
            getTextViewPressure().setText(pressure);
            getTextViewPressureEU().setText(pressureEU);
            getTextViewHumidity().setText(humidity);
        }

        public TextView getTextViewDate() { return textViewDate; }

        public TextView getTextViewDayName() { return textViewDayName; }

        public ImageView getImageViewWeather() { return imageViewWeather; }

        public TextView getTextViewTemp() { return textViewTemp; }

        public TextView getTextViewTempEU() { return textViewTempEU; }

        public TextView getTextViewWindSpeed() { return textViewWindSpeed; }

        public TextView getTextViewWindSpeedEU() { return textViewWindSpeedEU; }

        public TextView getTextViewPressure() { return textViewPressure; }

        public TextView getTextViewPressureEU() { return textViewPressureEU; }

        public TextView getTextViewHumidity() { return textViewHumidity; }
    }
}
