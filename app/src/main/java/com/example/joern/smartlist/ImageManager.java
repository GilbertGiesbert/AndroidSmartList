package com.example.joern.smartlist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.HashMap;

/**
 * Created by joern on 15.06.2016.
 */


public class ImageManager {

    private long cacheDuration;
    private DateFormat dateFormat;

    private HashMap<String, Bitmap> imageMap = new HashMap<String, Bitmap>();
    private File cacheDir;

    private final ImageQueue imageQueue = new ImageQueue();
    private Thread imageLoaderThread = new Thread(new ImageQueueManager());

    public ImageManager(Context context) {

        // Make background thread low priority, to avoid affecting UI performance
        imageLoaderThread.setPriority(Thread.NORM_PRIORITY-1);

        // Find the dir to save cached images
        String sdState = android.os.Environment.getExternalStorageState();
        if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
            File sdDir = android.os.Environment.getExternalStorageDirectory();
            cacheDir = new File(sdDir,"data/smartlist");
        }
        else
            cacheDir = context.getCacheDir();

        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public void displayImage(String url, Activity activity, ImageView imageView) {

        if(imageMap.containsKey(url))
            imageView.setImageBitmap(imageMap.get(url));
        else {
            queueImage(url, imageView);
            imageView.setImageResource(R.mipmap.ic_launcher);
        }
    }



    private void queueImage(String url, ImageView imageView) {

        // This ImageView might have been used for other images, so we clear
        // the queue of old tasks before starting.
        imageQueue.clean(imageView);
        ImageRef p = new ImageRef(url, imageView);

        synchronized(imageQueue) {
            imageQueue.getImageRefs().push(p);
            imageQueue.getImageRefs().notifyAll();
        }

        // Start thread if it's not started yet
        if(imageLoaderThread.getState() == Thread.State.NEW)
            imageLoaderThread.start();
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
                if ( (currentTimeMillis - bitmapTimeMillis) < cacheDuration ) {
                    return bitmap;
                }

                // Check also if it was modified on the server before downloading it
                String lastMod = openConnection.getHeaderField("Last-Modified");
                long lastModTimeMillis = dateFormat.parse(lastMod).getTime();

                if ( lastModTimeMillis <= bitmapTimeMillis ) {

                    //Discard the connection and return the cached version
                    return bitmap;
                }
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
            Log.d("", "failed to write bitmap file");
            e.printStackTrace();
        }
        finally {
            try { if (out != null ) out.close(); }
            catch(Exception ex) {
                Log.d("", "failed to write bitmap file");
            }
        }
    }


}