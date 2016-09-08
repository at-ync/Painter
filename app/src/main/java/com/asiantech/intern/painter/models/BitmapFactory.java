package com.asiantech.intern.painter.models;

import android.graphics.Canvas;
import android.graphics.Matrix;

import com.asiantech.intern.painter.beans.BitmapObject;
import com.asiantech.intern.painter.commons.Constant;
import com.asiantech.intern.painter.interfaces.IBitmapFactory;

import lombok.Setter;

/**
 * Copyright @2016 AsianTech Inc.
 * Created by LyHV on 9/7/2016.
 */
@Setter
public class BitmapFactory implements IBitmapFactory {
    BitmapObject bitmapObject;

    @Override
    public void onDrawBitmap(Canvas canvas) {
        Matrix matrix = new Matrix();
        matrix.postTranslate(bitmapObject.getBitmapCoordinateX(), bitmapObject.getBitmapCoordinateY());
        matrix.postRotate(bitmapObject.getRotateAngle(), bitmapObject.getRotateOriginX(), bitmapObject.getRotateOriginY());
        canvas.drawBitmap(bitmapObject.getBitmap(), matrix, null);
    }

    @Override
    public int getAreaInBitmap(float coordinateX, float coordinateY) {
        if (coordinateX <= bitmapObject.getRotateOriginX() && coordinateY < bitmapObject.getRotateOriginY())
            return Constant.AREA_1;
        if (coordinateX > bitmapObject.getRotateOriginX() && coordinateY <= bitmapObject.getRotateOriginY())
            return Constant.AREA_2;
        if (coordinateX >= bitmapObject.getRotateOriginX() && coordinateY > bitmapObject.getRotateOriginY())
            return Constant.AREA_3;
        if (coordinateX < bitmapObject.getRotateOriginX() && coordinateY >= bitmapObject.getRotateOriginY())
            return Constant.AREA_4;
        return 0;
    }

    @Override
    public float getRotateAngle(float currentCoordinateX, float currentCoordinateY, float newCoordinateX, float newCoordinateY) {
        int areaNew = getAreaInBitmap(newCoordinateX, newCoordinateY);
        float angleCurrent = getVerticalAngle(currentCoordinateX, currentCoordinateY);
        float angleNew = getVerticalAngle(newCoordinateX, newCoordinateY);
        switch (areaNew) {
            case Constant.AREA_1:
            case Constant.AREA_3:
                return angleCurrent - angleNew;
            case Constant.AREA_2:
            case Constant.AREA_4:
                return angleNew - angleCurrent;
        }
        return 0;
    }

    @Override
    public float getVerticalAngle(float coordinateX, float coordinateY) {
        int area = getAreaInBitmap(coordinateX, coordinateY);
        switch (area) {
            case Constant.AREA_1:
                return (float) (Math.atan((bitmapObject.getRotateOriginX() - coordinateX) / (bitmapObject.getRotateOriginY() - coordinateY)) / Math.PI * 180);
            case Constant.AREA_2:
                if (bitmapObject.getRotateOriginY() == coordinateY) {
                    return 90;
                } else {
                    return (float) (Math.atan((coordinateX - bitmapObject.getRotateOriginX()) / (bitmapObject.getRotateOriginY() - coordinateY)) / Math.PI * 180);
                }
            case Constant.AREA_3:
                return (float) (Math.atan((coordinateX - bitmapObject.getRotateOriginX()) / (coordinateY - bitmapObject.getRotateOriginY())) / Math.PI * 180);
            case Constant.AREA_4:
                if (bitmapObject.getRotateOriginY() == coordinateY) {
                    return 90;
                } else {
                    return (float) (Math.atan((bitmapObject.getRotateOriginX() - coordinateX) / (coordinateY - bitmapObject.getRotateOriginY())) / Math.PI * 180);
                }
        }
        return 0;
    }
}
