package com.melbournestore.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtils {

    //图片缩放
    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h = (int) (newHeight * densityMultiplier);
        int w = (int) (h * photo.getWidth() / ((double) photo.getHeight()));

        photo = Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

    public static void saveMyBitmap(String bitName, Bitmap mBitmap) throws IOException {

        String directory = Environment.getExternalStorageDirectory().getPath();

        String bitmap_path = directory + "/yabbyhouse";

        File file = new File(bitmap_path);
        if (!file.exists())
            file.mkdir();

        File f = new File(bitmap_path + "/" + bitName + ".png");
        if (f.exists()) {
            f.delete();
        }
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getMyBitMapFile(String name){
        String directory = Environment.getExternalStorageDirectory().getPath();

        String bitmap_path = directory + "/yabbyhouse";


        File file = new File(bitmap_path + "/" + name + ".png");
        return file;
    }



}
