package com.asiantech.intern.painter.models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import com.asiantech.intern.painter.beans.BitmapDrawer;
import com.asiantech.intern.painter.beans.TextDrawer;
import com.asiantech.intern.painter.commons.Constant;
import com.asiantech.intern.painter.interfaces.IBitmapFactory;
import com.asiantech.intern.painter.utils.PaintUtil;

import lombok.Setter;

/**
 * Copyright @2016 AsianTech Inc.
 * Created by LyHV on 9/7/2016.
 */
@Setter
public class BitmapFactory implements IBitmapFactory {

    @Override
    public void onDrawBitmap(Canvas canvas, BitmapDrawer bitmapDrawer) {
        Matrix matrix = new Matrix();
        matrix.postTranslate(bitmapDrawer.getBitmapCoordinateX(), bitmapDrawer.getBitmapCoordinateY());
        matrix.postRotate(bitmapDrawer.getRotateAngle(), bitmapDrawer.getRotateOriginX(), bitmapDrawer.getRotateOriginY());
        canvas.drawBitmap(bitmapDrawer.getBitmap(), matrix, null);
    }

    @Override
    public int getAreaInBitmap(BitmapDrawer bitmapDrawer, float coordinateX, float coordinateY) {
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
    public float getRotateAngle(BitmapDrawer bitmapDrawer, float currentCoordinateX, float currentCoordinateY, float newCoordinateX, float newCoordinateY) {
        int areaNew = getAreaInBitmap(bitmapDrawer, newCoordinateX, newCoordinateY);
        float angleCurrent = getVerticalAngle(bitmapDrawer, currentCoordinateX, currentCoordinateY);
        float angleNew = getVerticalAngle(bitmapDrawer, newCoordinateX, newCoordinateY);
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
    public float getVerticalAngle(BitmapDrawer bitmapDrawer, float coordinateX, float coordinateY) {
        int area = getAreaInBitmap(bitmapDrawer, coordinateX, coordinateY);
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
        BitmapDrawer bitmapDrawer = new BitmapDrawer();
        Rect rect = new Rect();
        textDrawer.getPaint().getTextBounds(textDrawer.getContent(), 0, textDrawer.getContent().length(), rect);
        int height = (int) (Math.abs(textDrawer.getPaint().ascent()) + Math.abs(textDrawer.getPaint().descent()));
        int width = (int) textDrawer.getPaint().measureText(textDrawer.getContent(), 0, textDrawer.getContent().length());
        Log.d("Height", height + "");
        Log.d("Width", width + "");
        bitmapDrawer.setBitmap(Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888));// ???fuck this
        bitmapDrawer.setBitmapCoordinateX(textDrawer.getCoordinatesX());
        bitmapDrawer.setBitmapCoordinateY(textDrawer.getCoordinatesY());
     /*   bitmapDrawer.setRotateOriginX(textDrawer.getCoordinatesX() + rect.width() / 2);
        bitmapDrawer.setRotateOriginY(textDrawer.getCoordinatesY() + rect.height() / 2);*/
        Canvas canvas = new Canvas(bitmapDrawer.getBitmap());
        TextFactory textFactory = new TextFactory();
        textFactory.onDrawText(canvas, textDrawer);
        return bitmapDrawer;
    }

    @Override
    public boolean isTouchCircleBitmap(BitmapDrawer bitmapDrawer, float x, float y) {
        float originX = bitmapDrawer.getRotateOriginX();
        float originY = bitmapDrawer.getRotateOriginY();
        float radius = (float) Math.sqrt(Math.pow(x - originX, 2) + Math.pow(y - originY, 2));
        return radius <= bitmapDrawer.getRadius();
    }

    @Override
    public void onDrawCircleBitmap(Canvas canvas, BitmapDrawer bitmapDrawer) {
        canvas.drawCircle(bitmapDrawer.getBitmapCoordinateX(), bitmapDrawer.getBitmapCoordinateY(), bitmapDrawer.getRadius(), PaintUtil.getInstance().getPaint());

    }

}
