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
import android.view.ScaleGestureDetector;
import android.view.View;

import com.asiantech.intern.painter.beans.BitmapBackground;
import com.asiantech.intern.painter.beans.BitmapDrawer;
import com.asiantech.intern.painter.beans.Component;
import com.asiantech.intern.painter.beans.PathDrawer;
import com.asiantech.intern.painter.commons.Constant;
import com.asiantech.intern.painter.interfaces.IAction;
import com.asiantech.intern.painter.models.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright @2016 AsianTech Inc.
 * Created by LyHV on 8/31/2016.
 */
public class CustomPainter extends View implements IAction {
    private float mValueScale = 1f;
    private ScaleGestureDetector mScaleDetector;
    private static final float DRAW_TOUCH_TOLERANCE = 4;
    private float mInitialX;
    private float mInitialY;
    private List<Component> mComponents;
    private BitmapFactory mBitmapFactory;
    private BitmapBackground mBitmapBackground;
    private int mIndexDrawCircle = -1;
    private int mIndexScale = -1;
    // TextDrawer Activities
    private float mCenterX;
    private float mCenterY;
    private int mAction;
    //Draw Activities
    private boolean mIsDone;
    private PathDrawer mPathDrawer;
    private float mX, mY;
    private int mPathColor = Color.BLACK;
    private int mPathRadius;
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
        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
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
                    if (i == mIndexDrawCircle || i == mIndexScale) {
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
        mScaleDetector.onTouchEvent(event);
        int numTouch = event.getPointerCount();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                initMoveBitmap(event);
                initRoteBitMap(event);
                onDrawInit(event);
                checkSelectScale(event);
                break;
            case MotionEvent.ACTION_UP:
                mIndexDrawCircle = -1;
                if (mAction != Constant.ACTION_SCALE) {
                    mIndexScale = -1;
                }
                onDrawFinish();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                updateMoveBitmap(event);
                updateRotateBitmap(event);
                onDrawMove(event);
                updateScale(numTouch);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;

        }
        return true;
    }

    private void checkSelectScale(MotionEvent event) {
        if (mAction == Constant.ACTION_SCALE) {
            for (int i = mComponents.size() - 1; i >= 0; i--) {
                BitmapDrawer bitmapDrawer = mComponents.get(i).getBitmapDrawer();
                if (bitmapDrawer != null && mBitmapFactory.checkTouchCircleBitmap(bitmapDrawer, event.getX(), event.getY())) {
                    mIndexScale = i;
                    break;
                }
            }
        }

    }

    private void updateScale(int numTouch) {
        if (mAction == Constant.ACTION_SCALE) {
            if (numTouch == 2) {
                for (int i = mComponents.size() - 1; i >= 0; i--) {
                    BitmapDrawer bitmapDrawer = mComponents.get(i).getBitmapDrawer();
                    if (bitmapDrawer != null && i == mIndexScale) {
                        bitmapDrawer.setScale(mValueScale);
                        mBitmapFactory.updatePositionBitmapAfterScale(bitmapDrawer, mValueScale);
                        mIndexDrawCircle = i;
                        invalidate();
                        break;
                    }
                }
            }
        }

    }

    private void updateRotateBitmap(MotionEvent event) {
        float newX = event.getX();
        float newY = event.getY();
        if (mAction == Constant.ACTION_ROTATE_BITMAP) {
            for (int i = mComponents.size() - 1; i >= 0; i--) {
                BitmapDrawer bitmapDrawer = mComponents.get(i).getBitmapDrawer();
                if (bitmapDrawer != null && mBitmapFactory.checkTouchCircleBitmap(bitmapDrawer, newX, newY)) {
                    mIndexDrawCircle = i;
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
            mPathDrawer.getPaint().setColor(mPathColor);
            if (mPathRadius > 0) {
                mPathDrawer.getPaint().setStrokeWidth(mPathRadius);
            }
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
                    mIndexDrawCircle = i;
                    mBitmapFactory.updatePositionBitmap(mComponents.get(i).getBitmapDrawer(), newX - mInitialX, newY - mInitialY);
                    invalidate();
                    mInitialX = newX;
                    mInitialY = newY;
                    break;
                }
            }
        }
    }

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
        invalidate();
    }

    public void setPathColor(int pathColor) {
        this.mPathColor = pathColor;
    }

    public void setPathRadius(int pathRadius) {
        this.mPathRadius = pathRadius;
    }

    public int getPathColor() {
        return mPathColor;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mValueScale *= detector.getScaleFactor();
            mValueScale = Math.max(Constant.MIN_SCALE, Math.min(mValueScale, Constant.MAX_SCALE));
            return true;
        }
    }
}
