package com.asiantech.intern.painter.activities;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Copyright © 2016 YNC.
 * Created by Congybk on 8/30/2016.
 */
@EActivity
public abstract class BaseActivity extends AppCompatActivity {
    @AfterViews
    abstract void afterViews();
    public void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
