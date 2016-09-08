package com.asiantech.intern.painter.beans;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import lombok.Data;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by HungTQB on 05/09/2016.
 */
@Data
public class DrawingPainter {
    private static final float DEFAULT_STROKE_WIDTH = 12;
    private static final float TOUCH_TOLERANCE = 4;
    private Paint paint;
    private Path path;
    private Bitmap bitmap;
    private Canvas canvas;
    private float coordinatesX;
    private float coordinatesY;

    public DrawingPainter() {
        path = new Path();
        paint = new Paint();
        initPaint();
    }

    public void onTouchStart(float x, float y) {
        path.reset();
        path.moveTo(x, y);
        coordinatesX = x;
        coordinatesY = y;
    }

    public void onTouchMove(float x, float y) {
        float dx = Math.abs(x - coordinatesX);
        float dy = Math.abs(x - coordinatesY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(coordinatesX, coordinatesY, (x + coordinatesX) / 2, (y + coordinatesY) / 2);
            coordinatesX = x;
            coordinatesY = y;
        }
    }

    public void onTouchUp() {
        path.lineTo(coordinatesX, coordinatesY);
        canvas.drawPath(path, paint);
        path.reset();
    }

    public void setBitmap(int w, int h) {
        this.bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(this.bitmap);
    }

    public void setIsEraser(boolean isEraser) {
        if (isEraser) {
            paint.setAlpha(Color.TRANSPARENT);
            paint.setColor(Color.TRANSPARENT);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            paint.setXfermode(null);
            paint.setColor(Color.BLACK);
        }
    }

    private void initPaint() {
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        paint.setStyle(Paint.Style.STROKE);
    }
}
