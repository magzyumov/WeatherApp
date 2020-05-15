package ru.magzyumov.weatherapp.Database.Location;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ru.magzyumov.weatherapp.R;

// Адаптер для RecyclerView
public class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationRecyclerAdapter.ViewHolder> {

    private Fragment fragment;
    // Источник данных
    private LocationSource dataSource;
    // Позиция в списке, на которой было нажато меню
    private long menuPosition;
    private Location pressedLocation;       // Локация на которой было нажато меню
    private SimpleDateFormat dateFormat;
    private Calendar calendar;

    public LocationRecyclerAdapter(LocationSource dataSource, Fragment fragment){
        this.dataSource = dataSource;
        this.fragment = fragment;
        this.dateFormat = new SimpleDateFormat("dd MMMM HH:mm", Locale.getDefault());
        this.calendar = Calendar.getInstance();
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
        calendar.setTimeInMillis(location.date*1000L);
        holder.textViewDate.setText(dateFormat.format(calendar.getTime()));
        holder.textViewCity.setText(location.city);
        holder.imageViewWeather.setImageResource(R.drawable.bkn_d_light);
        holder.textViewTemp.setText(String.valueOf(location.temperature));
        holder.textViewRegion.setText(location.region);

        // Тут определяем, какой пункт меню был нажат
        holder.cardView.setOnLongClickListener(view -> {
            pressedLocation = location;
            menuPosition = position;
            return false;
        });

        // Регистрируем контекстное меню
        if (fragment != null){
            fragment.registerForContextMenu(holder.cardView);
        }
    }

    @Override
    public int getItemCount() {
        return (int) dataSource.getCountHistoryLocations();
    }

    public long getMenuPosition() { return menuPosition; }
    public Location getPressedLocation() {return pressedLocation; }

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

