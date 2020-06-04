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

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> implements Constants {
    private ForecastDataSource dataSource;

    public ForecastAdapter(ForecastDataSource dataSource){
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public ForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastAdapter.ViewHolder viewHolder, int position) {
        Forecast forecast = dataSource.getForecast(position);
        viewHolder.setData(forecast.getDate(), forecast.getImage(),
                forecast.getTemp(), forecast.getTempEU(),
                forecast.getWindSpeed(), forecast.getWindSpeedEU(),
                forecast.getPressure(), forecast.getPressureEU(),
                forecast.getHumidity());
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private PicassoLoader picassoLoader;
        private TextView textViewDate;
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
            picassoLoader = new PicassoLoader();
            textViewDate = itemView.findViewById(R.id.textViewDate);
            imageViewWeather = itemView.findViewById(R.id.imageViewWeather);
            textViewTemp = itemView.findViewById(R.id.textViewTemp);
            textViewTempEU = itemView.findViewById(R.id.textViewTempEU);
            textViewPressure = itemView.findViewById(R.id.textViewPressure);
            textViewPressureEU = itemView.findViewById(R.id.textViewPressureEU);
            textViewWindSpeed = itemView.findViewById(R.id.textViewWindSpeed);
            textViewWindSpeedEU = itemView.findViewById(R.id.textViewWindSpeedEU);
            textViewHumidity = itemView.findViewById(R.id.textViewHumidity);
        }

        public void setData(String date, String picture, String temp,
                            String tempEU, String windSpeed, String windSpeedEU,
                            String pressure, String pressureEU, String humidity){

            getTextViewDate().setText(date);
            picassoLoader.load(picture, getImageViewWeather());
            getTextViewTemp().setText(temp);
            getTextViewTempEU().setText(tempEU);
            getTextViewWindSpeed().setText(windSpeed);
            getTextViewWindSpeedEU().setText(windSpeedEU);
            getTextViewPressure().setText(pressure);
            getTextViewPressureEU().setText(pressureEU);
            getTextViewHumidity().setText(humidity);
        }

        public TextView getTextViewDate() { return textViewDate; }

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
