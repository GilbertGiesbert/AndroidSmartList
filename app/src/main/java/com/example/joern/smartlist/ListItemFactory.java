package com.example.joern.smartlist;

import java.util.ArrayList;

/**
 * Created by joern on 16.06.2016.
 */

public class ListItemFactory {


    public ArrayList<ListItem> buildListItems(int quantity){

        String placeholder = "_placeholder_";
        // http://gallery.photo.net/photo/18246292-md.jpg
        String baseUrl = "http://gallery.photo.net/photo/18246" + placeholder + "-md.jpg";

        ArrayList<ListItem> items = new ArrayList<>();

        for (int i = 100; i < (i+quantity); i++){

            ListItem item = new ListItem();
            item.setText("item no. "+i);
            item.setImageUrl(baseUrl.replace(placeholder, ""+i));
            items.add(item);

        }
        return items;
    }
}
