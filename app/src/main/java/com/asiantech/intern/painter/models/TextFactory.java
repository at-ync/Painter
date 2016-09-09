package com.asiantech.intern.painter.models;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.asiantech.intern.painter.beans.TextDrawer;
import com.asiantech.intern.painter.interfaces.ITextFactory;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Copyright @2016 AsianTech Inc.
 * Created by LyHV on 9/3/2016.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class TextFactory implements ITextFactory {
    private int mAction;

    @Override
    public void onDrawText(Canvas canvas, TextDrawer textObject) {
        canvas.drawText(textObject.getContent(), textObject.getCoordinatesX(), textObject.getCoordinatesY(), textObject.getPaint());
    }

    @Override
    public boolean isTouchInTextArea(TextDrawer textObject, float x, float y) {
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

    @Override
    public void updateCoordinatesText(TextDrawer textObject, float movementX, float movementY) {
        textObject.setCoordinatesX(textObject.getCoordinatesX() + movementX);
        textObject.setCoordinatesY(textObject.getCoordinatesY() + movementY);
    }

}
