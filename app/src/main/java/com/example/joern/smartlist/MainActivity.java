package com.example.joern.smartlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

// code source:
// http://www.codehenge.net/2011/06/android-development-tutorial-asynchronous-lazy-loading-and-caching-of-listview-images/

// image source
// http://gallery.photo.net/photo/18246292-md.jpg

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ArrayList<ListItem> items = new ListItemFactory().buildListItems(10);




    }

}
