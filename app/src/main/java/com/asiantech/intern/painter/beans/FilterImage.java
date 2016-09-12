package com.asiantech.intern.painter.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Copyright © 2016 YNC.
 * Created by Congybk on 8/31/2016.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class FilterImage {
    private int typeFilter;
    private String nameFilter;
}
