package ru.magzyumov.weatherapp.Forecast.Display;

import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoLoader {

    public void load(String path, ImageView imageView){
        Picasso.get().load(path).into(imageView);
    }

    public void load(int resId, ImageView imageView){
        Picasso.get().load(resId).into(imageView);
    }
}
