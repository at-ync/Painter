package com.asiantech.intern.painter.interfaces;

import android.graphics.Canvas;

import com.asiantech.intern.painter.beans.BitmapDrawer;
import com.asiantech.intern.painter.beans.TextDrawer;

/**
 * Copyright @2016 AsianTech Inc.
 * Created by LyHV on 9/8/2016.
 */
public interface IBitmapFactory {
    void onDrawBitmap(Canvas canvas, BitmapDrawer bitmapDrawer);

    int getAreaInBitmap(BitmapDrawer bitmapDrawer, float coordinateX, float coordinateY);

    float getRotateAngle(BitmapDrawer bitmapDrawer, float currentCoordinateX, float currentCoordinateY, float newCoordinateX, float newCoordinateY);

    /**
     * @param coordinateX
     * @param coordinateY
     * @return angle flow oy of bitmap
     */
    float getVerticalAngle(BitmapDrawer bitmapDrawer, float coordinateX, float coordinateY);

    BitmapDrawer convertTextToBitmap(TextDrawer textDrawer);

    boolean checkTouchCircleBitmap(BitmapDrawer bitmapDrawer, float x, float y);

    void onDrawCircleBitmap(Canvas canvas, BitmapDrawer bitmapDrawer);

    void updatePositionBitmap(BitmapDrawer bitmapDrawer, float movementX, float movementY);
}
