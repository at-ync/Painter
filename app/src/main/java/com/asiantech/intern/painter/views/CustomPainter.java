package com.asiantech.intern.painter.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.asiantech.intern.painter.beans.Component;
import com.asiantech.intern.painter.beans.DrawingPainter;
import com.asiantech.intern.painter.beans.TextDrawer;
import com.asiantech.intern.painter.commons.Constant;
import com.asiantech.intern.painter.interfaces.ITextLab;
import com.asiantech.intern.painter.models.TextFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright @2016 AsianTech Inc.
 * Created by LyHV on 8/31/2016.
 */
public class CustomPainter extends View implements ITextLab {
    private float mInitialX;
    private float mInitialY;
    private boolean mIsOnDraw = true;
    // Text Activities
    private TextFactory mTextFactory;
    private List<Component> mComponents;
    private float mCenterX;
    private float mCenterY;
    private int mActionText;
    //Draw Activities
    private boolean mIsDrawing;
    private Bitmap mBitmapBackground;
    private Paint mPaintBackground;
    private DrawingPainter mDrawingPainter;
    // Image Activities

    public CustomPainter(Context context) {
        super(context);
    }

    public CustomPainter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mTextFactory = new TextFactory();
        mComponents = new ArrayList<>();
        mPaintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDrawingPainter = new DrawingPainter();
        this.setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mBitmapBackground == null) {
            mDrawingPainter.setBitmap(Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888));
        } else {
            mDrawingPainter.setBitmap(mBitmapBackground.copy(Bitmap.Config.ARGB_8888, true));
        }
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float left = (getWidth() - mBitmapBackground.getWidth()) / 2;
        float top = (getHeight() - mBitmapBackground.getHeight()) / 2;
        canvas.drawBitmap(mBitmapBackground, left, top, mPaintBackground);

        int size = mComponents.size();
        for (int i = 0; i < size; i++) {
            Component component = mComponents.get(i);
            onDrawText(canvas, component.getTextObject());
        }
        if (mIsOnDraw) {
            invalidate();
        }

        if (mIsDrawing) {
            canvas.drawPath(mDrawingPainter.getPath(), mDrawingPainter.getPaint());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initMove(event);
                onDrawInit(event);
                break;
            case MotionEvent.ACTION_UP:
                onDrawFinish();
                break;
            case MotionEvent.ACTION_MOVE:
                onDrawMove(event);
                updateMove(event);
                break;
        }
        return true;
    }

    @Override
    public void setTextObject(TextDrawer textObject) {
        synchronized (mComponents) {
            if (textObject == null) {
                return;
            }
            Component component = new Component();
            textObject.setCoordinatesX(mCenterX);
            textObject.setCoordinatesY(mCenterY);
            component.setTextObject(textObject);
            mComponents.add(component);
        }
    }

    @Override
    public void setActionText(int action) {
        mActionText = action;
    }


    private void updateMoveText(float movementX, float movementY) {
        synchronized (mComponents) {
            for (int i = mComponents.size() - 1; i >= 0; i--) {
                TextDrawer textObject = mComponents.get(i).getTextObject();
                if (textObject != null) {
                    if (mTextFactory.isTouchInTextArea(textObject, mInitialX, mInitialY)) {
                        mTextFactory.updateCoordinatesText(textObject, movementX, movementY);
                        break;
                    }
                }

            }
        }

    }

    private void onDrawInit(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        mDrawingPainter.onTouchStart(x, y);
    }

    private void onDrawMove(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (mIsDrawing) {
            mDrawingPainter.onTouchMove(x, y);
        }
    }

    private void onDrawFinish() {
        if (mIsDrawing) {
            mDrawingPainter.onTouchUp();
            setLayerType(LAYER_TYPE_HARDWARE, mPaintBackground);
        }
    }

    private void initMove(MotionEvent event) {
        if (mActionText == Constant.MOVE) {
            mInitialX = event.getX();
            mInitialY = event.getY();
        }
    }

    private void updateMove(MotionEvent event) {
        if (mActionText == Constant.MOVE) {
            float xMovement = event.getX() - mInitialX;
            float yMovement = event.getY() - mInitialY;
            mInitialX = event.getX();
            mInitialY = event.getY();
            updateMoveText(xMovement, yMovement);
        }
    }

    private void onDrawText(Canvas canvas, TextDrawer textObject) {
        if (textObject != null) {
            mTextFactory.onDraw(canvas, textObject);
        }
    }

    //Set background for Painter
    public void setBackground(Bitmap background) {
        mBitmapBackground = Bitmap.createBitmap(background, 0, 0, background.getWidth(), background.getHeight());
    }

    //Set isDrawing
    public void setIsDrawing(boolean isDrawing) {
        mIsDrawing = isDrawing;
    }

    public DrawingPainter getDrawingPainter() {
        return mDrawingPainter;
    }
}
