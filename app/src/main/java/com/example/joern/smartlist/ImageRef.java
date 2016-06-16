package com.example.joern.smartlist;

import android.widget.ImageView;

/**
 * Created by joern on 15.06.2016.
 */


public class ImageRef {

    public String url;
    public ImageView imageView;
    public int defaultDrawableId;

    public ImageRef(String url, ImageView imageView, int defaultDrawableId) {

        this.url=url;
        this.imageView=imageView;
        this.defaultDrawableId = defaultDrawableId;
    }
}