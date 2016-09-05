package com.asiantech.intern.painter.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.asiantech.intern.painter.beans.Component;
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
public class Painter extends View implements ITextLab {
    private boolean isOnDraw = true;
    // Text Activities
    private TextFactory mTextFactory;
    private List<Component> mComponents;
    private float mCenterX;
    private float mCenterY;
    private int actionText;
    float initialX;
    float initialY;
    private Matrix matrix;
    // Image Activities

    public Painter(Context context) {
        super(context);
    }

    public Painter(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTextFactory = new TextFactory();
        mComponents = new ArrayList<>();
        matrix = new Matrix();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int size = mComponents.size();
        for (int i = 0; i < size; i++) {
            Component component = mComponents.get(i);
            if (component.getTextObject() != null) {
                TextObject textObject = component.getTextObject();
                // Draw
                if (actionText == Action.ROTATE) {
                    mTextFactory.onRotateText(textObject, canvas, matrix);
                } else {
                   // canvas.rotate(-textObject.getAngle());
                    mTextFactory.onDraw(canvas, textObject);
                }


            }

            if (component.getImageObject() != null) {
                // Draw
            }

            if (component.getPathObject() != null) {
                // Draw
            }
        }
        if (isOnDraw) {
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (actionText == Action.MOVE) {
                    initialX = event.getX();
                    initialY = event.getY();
                }
                if (actionText == Action.ROTATE) {
                    initialX = event.getX();
                    initialY = event.getY();
                    updateMoveText(initialX, initialY);
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_MOVE:
                switch (actionText) {
                    case Action.MOVE:
                        float xMovement = event.getX() - initialX;
                        float yMovement = event.getY() - initialY;
                        initialX = event.getX();
                        initialY = event.getY();
                        updateMoveText(xMovement, yMovement);
                        break;
                    case Action.ROTATE:
                      /*  initialX = event.getX();
                        initialY = event.getY();
                        updateRotateText();*/
                        break;
                }
                break;
        }
        return true;
    }

    @Override
    public void setTextObject(TextObject textObject) {
        synchronized (mComponents) {
            if (textObject == null) return;
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

    private void updateRotateText() {
        synchronized (mComponents) {
            for (int i = mComponents.size() - 1; i >= 0; i--) {
                TextObject textObject = mComponents.get(i).getTextObject();
                if (textObject != null) {
                    if (mTextFactory.isTouchInTextArea(textObject, initialX, initialY)) {
                        mTextFactory.updateAngle(textObject, 4);
                        break;
                    }
                }

            }
        }
    }
}
