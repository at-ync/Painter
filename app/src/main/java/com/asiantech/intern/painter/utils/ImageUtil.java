package com.asiantech.intern.painter.utils;

import android.graphics.Bitmap;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by HungTQB on 12/09/2016.
 */
public class ImageUtil {
    public static Bitmap scaleBitmap(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());
        System.gc();
        return Bitmap.createScaledBitmap(realImage, width, height, filter);
    }
}
