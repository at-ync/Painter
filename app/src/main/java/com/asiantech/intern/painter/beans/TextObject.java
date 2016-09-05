package com.asiantech.intern.painter.beans;

import android.graphics.Paint;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Copyright @2016 AsianTech Inc.
 * Created by LyHV on 9/3/2016.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class TextObject {
    private float coordinatesX;
    private float coordinatesY;
    private String content;
    private Paint paint;
    private float angle;
}
