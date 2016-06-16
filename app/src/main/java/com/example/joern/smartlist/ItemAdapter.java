package com.example.joern.smartlist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by joern on 15.06.2016.
 */
public class ItemAdapter extends ArrayAdapter<ListItem> {

    private ArrayList<ListItem> items;
    private Activity activity;
    private static LayoutInflater inflater = null;
    public ImageManager imageManager;

    public ItemAdapter(Activity activity, int textViewResourceId, ArrayList<ListItem> items) {

        super(activity, textViewResourceId, items);

        this.items = items;
        this.activity = activity;
        imageManager = new ImageManager(activity.getApplicationContext());
    }

    public static class ViewHolder{
        public ImageView image;
        public TextView text;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        ViewHolder holder;

        if (v == null) {
            LayoutInflater li = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.simple_list_item, null);
            holder = new ViewHolder();
            holder.image = (ImageView) v.findViewById(R.id.iv_image);
            holder.text = (TextView) v.findViewById(R.id.tv_text);
            v.setTag(holder);
        }
        else
            holder=(ViewHolder)v.getTag();

        final ListItem item = items.get(position);
        if (item != null) {
            holder.text.setText(item.getText());
            holder.image.setTag(item.getImageUrl());
            imageManager.displayImage(item.getImageUrl(), activity, holder.image);
        }
        return v;
    }
}