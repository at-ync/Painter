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
    private static final float DRAW_TOUCH_TOLERANCE = 4;
    private float mInitialX;
    private float mInitialY;
    private boolean mIsOnDraw;
    private List<Component> mComponents;
    private BitmapFactory mBitmapFactory;
    private TextFactory mTextFactory;
    private BitmapBackground mBitmapBackground;
    // TextDrawer Activities
    private float mCenterX;
    private float mCenterY;
    private int mActionText;
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
        mDrawingBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mDrawingBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;
        // Draw Background
        if (!mBitmapBackground.isSetting()) {
            mBitmapBackground.setLeft((getWidth() - mBitmapBackground.getBitmap().getWidth()) / 2);
            mBitmapBackground.setTop((getHeight() - mBitmapBackground.getBitmap().getHeight()) / 2);
            mBitmapBackground.setSetting(true);
        }
        canvas.drawBitmap(mBitmapBackground.getBitmap(), mBitmapBackground.getLeft(), mBitmapBackground.getTop(), mBitmapBackground.getPaint());
        if (mIsDone) {
            for (Component comp : mComponents) {
                TextDrawer textDrawer = comp.getTextDrawer();
                BitmapDrawer bitmapDrawer = comp.getBitmapDrawer();
                PathDrawer pathDrawer = comp.getPathDrawer();
                if (textDrawer != null) {
                    mTextFactory.onDrawText(canvas, textDrawer);
                }
                if (bitmapDrawer != null) {
                    mBitmapFactory.setBitmapDrawer(bitmapDrawer);
                    mBitmapFactory.onDrawBitmap(canvas);
                }
                if (pathDrawer != null) {
                    mCanvas.drawPath(pathDrawer.getPath(), pathDrawer.getPaint());
                }
            }
        }
        if (mPathDrawer != null) {
            if (!mIsEraser) {
                canvas.drawPath(mPathDrawer.getPath(), mPathDrawer.getPaint());
            } else {
                mCanvas.drawPath(mPathDrawer.getPath(), mPathDrawer.getPaint());
            }
        }
        canvas.drawBitmap(mDrawingBitmap, getWidth() - mDrawingBitmap.getWidth(), getHeight() - mDrawingBitmap.getHeight(), mPaintBackground);
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
        invalidate();
        return true;
    }

    @Override
    public void setTextDrawer(TextDrawer textDrawer) {
        synchronized (mComponents) {
            Component component = new Component();
            textDrawer.setCoordinatesX(mCenterX);
            textDrawer.setCoordinatesY(mCenterY);
            component.setTextDrawer(textDrawer);
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
        if (mIsDrawing) {
            float x = event.getX();
            float y = event.getY();
            Path path = new Path();
            path.moveTo(x, y);
            mX = x;
            mY = y;
            mPathDrawer = new PathDrawer();
            mPathDrawer.setPath(path);
            if (mIsEraser) {
                mPathDrawer.getPaint().setAlpha(Color.TRANSPARENT);
                mPathDrawer.getPaint().setColor(Color.TRANSPARENT);
                mPathDrawer.getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            }
            mIsDone = false;
        }
    }

    private void onDrawMove(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (mIsDrawing) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= DRAW_TOUCH_TOLERANCE || dy >= DRAW_TOUCH_TOLERANCE) {
                mPathDrawer.getPath().quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
            mIsDone = false;
        }
    }

    private void onDrawFinish() {
        if (mIsDrawing) {
            mPathDrawer.getPath().lineTo(mX, mY);
            Component component = new Component();
            component.setPathDrawer(mPathDrawer);
            mComponents.add(component);
            setLayerType(LAYER_TYPE_HARDWARE, mPaintBackground);
            if (!mIsEraser) {
                mCanvas.drawPath(mPathDrawer.getPath(), mPathDrawer.getPaint());
            }
            mIsDone = true;
        }
    }

    private void initMove(MotionEvent event) {
        if (mActionText == Constant.ACTION_MOVE) {
            mInitialX = event.getX();
            mInitialY = event.getY();
        }
    }

    private void updateMove(MotionEvent event) {
        if (mActionText == Constant.ACTION_MOVE) {
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

    public void setIsDrawing(boolean isDrawing) {
        mIsDrawing = isDrawing;
    }

    public void setIsEraser(boolean isEraser) {
        mIsEraser = isEraser;
    }
}
