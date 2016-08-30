package com.asiantech.intern.painter.activitys;

import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Copyright © 2016 YNC.
 * Created by Congybk on 8/30/2016.
 */
@EActivity
public abstract class BaseActivity extends AppCompatActivity {
    abstract void afterViews();

    @AfterViews
    void init() {
        afterViews();
    }
}
