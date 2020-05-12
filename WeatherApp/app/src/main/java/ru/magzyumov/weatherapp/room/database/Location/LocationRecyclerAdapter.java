package ru.magzyumov.weatherapp.room.database.Location;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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
        holder.locationRegion.setText(location.region);
        holder.locationCity.setText(location.city);

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
        TextView locationRegion;
        TextView locationCity;
        View cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView;
            locationRegion = cardView.findViewById(R.id.textViewRegion);
            locationCity = cardView.findViewById(R.id.textViewCity);
        }
    }
}

