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
 * Created by joern on 16.06.2016.
 */
public class ImageItemAdapter extends ArrayAdapter<ImageItem> {


    private ArrayList<ImageItem> itemList;
    private Activity activity;
    public ImageManager imageManager;

    public ImageItemAdapter(Activity activity, int itemLayoutId, ArrayList<ImageItem> itemList) {
        super(activity, itemLayoutId, itemList);

        this.itemList = itemList;
        this.activity = activity;
        long cacheDurationMillis = 1000*20; // 20 secs
        imageManager = new ImageManager(activity, cacheDurationMillis);
    }

    public static class ViewHolder{
        public TextView text;
        public ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;

        if (view == null) {

            LayoutInflater li = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.simple_list_item, null);

            holder = new ViewHolder();
            holder.text = (TextView) view.findViewById(R.id.tv_text);
            holder.image = (ImageView) view.findViewById(R.id.iv_image);
            view.setTag(holder);
        }
        else
            holder = (ViewHolder)view.getTag();

        final ImageItem item = itemList.get(position);
        if (item != null) {
            holder.text.setText(item.getText());
            holder.image.setTag(item.getImageUrl());
            imageManager.displayImage(item.getImageUrl(), holder.image, R.mipmap.ic_launcher);
        }
        return view;
    }
}