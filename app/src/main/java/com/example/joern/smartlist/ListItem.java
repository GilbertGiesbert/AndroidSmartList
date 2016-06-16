package com.example.joern.smartlist;

import android.graphics.Bitmap;

/**
 * Created by joern on 15.06.2016.
 */
public class ListItem {

    private String text;
    private String imageUrl;
    private Bitmap image;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
