package com.asiantech.intern.painter.models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.asiantech.intern.painter.beans.BitmapDrawer;
import com.asiantech.intern.painter.beans.TextDrawer;
import com.asiantech.intern.painter.commons.Constant;
import com.asiantech.intern.painter.interfaces.IBitmapFactory;

import lombok.Setter;

/**
 * Copyright @2016 AsianTech Inc.
 * Created by LyHV on 9/7/2016.
 */
@Setter
public class BitmapFactory implements IBitmapFactory {
    BitmapDrawer bitmapDrawer;

    @Override
    public void onDrawBitmap(Canvas canvas) {
        Matrix matrix = new Matrix();
        matrix.postTranslate(bitmapDrawer.getBitmapCoordinateX(), bitmapDrawer.getBitmapCoordinateY());
        matrix.postRotate(bitmapDrawer.getRotateAngle(), bitmapDrawer.getRotateOriginX(), bitmapDrawer.getRotateOriginY());
        canvas.drawBitmap(bitmapDrawer.getBitmap(), matrix, null);
    }

    @Override
    public int getAreaInBitmap(float coordinateX, float coordinateY) {
        if (coordinateX <= bitmapDrawer.getRotateOriginX() && coordinateY < bitmapDrawer.getRotateOriginY())
            return Constant.AREA_1;
        if (coordinateX > bitmapDrawer.getRotateOriginX() && coordinateY <= bitmapDrawer.getRotateOriginY())
            return Constant.AREA_2;
        if (coordinateX >= bitmapDrawer.getRotateOriginX() && coordinateY > bitmapDrawer.getRotateOriginY())
            return Constant.AREA_3;
        if (coordinateX < bitmapDrawer.getRotateOriginX() && coordinateY >= bitmapDrawer.getRotateOriginY())
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
                return (float) (Math.atan((bitmapDrawer.getRotateOriginX() - coordinateX) / (bitmapDrawer.getRotateOriginY() - coordinateY)) / Math.PI * 180);
            case Constant.AREA_2:
                if (bitmapDrawer.getRotateOriginY() == coordinateY) {
                    return 90;
                } else {
                    return (float) (Math.atan((coordinateX - bitmapDrawer.getRotateOriginX()) / (bitmapDrawer.getRotateOriginY() - coordinateY)) / Math.PI * 180);
                }
            case Constant.AREA_3:
                return (float) (Math.atan((coordinateX - bitmapDrawer.getRotateOriginX()) / (coordinateY - bitmapDrawer.getRotateOriginY())) / Math.PI * 180);
            case Constant.AREA_4:
                if (bitmapDrawer.getRotateOriginY() == coordinateY) {
                    return 90;
                } else {
                    return (float) (Math.atan((bitmapDrawer.getRotateOriginX() - coordinateX) / (coordinateY - bitmapDrawer.getRotateOriginY())) / Math.PI * 180);
                }
        }
        return 0;
    }

    @Override
    public BitmapDrawer convertTextToBitmap(TextDrawer textDrawer) {
        BitmapDrawer bitmapDrawer = null;
        Rect rect = new Rect();
        textDrawer.getPaint().getTextBounds(textDrawer.getContent(), 0, textDrawer.getContent().length(), rect);
        bitmapDrawer.setBitmap(Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888));
        bitmapDrawer.setBitmapCoordinateX(textDrawer.getCoordinatesX());
        bitmapDrawer.setBitmapCoordinateY(textDrawer.getCoordinatesY());
        bitmapDrawer.setRotateOriginX(textDrawer.getCoordinatesX() + rect.width() / 2);
        bitmapDrawer.setBitmapCoordinateY(textDrawer.getCoordinatesY() + rect.height() / 2);
        TextFactory textFactory = new TextFactory();
        Canvas canvas = new Canvas(bitmapDrawer.getBitmap());
        textFactory.onDrawText(canvas, textDrawer);
        return bitmapDrawer;
    }

}
