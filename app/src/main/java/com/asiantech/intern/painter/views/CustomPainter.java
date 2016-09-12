package com.asiantech.intern.painter.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.asiantech.intern.painter.beans.BitmapBackground;
import com.asiantech.intern.painter.beans.BitmapDrawer;
import com.asiantech.intern.painter.beans.Component;
import com.asiantech.intern.painter.beans.DrawingPainter;
import com.asiantech.intern.painter.beans.TextDrawer;
import com.asiantech.intern.painter.commons.Constant;
import com.asiantech.intern.painter.interfaces.IAction;
import com.asiantech.intern.painter.models.BitmapFactory;
import com.asiantech.intern.painter.models.TextFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright @2016 AsianTech Inc.
 * Created by LyHV on 8/31/2016.
 */
public class CustomPainter extends View implements IAction {
    private float mInitialX;
    private float mInitialY;
    private boolean mIsOnDraw = true;
    private List<Component> mComponents;
    private BitmapFactory mBitmapFactory;
    private TextFactory mTextFactory;
    private BitmapBackground mBitmapBackground;
    private int mAction;
    private int mCenterX;
    private int mCenterY;
    //Draw Activities
    private boolean mIsDrawing;
    //  private Bitmap mBitmapBackground;
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

    public void init() {
        mTextFactory = new TextFactory();
        mBitmapFactory = new BitmapFactory();
        mComponents = new ArrayList<>();
        mPaintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDrawingPainter = new DrawingPainter();
        mBitmapBackground = new BitmapBackground();
        this.setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw Background
        if (!mBitmapBackground.isSetting()) {
            mCenterX = getWidth() / 2;
            mCenterY = getHeight() / 2;
            mBitmapBackground.setLeft((getWidth() - mBitmapBackground.getBitmap().getWidth()) / 2);
            mBitmapBackground.setTop((getHeight() - mBitmapBackground.getBitmap().getHeight()) / 2);
            mBitmapBackground.setSetting(true);
        }
        canvas.drawBitmap(mBitmapBackground.getBitmap(), mBitmapBackground.getLeft(), mBitmapBackground.getTop(), mBitmapBackground.getPaint());

        // Draw other component
        int size = mComponents.size();
        for (int i = 0; i < size; i++) {
            BitmapDrawer bitmapDrawer = mComponents.get(i).getBitmapDrawer();
            if (bitmapDrawer != null) {
                mBitmapFactory.onDrawBitmap(canvas, bitmapDrawer);
            }
        }
        if (mIsOnDraw) {
            invalidate();
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
    public void setActionText(int action) {
        mAction = action;
    }

    @Override
    public void setBitmapDrawer(BitmapDrawer bitmapDrawer) {
        synchronized (mComponents) {
            bitmapDrawer.setBitmapCoordinateX(mCenterX - bitmapDrawer.getBitmap().getWidth() / 2);
            bitmapDrawer.setBitmapCoordinateY(mCenterY - bitmapDrawer.getBitmap().getHeight() / 2);
            bitmapDrawer.setRotateOriginX(mCenterX);
            bitmapDrawer.setRotateOriginY(mCenterY);
            Component component = new Component();
            component.setBitmapDrawer(bitmapDrawer);
            mComponents.add(component);
        }
    }


    private void updateMoveText(float movementX, float movementY) {
        synchronized (mComponents) {
            for (int i = mComponents.size() - 1; i >= 0; i--) {
                TextDrawer textObject = mComponents.get(i).getTextDrawer();
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
        if (mAction == Constant.ACTION_MOVE) {
            mInitialX = event.getX();
            mInitialY = event.getY();
        }
    }

    private void updateMove(MotionEvent event) {
        if (mAction == Constant.ACTION_MOVE) {
            float xMovement = event.getX() - mInitialX;
            float yMovement = event.getY() - mInitialY;
            mInitialX = event.getX();
            mInitialY = event.getY();
            updateMoveText(xMovement, yMovement);
        }
    }


    //Set background for Painter
    public void setBackground(Bitmap background) {
        mBitmapBackground.setBitmap(Bitmap.createBitmap(background, 0, 0, background.getWidth(), background.getHeight()));
    }
}
