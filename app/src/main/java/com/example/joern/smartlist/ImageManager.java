package com.example.joern.smartlist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Created by joern on 16.06.2016.
 */
public class ImageManager {


    private long cacheDurationMillis;
    private DateFormat dateFormat;

    private HashMap<String, SoftReference<Bitmap>> imageMap = new HashMap<>();

    private File cacheDir;
    private ImageRefQueue imageRefQueue = new ImageRefQueue();
    private Thread imageLoaderThread = new Thread(new ImageQueueManager());

    private Activity activity;

    public ImageManager(Activity activity, long cacheDurationMillis) {

        this.activity = activity;
        this.cacheDurationMillis = cacheDurationMillis;

        //TODO Make this match the date format of your server
        dateFormat = new SimpleDateFormat("EEE',' dd MMM yyyy HH:mm:ss zzz");

        // Make background thread low priority, to avoid affecting UI performance
        imageLoaderThread.setPriority(Thread.NORM_PRIORITY-1);

        // Find the dir to save cached images
        String sdState = android.os.Environment.getExternalStorageState();
        if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
            File sdDir = android.os.Environment.getExternalStorageDirectory();
            cacheDir = new File(sdDir,"data/smartlist");

        } else {
            cacheDir = activity.getCacheDir();
        }

        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }

    public void displayImage(String url, ImageView imageView, int defaultDrawableId) {

        if(imageMap.containsKey(url)) {
            imageView.setImageBitmap(imageMap.get(url).get());

        } else {
            queueImage(url, imageView, defaultDrawableId);
            imageView.setImageResource(defaultDrawableId);
        }
    }

    private void queueImage(String url, ImageView imageView, int defaultDrawableId) {

        // This ImageView might have been used for other images, so we clear
        // the queue of old tasks before starting.
        imageRefQueue.clean(imageView);
        ImageRef p = new ImageRef(url, imageView, defaultDrawableId);

        synchronized(imageRefQueue.getImageRefs()) {
            imageRefQueue.getImageRefs().push(p);
            imageRefQueue.getImageRefs().notifyAll();
        }

        // Start thread if it's not started yet
        if(imageLoaderThread.getState() == Thread.State.NEW) {
            imageLoaderThread.start();
        }
    }

    private Bitmap getBitmap(String url) {

        try {
            URLConnection openConnection = new URL(url).openConnection();

            String filename = String.valueOf(url.hashCode());

            File bitmapFile = new File(cacheDir, filename);
            Bitmap bitmap = BitmapFactory.decodeFile(bitmapFile.getPath());

            long currentTimeMillis = System.currentTimeMillis();

            // Is the bitmap in our cache?
            if (bitmap != null) {

                // Has it expired?
                long bitmapTimeMillis = bitmapFile.lastModified();
                if ( (currentTimeMillis - bitmapTimeMillis) < cacheDurationMillis) {
                    return bitmap;
                }

                /*
                // Check also if it was modified on the server before downloading it
                String lastMod = openConnection.getHeaderField("Last-Modified");
                long lastModTimeMillis = dateFormat.parse(lastMod).getTime();

                if ( lastModTimeMillis <= bitmapTimeMillis ) {

                    //Discard the connection and return the cached version
                    return bitmap;
                }
                */
            }

            // Nope, have to download it
            bitmap = BitmapFactory.decodeStream(openConnection.getInputStream());
            // save bitmap to cache for later
            writeFile(bitmap, bitmapFile);

            return bitmap;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void writeFile(Bitmap bmp, File f) {
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, out);
        } catch (Exception e) {
            Log.d("", "problems writing bitmap to file");
            e.printStackTrace();
        }
        finally {
            try { if (out != null ) out.close(); }
            catch(Exception ex) {
                Log.d("", "problems writing bitmap to file");
            }
        }
    }

    /** Classes **/





    private class ImageQueueManager implements Runnable {


        @Override
        public void run() {
            try {
                while(true) {
                    // Thread waits until there are images in the
                    // queue to be retrieved
                    if(imageRefQueue.getImageRefs().size() == 0) {
                        synchronized(imageRefQueue.getImageRefs()) {
                            imageRefQueue.getImageRefs().wait();
                        }
                    }

                    // When we have images to be loaded
                    if(imageRefQueue.getImageRefs().size() != 0) {
                        ImageRef imageToLoad;

                        synchronized(imageRefQueue.getImageRefs()) {
                            imageToLoad = imageRefQueue.getImageRefs().pop();
                        }

                        Bitmap bmp = getBitmap(imageToLoad.url);
                        imageMap.put(imageToLoad.url, new SoftReference<Bitmap>(bmp));
                        Object tag = imageToLoad.imageView.getTag();

                        // Make sure we have the right view - thread safety defender
                        if(tag != null && ((String)tag).equals(imageToLoad.url)) {

                            ImageDisplayer imageDisplayer = new ImageDisplayer(bmp, imageToLoad.imageView, imageToLoad.defaultDrawableId);
                            //Activity a = (Activity)imageToLoad.imageView.getContext();
                            activity.runOnUiThread(imageDisplayer);
                        }
                    }

                    if(Thread.interrupted()){
                        Log.d("", "thread interrupted");
                        break;
                    }

                }
            } catch (InterruptedException e) {
                Log.d("", "thread interrupted");
            }
        }
    }
}