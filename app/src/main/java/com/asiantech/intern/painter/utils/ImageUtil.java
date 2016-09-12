package com.asiantech.intern.painter.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by HungTQB on 12/09/2016.
 */
public final class ImageUtil {
    public static ImageUtil sImageUtil;

    public static ImageUtil getInstance() {
        if (sImageUtil == null) {
            sImageUtil = new ImageUtil();
        }
        return sImageUtil;
    }

    public Bitmap scaleBitmap(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());
        System.gc();
        return Bitmap.createScaledBitmap(realImage, width, height, filter);
    }

    public Bitmap compressBitmap(Bitmap sourceBitmap, int quality) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        sourceBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        Rect rect = new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 3;
        return BitmapFactory.decodeStream(new ByteArrayInputStream(outputStream.toByteArray()), rect, options);
    }
}
