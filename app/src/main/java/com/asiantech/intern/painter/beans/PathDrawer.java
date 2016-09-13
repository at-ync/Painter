package com.asiantech.intern.painter.beans;

import android.graphics.Paint;
import android.graphics.Path;

import lombok.Data;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by HungTQB on 12/09/2016.
 */
@Data
public class PathDrawer extends BaseDrawer {
    private Path path;
    private Paint paint;

    public PathDrawer() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(12);
        paint.setStyle(Paint.Style.STROKE);
    }
}
