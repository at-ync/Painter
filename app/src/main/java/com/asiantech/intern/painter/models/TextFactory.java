package com.asiantech.intern.painter.models;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.asiantech.intern.painter.beans.TextObject;

/**
 * Copyright @2016 AsianTech Inc.
 * Created by LyHV on 9/3/2016.
 */
public class TextFactory implements ITextFactory {
    @Override
    public void onDraw(Canvas canvas, TextObject textObject) {
        canvas.drawText(textObject.getContent(), textObject.getCoordinatesX(), textObject.getCoordinatesY(), textObject.getPaint());
    }

    @Override
    public boolean isTouchInTextArea(TextObject textObject, float x, float y) {
        String contentText = textObject.getContent();
        float oX = textObject.getCoordinatesX();
        float oY = textObject.getCoordinatesY();
        Rect rect = new Rect();
        textObject.getPaint().getTextBounds(contentText, 0, contentText.length(), rect);
        float width = rect.width();
        float height = rect.height();
        if (textObject.getPaint().getTextAlign() == Paint.Align.CENTER) {
            return (oX - width / 2 <= x && x <= width / 2 + oX && oY - height <= y && y <= oY);
        }
        return false;
    }
}
