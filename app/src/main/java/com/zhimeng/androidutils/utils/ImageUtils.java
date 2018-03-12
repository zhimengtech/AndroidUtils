package com.zhimeng.androidutils.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

public class ImageUtils {

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap maxCropBitmap(Bitmap bitmap, int widthRatio, int weightRatio) {
        int width = bitmap.getWidth(), height = bitmap.getHeight();
        width -= width % widthRatio;
        height -= height % weightRatio;
        if (width * weightRatio > height * widthRatio) {
            width = height * widthRatio / weightRatio;
        }
        else if (width * weightRatio < height * widthRatio) {
            height = width * weightRatio / widthRatio;
        }
        return Bitmap.createBitmap(bitmap
                , (bitmap.getWidth() - width) / 2
                , (bitmap.getHeight() - height) / 2
                , width
                , height);
    }

    public static void makeOptionsScaleFitMinSize(BitmapFactory.Options options, int minWidth, int minHeight) {
        int w = options.outWidth;
        int h = options.outHeight;
        int scale = 1;
        while (w / 2 >= minWidth && h / 2 >= minHeight) {
            w/=2;
            h/=2;
            scale*=2;
        }
        options.inSampleSize = scale;
    }

    public static void makeOptionsScaleFitMaxSize(BitmapFactory.Options options, int maxWidth, int maxHeight) {
        int w = options.outWidth;
        int h = options.outHeight;
        int scale = 1;
        while (w > maxWidth || h > maxHeight) {
            w/=2;
            h/=2;
            scale*=2;
        }
        options.inSampleSize = scale;
    }

}
