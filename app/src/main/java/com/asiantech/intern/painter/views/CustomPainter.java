package com.asiantech.intern.painter.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.asiantech.intern.painter.beans.Component;
import com.asiantech.intern.painter.beans.DrawingPainter;
import com.asiantech.intern.painter.beans.TextObject;
import com.asiantech.intern.painter.commo.Action;
import com.asiantech.intern.painter.interfaces.ITextLab;
import com.asiantech.intern.painter.models.TextFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright @2016 AsianTech Inc.
 * Created by LyHV on 8/31/2016.
 */
public class CustomPainter extends View implements ITextLab {
    float initialX;
    float initialY;
    private boolean isOnDraw = true;
    // Text Activities
    private TextFactory mTextFactory;
    private List<Component> mComponents;
    private float mCenterX;
    private float mCenterY;
    private int actionText;
    //Draw Activities
    private boolean mIsDrawing;
    private Bitmap mBitmapBackground;
    private Paint mPaintBackground;
    private DrawingPainter mDrawingPainter;
    // Image Activities

    public CustomPainter(Context context) {
        super(context);
        init();
    }

    public CustomPainter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mTextFactory = new TextFactory();
        mComponents = new ArrayList<>();
        mPaintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDrawingPainter = new DrawingPainter();
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mBitmapBackground != null) {
            setMeasuredDimension(mBitmapBackground.getWidth(), mBitmapBackground.getHeight());
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int size = mComponents.size();
        for (int i = 0; i < size; i++) {
            Component component = mComponents.get(i);
            onDrawText(canvas, component.getTextObject());
        }
        if (isOnDraw) {
            invalidate();
        }
        canvas.drawBitmap(mDrawingPainter.getBitmap(), 0, 0, mPaintBackground);
        if(mIsDrawing){
            canvas.drawPath(mDrawingPainter.getPath(), mDrawingPainter.getPaint());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initMove(event);
                if (mIsDrawing) {
                    mDrawingPainter.onTouchStart(x, y);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsDrawing) {
                    mDrawingPainter.onTouchUp();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(mIsDrawing){
                    mDrawingPainter.onTouchMove(x, y);
                }
                updateMove(event);
                break;
        }
        return true;
    }

    @Override
    public void setTextObject(TextObject textObject) {
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
        actionText = action;
    }


    private void updateMoveText(float movementX, float movementY) {
        synchronized (mComponents) {
            for (int i = mComponents.size() - 1; i >= 0; i--) {
                TextObject textObject = mComponents.get(i).getTextObject();
                if (textObject != null) {
                    if (mTextFactory.isTouchInTextArea(textObject, initialX, initialY)) {
                        mTextFactory.updateCoordinatesText(textObject, movementX, movementY);
                        break;
                    }
                }

            }
        }

    }

    private void initMove(MotionEvent event) {
        if (actionText == Action.MOVE) {
            initialX = event.getX();
            initialY = event.getY();
        }
    }

    private void updateMove(MotionEvent event) {
        if (actionText == Action.MOVE) {
            float xMovement = event.getX() - initialX;
            float yMovement = event.getY() - initialY;
            initialX = event.getX();
            initialY = event.getY();
            updateMoveText(xMovement, yMovement);
        }
    }

    private void onDrawText(Canvas canvas, TextObject textObject) {
        if (textObject != null) {
            mTextFactory.onDraw(canvas, textObject);
        }
    }

    //Set background for Painter
    public void setBackground(Bitmap background) {
        this.mBitmapBackground = background;
        this.setBackground(new BitmapDrawable(getResources(), mBitmapBackground));
    }

    //Set isDrawing
    public void isDrawing(boolean isDrawing){
        this.mIsDrawing = isDrawing;
    }
}
