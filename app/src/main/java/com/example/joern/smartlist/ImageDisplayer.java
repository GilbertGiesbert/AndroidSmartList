package com.example.joern.smartlist;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by joern on 16.06.2016.
 */

//Used to display bitmap in the UI thread
public class ImageDisplayer implements Runnable {

    Bitmap bitmap;
    ImageView imageView;
    int defaultDrawableId;

    public ImageDisplayer(Bitmap b, ImageView i, int defaultDrawableId) {
        bitmap=b;
        imageView=i;
        this.defaultDrawableId = defaultDrawableId;
    }

    public void run() {
        if(bitmap != null)
            imageView.setImageBitmap(bitmap);
        else
            imageView.setImageResource(defaultDrawableId);
    }
}