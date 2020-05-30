package ru.magzyumov.weatherapp.Forecast.Display;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class PicassoLoader {

    public void load(String path, ImageView imageView){
        Picasso.get()
                .load(path)
                .transform(new weatherTransformation())
                .into(imageView);
    }

    public void load(int resId, ImageView imageView){
        Picasso.get().load(resId).into(imageView);
    }

    public class weatherTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth() - 60, source.getHeight() - 60);
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() { return "square()"; }
    }
}
