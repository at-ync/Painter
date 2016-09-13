package com.asiantech.intern.painter.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.asiantech.intern.painter.beans.BitmapBackground;
import com.asiantech.intern.painter.beans.BitmapDrawer;
import com.asiantech.intern.painter.beans.Component;
import com.asiantech.intern.painter.beans.PathDrawer;
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
    private static final float DRAW_TOUCH_TOLERANCE = 4;
    private float mInitialX;
    private float mInitialY;
    private boolean mIsOnDraw;
    private List<Component> mComponents;
    private BitmapFactory mBitmapFactory;
    private TextFactory mTextFactory;
    private BitmapBackground mBitmapBackground;
    private int positionDrawCircle = -1;
    // TextDrawer Activities
    private float mCenterX;
    private float mCenterY;
    private int mAction;
    //Draw Activities
    private boolean mIsDrawing;
    private boolean mIsEraser;
    private boolean mIsDone;
    private PathDrawer mPathDrawer;
    private float mX, mY;
    //  private Bitmap mBitmapBackground;
    private Paint mPaintBackground;
    private Canvas mCanvas;
    private Bitmap mDrawingBitmap;

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
        mBitmapBackground = new BitmapBackground();
        this.setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDrawingBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mDrawingBitmap);
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
        if ((mIsDone && (mAction == Constant.ACTION_DRAWING || mAction == Constant.ACTION_ERASER))
                || (mAction != Constant.ACTION_DRAWING && mAction != Constant.ACTION_ERASER)) {
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            int size = mComponents.size();
            for (int i = 0; i < size; i++) {
                BitmapDrawer bitmapDrawer = mComponents.get(i).getBitmapDrawer();
                PathDrawer pathDrawer = mComponents.get(i).getPathDrawer();
                if (bitmapDrawer != null) {
                    mBitmapFactory.onDrawBitmap(mCanvas, bitmapDrawer);
                    if (i == positionDrawCircle) {
                        mBitmapFactory.onDrawCircleBitmap(mCanvas, bitmapDrawer);
                    }
                }
                if (pathDrawer != null) {
                    mCanvas.drawPath(pathDrawer.getPath(), pathDrawer.getPaint());
                }
            }
        } else {
            if (mPathDrawer != null) {
                if (mAction == Constant.ACTION_DRAWING) {
                    canvas.drawPath(mPathDrawer.getPath(), mPathDrawer.getPaint());
                } else {
                    mCanvas.drawPath(mPathDrawer.getPath(), mPathDrawer.getPaint());
                }
            }
        }
        canvas.drawBitmap(mDrawingBitmap, getWidth() - mDrawingBitmap.getWidth(), getHeight() - mDrawingBitmap.getHeight(), mPaintBackground);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initMoveBitmap(event);
                initRoteBitMap(event);
                onDrawInit(event);
                break;
            case MotionEvent.ACTION_UP:
                positionDrawCircle = -1;
                onDrawFinish();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                updateMoveBitmap(event);
                updateRotateBitmap(event);
                onDrawMove(event);
                break;
        }
        return true;
    }

    private void updateRotateBitmap(MotionEvent event) {
        float newX = event.getX();
        float newY = event.getY();
        if (mAction == Constant.ACTION_ROTATE_BITMAP) {
            for (int i = mComponents.size() - 1; i >= 0; i--) {
                BitmapDrawer bitmapDrawer = mComponents.get(i).getBitmapDrawer();
                if (bitmapDrawer != null && mBitmapFactory.checkTouchCircleBitmap(bitmapDrawer, newX, newY)) {
                    positionDrawCircle = i;
                    bitmapDrawer.setRotateAngle(bitmapDrawer.getRotateAngle()
                            + mBitmapFactory.getRotateAngle(bitmapDrawer, mInitialX, mInitialY, newX, newY));
                    invalidate();
                    mInitialX = newX;
                    mInitialY = newY;
                    break;
                }
            }
        }
    }

    private void initRoteBitMap(MotionEvent event) {
        if (mAction == Constant.ACTION_ROTATE_BITMAP) {
            mInitialX = event.getX();
            mInitialY = event.getY();
        }
    }


    private void onDrawInit(MotionEvent event) {
        if (mAction == Constant.ACTION_DRAWING || mAction == Constant.ACTION_ERASER) {
            float x = event.getX();
            float y = event.getY();
            Path path = new Path();
            path.moveTo(x, y);
            mX = x;
            mY = y;
            mPathDrawer = new PathDrawer();
            mPathDrawer.setPath(path);
            if (mAction == Constant.ACTION_ERASER) {
                mPathDrawer.getPaint().setAlpha(Color.TRANSPARENT);
                mPathDrawer.getPaint().setColor(Color.TRANSPARENT);
                mPathDrawer.getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            }
            mIsDone = false;
            invalidate();
        }
    }

    //
    @Override
    public void setAction(int action) {
        mAction = action;
    }

    @Override
    public void setBitmapDrawer(BitmapDrawer bitmapDrawer) {
        bitmapDrawer.setBitmapCoordinateX(mCenterX - bitmapDrawer.getBitmap().getWidth() / 2);
        bitmapDrawer.setBitmapCoordinateY(mCenterY - bitmapDrawer.getBitmap().getHeight() / 2);
        bitmapDrawer.setRotateOriginX(mCenterX);
        bitmapDrawer.setRotateOriginY(mCenterY);
        bitmapDrawer.setRadius((float) Math.sqrt(Math.pow(bitmapDrawer.getBitmap().getWidth() / 2, 2) + Math.pow(bitmapDrawer.getBitmap().getHeight() / 2, 2)));
        Component component = new Component();
        component.setBitmapDrawer(bitmapDrawer);
        mComponents.add(component);
        invalidate();
    }

    private void initMoveBitmap(MotionEvent event) {
        if (mAction == Constant.ACTION_MOVE_BITMAP) {
            mInitialX = event.getX();
            mInitialY = event.getY();
        }
    }

    public void updateMoveBitmap(MotionEvent event) {
        float newX = event.getX();
        float newY = event.getY();
        if (mAction == Constant.ACTION_MOVE_BITMAP) {
            for (int i = mComponents.size() - 1; i >= 0; i--) {
                BitmapDrawer bitmapDrawer = mComponents.get(i).getBitmapDrawer();
                if (bitmapDrawer != null && mBitmapFactory.checkTouchCircleBitmap(bitmapDrawer, newX, newY)) {
                    positionDrawCircle = i;
                    mBitmapFactory.updatePositionBitmap(mComponents.get(i).getBitmapDrawer(), newX - mInitialX, newY - mInitialY);
                    invalidate();
                    mInitialX = newX;
                    mInitialY = newY;
                    break;
                }
            }
        }
    }

    //

    private void onDrawMove(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (mAction == Constant.ACTION_DRAWING || mAction == Constant.ACTION_ERASER) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= DRAW_TOUCH_TOLERANCE || dy >= DRAW_TOUCH_TOLERANCE) {
                mPathDrawer.getPath().quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
            mIsDone = false;
            invalidate();
        }
    }

    private void onDrawFinish() {
        if (mAction == Constant.ACTION_DRAWING || mAction == Constant.ACTION_ERASER) {
            mPathDrawer.getPath().lineTo(mX, mY);
            Component component = new Component();
            component.setPathDrawer(mPathDrawer);
            mComponents.add(component);
            setLayerType(LAYER_TYPE_HARDWARE, mPaintBackground);
            if (mAction != Constant.ACTION_ERASER) {
                mCanvas.drawPath(mPathDrawer.getPath(), mPathDrawer.getPaint());
            }
            invalidate();
            mIsDone = true;
        }
    }

    public void setBackground(Bitmap background) {
        mBitmapBackground.setBitmap(Bitmap.createBitmap(background, 0, 0, background.getWidth(), background.getHeight()));
    }


}
