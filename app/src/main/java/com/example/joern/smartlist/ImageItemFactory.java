package com.example.joern.smartlist;

import java.util.ArrayList;

/**
 * Created by joern on 16.06.2016.
 */
public class ImageItemFactory {

    // http://gallery.photo.net/photo/18246292-md.jpg

    public ArrayList<ImageItem> buildItems(int quantity){

        String placeholder = "__placeholder__";
        String baseUrl = "http://gallery.photo.net/photo/18246"+placeholder+"-md.jpg";

        ArrayList<ImageItem> itemlist = new ArrayList<>();

        int start = 100;
        int stop = start + quantity;
        for(int i = start; i < stop; i++){

            ImageItem item = new ImageItem();
            item.setText("item no. "+i);
            item.setImageUrl(baseUrl.replace(placeholder, ""+i));
            itemlist.add(item);
        }
        return itemlist;
    }
}