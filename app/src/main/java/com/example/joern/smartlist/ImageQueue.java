package com.example.joern.smartlist;

import android.widget.ImageView;

import java.util.Stack;

/**
 * Created by joern on 15.06.2016.
 */
public class ImageQueue {

    private Stack<ImageRef> imageRefs = new Stack<ImageRef>();

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