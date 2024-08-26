package com.example.pickcourt.Utilities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.pickcourt.R;

public class ImageLoader {
    private static volatile  ImageLoader instance;
    private static Context context;

    private ImageLoader(Context context) {this.context = context;}

    public static ImageLoader getInstance(){
        return instance;
    }

    public static ImageLoader init(Context context){
        if(instance == null){
            synchronized (ImageLoader.class){
                if(instance == null)
                    instance = new ImageLoader(context);
            }
        }
        return getInstance();
    }

    public void load(String source, ImageView imageView){
        Glide.with(context)
                .load(source)
                .placeholder(R.drawable.unavailable_photo)
                .centerInside()
                .into(imageView);
    }

    public void load(Drawable source, ImageView imageView){
        Glide.with(context)
                .load(source)
                .placeholder(R.drawable.unavailable_photo)
                .centerInside()
                .into(imageView);
    }

}
