package com.asiantech.intern.painter.beans;

import lombok.Data;

/**
 * Copyright © 2016 YNC.
 * Created by Congybk on 8/31/2016.
 */
@Data
public class FilterImage {
    private int filterType;
    private String nameFilter;

    public FilterImage(int filterType, String nameFilter) {
        this.filterType = filterType;
        this.nameFilter = nameFilter;
    }
}
