package com.example.joern.smartlist;

import android.widget.ImageView;

/**
 * Created by joern on 16.06.2016.
 */
public class ImageRef {

        public String url;
        public ImageView imageView;
        public int defaultDrawableId;

        public ImageRef(String u, ImageView i, int defaultDrawableId) {
            url=u;
            imageView=i;
            this.defaultDrawableId = defaultDrawableId;
        }
    }