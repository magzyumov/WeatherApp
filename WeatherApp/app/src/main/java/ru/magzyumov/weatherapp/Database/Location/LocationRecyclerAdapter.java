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
    private LocationDataSource dataSource;
    // Позиция в списке, на которой было нажато меню
    private long menuPosition;
    private Location pressedLocation;       // Локация на которой было нажато меню
    private SimpleDateFormat dateFormat;
    private Calendar calendar;
    private OnItemClickListener itemClickListener;
    private OnMenuClickListener menuClickListener;

    public LocationRecyclerAdapter(LocationDataSource dataSource, Fragment fragment){
        this.dataSource = dataSource;
        this.fragment = fragment;
        this.dateFormat = new SimpleDateFormat("dd MMMM HH:mm", Locale.getDefault());
        this.calendar = Calendar.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        if (itemClickListener != null) {
            viewHolder.setOnClickListener(itemClickListener);
        }

        if (menuClickListener != null) {
            viewHolder.setOnMenuClickListener(menuClickListener);
        }

        return viewHolder;
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
        holder.textViewTemp.setText(String.valueOf((int)location.temperature));
        holder.textViewRegion.setText(location.region);

        holder.imageViewMenu.setContentDescription(location.region + "~" + location.city);

        // Тут определяем, какой пункт меню был нажат
        holder.cardView.setOnLongClickListener(view -> {
            pressedLocation = location;
            menuPosition = position;
            return false;
        });

        // Регистрируем контекстное меню
        if (fragment != null){
            fragment.registerForContextMenu(holder.imageViewMenu);
        }
    }

    @Override
    public int getItemCount() {
        return (int) dataSource.getCountHistoryLocations();
    }

    public long getMenuPosition() { return menuPosition; }

    public Location getPressedLocation() {return pressedLocation; }

    //Интерфейс для обработки нажатий как в ListView
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    //Интерфейс для обработки нажатий на меню
    public interface OnMenuClickListener{
        void onMenuClick(View view, int position);
    }

    //Сеттер слушателя нажатий
    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    //Сеттер слушателя нажатий
    public void setOnMenuClickListener(OnMenuClickListener menuClickListener){
        this.menuClickListener = menuClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate;
        TextView textViewCity;
        ImageView imageViewWeather;
        ImageView imageViewMenu;
        TextView textViewTemp;
        TextView textViewRegion;
        View cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView;
            textViewDate = cardView.findViewById(R.id.textViewDate);
            textViewCity = cardView.findViewById(R.id.textViewCity);
            imageViewWeather = cardView.findViewById(R.id.imageViewWeather);
            imageViewMenu = cardView.findViewById(R.id.imageViewMenu);
            textViewTemp = cardView.findViewById(R.id.textViewTemp);
            textViewRegion = cardView.findViewById(R.id.textViewRegion);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemClickListener != null){
                        itemClickListener.onItemClick(view, getAdapterPosition());
                    }
                }
            });

            imageViewMenu.setOnClickListener(new ImageView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(menuClickListener != null){
                        menuClickListener.onMenuClick(view, getAdapterPosition());
                    }
                }
            });
        }

        public void setOnClickListener(final OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Получаем позицию адаптера
                    int adapterPosition = getAdapterPosition();
                    // Проверяем ее на корректность
                    if (adapterPosition == RecyclerView.NO_POSITION) return;
                    listener.onItemClick(view, adapterPosition);
                }
            });
        }

        public void setOnMenuClickListener(final OnMenuClickListener listener){
            imageViewMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Получаем позицию адаптера
                    int adapterPosition = getAdapterPosition();
                    // Проверяем ее на корректность
                    if (adapterPosition == RecyclerView.NO_POSITION) return;
                    listener.onMenuClick(view, adapterPosition);
                }
            });
        }

    }
}

