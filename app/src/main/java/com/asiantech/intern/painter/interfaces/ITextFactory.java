package com.asiantech.intern.painter.interfaces;

import android.graphics.Canvas;

import com.asiantech.intern.painter.beans.TextDrawer;

/**
 * Copyright @2016 AsianTech Inc.
 * Created by LyHV on 9/3/2016.
 */
public interface ITextFactory {
    void onDrawText(Canvas canvas, TextDrawer textObject);

    boolean isTouchInTextArea(TextDrawer textObject, float x, float y);

    void updateCoordinatesText(TextDrawer textObject, float movementX, float movementY);

}
