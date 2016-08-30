package com.asiantech.intern.painter.fragments;

import android.support.v4.app.Fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

/**
 * Copyright © 2016 YNC.
 * Created by Congybk on 8/30/2016.
 */
@EFragment
public abstract class BaseFragment extends Fragment {
    @AfterViews
    abstract void afterViews();
}
