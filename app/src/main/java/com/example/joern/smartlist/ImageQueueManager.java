package com.example.joern.smartlist;

import android.app.Activity;
import android.graphics.Bitmap;

/**
 * Created by joern on 15.06.2016.
 */
public class ImageQueueManager implements Runnable {

    private final ImageQueue imageQueue;


    @Override
    public void run() {

        try {
            while(true) {
                // Thread waits until there are images in the
                // queue to be retrieved
                if(imageQueue.getImageRefs().size() == 0) {
                    synchronized(imageQueue.imageRefs) {
                        imageQueue.imageRefs.wait();
                    }
                }
                // When we have images to be loaded
                if(imageQueue.imageRefs.size() != 0) {
                    ImageRef imageToLoad;
                    synchronized(imageQueue.imageRefs) {
                        imageToLoad = imageQueue.imageRefs.pop();
                    }
                    Bitmap bmp = getBitmap(imageToLoad.url);
                    imageMap.put(imageToLoad.url, bmp);
                    Object tag = imageToLoad.imageView.getTag();
                    // Make sure we have the right view - thread safety defender
                    if(tag != null && ((String)tag).equals(imageToLoad.url)) {
                        BitmapDisplayer bmpDisplayer =
                                new BitmapDisplayer(bmp, imageToLoad.imageView);
                        Activity a =
                                (Activity)imageToLoad.imageView.getContext();
                        a.runOnUiThread(bmpDisplayer);
                    }
                }
                if(Thread.interrupted())
                    break;
            }
        } catch (InterruptedException e) {}
    }


}