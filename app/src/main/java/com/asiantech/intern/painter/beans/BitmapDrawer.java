package com.asiantech.intern.painter.beans;

import android.graphics.Bitmap;

import lombok.Data;

/**
 * Copyright @2016 AsianTech Inc.
 * Created by LyHV on 9/7/2016.
 */
@Data
public class BitmapDrawer extends BaseDrawer {
    private Bitmap bitmap;
    private float radius;
    private float rotateOriginX;
    private float rotateOriginY;
    private float bitmapCoordinateX;
    private float bitmapCoordinateY;
    private float rotateAngle;
}
