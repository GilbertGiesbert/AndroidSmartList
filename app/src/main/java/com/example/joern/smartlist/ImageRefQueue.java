package com.example.joern.smartlist;

import android.widget.ImageView;

import java.util.Stack;

/**
 * Created by joern on 16.06.2016.
 */

//stores list of images to download
public class ImageRefQueue {


    private Stack<ImageRef> imageRefs = new Stack<>();

    //removes all instances of this ImageView
    public void clean(ImageView view) {

        for(int i = 0 ;i < imageRefs.size();) {
            if(imageRefs.get(i).imageView == view)
                imageRefs.remove(i);
            else ++i;
        }
    }

    public Stack<ImageRef> getImageRefs() {
        return imageRefs;
    }

    public void setImageRefs(Stack<ImageRef> imageRefs) {
        this.imageRefs = imageRefs;
    }
}