package com.asiantech.intern.painter.beans;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import lombok.Data;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by HungTQB on 05/09/2016.
 */
@Data
public class DrawingPainter {
    private static final float TOUCH_TOLERANCE = 4;
    private Paint paint;
    private Path path;
    private Bitmap bitmap;
    private Canvas canvas;
    private float coordinatesX;
    private float coordinatesY;

    public DrawingPainter() {
        this.path = new Path();
        this.paint = new Paint();
        this.paint.setStyle(Paint.Style.STROKE);
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

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.canvas = new Canvas(bitmap);
    }
}
