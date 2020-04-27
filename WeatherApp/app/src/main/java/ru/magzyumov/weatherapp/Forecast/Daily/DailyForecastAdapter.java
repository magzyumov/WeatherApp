package ru.magzyumov.weatherapp.Forecast.Daily;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.magzyumov.weatherapp.R;

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.ViewHolder> {
    private DailyForecastDataSource dataSource;
    private OnItemClickListener itemClickListener;

    public DailyForecastAdapter(DailyForecastDataSource dataSource){
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public DailyForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_forecast_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        if (itemClickListener != null) {
            viewHolder.setOnClickListener(itemClickListener);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DailyForecastAdapter.ViewHolder viewHolder, int position) {
        DailyForecast dailyForecast = dataSource.getDailyForecast(position);
        viewHolder.setData(dailyForecast.getDate(),dailyForecast.getDayName(),dailyForecast.getImage(),dailyForecast.getTempDay(),dailyForecast.getTempNight());
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
    public void SetOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDate;
        private TextView textViewDayName;
        private ImageView imageViewWeather;
        private TextView textViewTempDay;
        private TextView textViewTempNight;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewDayName = itemView.findViewById(R.id.textViewDayName);
            imageViewWeather = itemView.findViewById(R.id.imageViewWeather);
            textViewTempDay = itemView.findViewById(R.id.textViewTempDay);
            textViewTempNight = itemView.findViewById(R.id.textViewTempNight);

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

        public void setData(String date, String dayName, int picture, String tempDay, String tempNight){
            getTextViewDate().setText(date);
            getTextViewText().setText(dayName);
            getImageViewWeather().setImageResource(picture);
            getTextViewTempDay().setText(tempDay);
            getTextViewTempNight().setText(tempNight);
        }

        public TextView getTextViewDate() { return textViewDate; }

        public TextView getTextViewText() { return textViewDayName; }

        public ImageView getImageViewWeather() { return imageViewWeather; }

        public TextView getTextViewTempDay() { return textViewTempDay; }

        public TextView getTextViewTempNight() { return textViewTempNight; }
    }
}
