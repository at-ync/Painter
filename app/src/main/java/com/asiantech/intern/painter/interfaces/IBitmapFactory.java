package com.asiantech.intern.painter.interfaces;

import android.graphics.Canvas;

/**
 * Copyright @2016 AsianTech Inc.
 * Created by LyHV on 9/8/2016.
 */
public interface IBitmapFactory {
    void onDrawBitmap(Canvas canvas);

    int getAreaInBitmap(float coordinateX, float coordinateY);

    float getRotateAngle(float currentCoordinateX, float currentCoordinateY, float newCoordinateX, float newCoordinateY);

    /**
     * @param coordinateX
     * @param coordinateY
     * @return angle flow oy of bitmap
     */
    float getVerticalAngle(float coordinateX, float coordinateY);

}
