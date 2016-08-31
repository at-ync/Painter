package com.asiantech.intern.painter.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by HungTQB on 30/08/2016.
 */
public class CustomDrawingView extends View {

    private static final float TOUCH_TOLERANCE = 4;
    private Path mPath;
    private Paint mPaint;
    private Bitmap mBitmap;
    private Paint mBitmapPaint;
    private Canvas mCanvasBitmap;
    private float mX;
    private float mY;

    public CustomDrawingView(Context context) {
        super(context);
        init();
    }

    public CustomDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mBitmap == null) {
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        } else {
            mBitmap = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
        }
        mCanvasBitmap = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                onTouchUp();
                invalidate();
                break;
        }
        return true;
    }

    private void onTouchDown(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void onTouchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void onTouchUp() {
        mPath.lineTo(mX, mY);
        mCanvasBitmap.drawPath(mPath, mPaint);
        setLayerType(LAYER_TYPE_HARDWARE, mBitmapPaint);
        mPath.reset();
    }

    public void setPaint(Paint paint) {
        this.mPaint = paint;
    }
}
