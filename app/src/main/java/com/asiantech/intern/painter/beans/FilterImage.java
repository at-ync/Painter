package com.asiantech.intern.painter.beans;

import lombok.Data;

/**
 * Copyright © 2016 YNC.
 * Created by Congybk on 8/31/2016.
 */
@Data
public class FilterImage {
    private int typeFilter;
    private String nameFilter;

    public FilterImage(int typeFilter, String nameFilter) {
        this.typeFilter = typeFilter;
        this.nameFilter = nameFilter;
    }
}
