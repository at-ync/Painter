package com.asiantech.intern.painter.utils;

import android.graphics.Paint;

/**
 * Copyright @2016 AsianTech Inc.
 * Created by LyHV on 9/7/2016.
 */
public class PaintUtil {
    private static PaintUtil mPaintUtil;
    private static final int STROKE_WIDTH = 1;

    public static PaintUtil getInstance() {
        if (mPaintUtil == null) {
            mPaintUtil = new PaintUtil();
        }
        return mPaintUtil;
    }

    public Paint getPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        return paint;
    }
}
