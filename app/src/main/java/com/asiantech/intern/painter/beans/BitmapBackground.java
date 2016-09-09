package com.asiantech.intern.painter.beans;

import android.graphics.Bitmap;
import android.graphics.Paint;

import lombok.Getter;
import lombok.Setter;

/**
 * Copyright @2016 AsianTech Inc.
 * Created by LyHV on 9/9/2016.
 */
@Getter
@Setter
public class BitmapBackground extends BaseDrawer {
    private Paint paint;
    private Bitmap bitmap;
    private float left;
    private float top;
    private boolean isSetting;

    public BitmapBackground() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }
}
