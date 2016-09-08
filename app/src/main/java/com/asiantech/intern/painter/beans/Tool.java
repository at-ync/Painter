package com.asiantech.intern.painter.beans;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Copyright © 2016 YNC.
 * Created by Congybk on 9/6/2016.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Tool {
    private int iconTool;
    public Tool(int iconTool){
        this.iconTool = iconTool;
    }
}
