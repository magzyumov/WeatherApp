package ru.magzyumov.weatherapp.room.database.Location;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.TreeMap;

import ru.magzyumov.weatherapp.R;

// Адаптер для RecyclerView
public class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationRecyclerAdapter.ViewHolder> {

    private Activity activity;
    // Источник данных
    private LocationSource dataSource;
    // Позиция в списке, на которой было нажато меню
    private long menuPosition;

    public LocationRecyclerAdapter(LocationSource dataSource, Activity activity){
        this.dataSource = dataSource;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Заполняем данными записи на экране
        List<Location> locations = dataSource.getHistoryLocations();
        Location location = locations.get(position);
        holder.textViewDate.setText(location.city); //Пока без даты
        holder.textViewCity.setText(location.city);
        holder.imageViewWeather.setImageResource(R.drawable.bkn_d_light);
        holder.textViewTemp.setText(location.city); //Пока без температуры
        holder.textViewRegion.setText(location.region);

        // Тут определяем, какой пункт меню был нажат
        holder.cardView.setOnLongClickListener(view -> {
            menuPosition = position;
            return false;
        });

        // Регистрируем контекстное меню
        if (activity != null){
            activity.registerForContextMenu(holder.cardView);
        }
    }

    @Override
    public int getItemCount() {
        return (int) dataSource.getCountHistoryLocations();
    }

    public long getMenuPosition() {
        return menuPosition;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate;
        TextView textViewCity;
        ImageView imageViewWeather;
        TextView textViewTemp;
        TextView textViewRegion;
        View cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView;
            textViewDate = cardView.findViewById(R.id.textViewDate);
            textViewCity = cardView.findViewById(R.id.textViewCity);
            imageViewWeather = cardView.findViewById(R.id.imageViewWeather);
            textViewTemp = cardView.findViewById(R.id.textViewTemp);
            textViewRegion = cardView.findViewById(R.id.textViewRegion);
        }
    }
}

