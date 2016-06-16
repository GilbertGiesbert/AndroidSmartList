package com.example.joern.smartlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

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


        ArrayList<ImageItem> items = new ImageItemFactory().buildItems(50);
        ImageItemAdapter adapter = new ImageItemAdapter(this, R.layout.simple_list_item, items);

        ListView lv = (ListView) findViewById(R.id.lv_list);

        if(lv != null)
            lv.setAdapter(adapter);


    }

}
