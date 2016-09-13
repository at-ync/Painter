package com.asiantech.intern.painter.beans;

import android.graphics.Paint;
import android.graphics.Path;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by HungTQB on 12/09/2016.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class PathDrawer extends BaseDrawer{
    private Path path;
    private Paint paint;
}
