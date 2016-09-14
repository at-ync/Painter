package com.asiantech.intern.painter.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.asiantech.intern.painter.R;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by HungTQB on 13/09/2016.
 */
public class CustomCirclePath extends View {
    private int mCircleRadius;
    private int mCircleColor = Color.BLACK;
    private Paint mPaint;

    public CustomCirclePath(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomCirclePath(Context context, int radius) {
        super(context);
        mPaint = new Paint();
        mPaint.setColor(mCircleColor);
        mCircleRadius = radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mCircleRadius, mPaint);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setColor(mCircleColor);
        setSaveEnabled(true);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomCirclePath, 0, 0);
        mCircleColor = typedArray.getColor(R.styleable.CustomCirclePath_circleColor, 0);
        mCircleRadius = typedArray.getDimensionPixelSize(R.styleable.CustomCirclePath_circleRadius, 0);
    }

    public int getCircleRadius() {
        return mCircleRadius;
    }

    public void setCircleRadius(int mCircleRadius) {
        this.mCircleRadius = mCircleRadius;
        mPaint.setStrokeWidth(mCircleRadius);
        invalidate();
    }

    public int getCircleColor() {
        return mCircleColor;
    }

    public void setCircleColor(int circleColor) {
        this.mCircleColor = circleColor;
        mPaint.setColor(mCircleColor);
        invalidate();
    }
}
