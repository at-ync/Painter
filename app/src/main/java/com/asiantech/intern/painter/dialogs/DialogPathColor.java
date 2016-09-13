package com.asiantech.intern.painter.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.asiantech.intern.painter.R;
import com.asiantech.intern.painter.views.CustomCirclePath;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by HungTQB on 13/09/2016.
 */
@EFragment(R.layout.dialog_path_color)
public class DialogPathColor extends DialogFragment {
    @ViewById(R.id.btnPickColor)
    ImageButton mBtnPickColor;
    @ViewById(R.id.pathRadius5)
    CustomCirclePath mPathRadius5;
    @ViewById(R.id.pathRadius6)
    CustomCirclePath mPathRadius6;
    @ViewById(R.id.pathRadius7)
    CustomCirclePath mPathRadius7;
    @ViewById(R.id.pathRadius8)
    CustomCirclePath mPathRadius8;
    @ViewById(R.id.pathRadius9)
    CustomCirclePath mPathRadius9;
    @ViewById(R.id.pathRadius10)
    CustomCirclePath mPathRadius10;
    @ViewById(R.id.pathRadius11)
    CustomCirclePath mPathRadius11;
    @ViewById(R.id.btnOk)
    Button mBtnOk;
    @ViewById(R.id.btnCancel)
    Button mBtnCancel;
    @ViewById(R.id.tvColorReview)
    TextView mTvColorReview;
    @FragmentArg
    int mColor;
    @FragmentArg
    int mRadius;
    private IOnPickPathStyle mOnPickPathStyle;

    @AfterViews
    void init() {
        setColorPathRadius(mColor);
        mTvColorReview.setBackgroundColor(mColor);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnPickPathStyle = (IOnPickPathStyle) context;
    }

    @Click(R.id.btnPickColor)
    void onPickColor() {
        ColorPickerDialogBuilder
                .with(getActivity())
                .setTitle(getString(R.string.dialog_pick_color_title))
                .initialColor(-1)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    }
                })
                .setPositiveButton(getString(R.string.dialog_pick_color_button_text_ok), new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        mColor = selectedColor;
                        setColorPathRadius(selectedColor);
                        mTvColorReview.setBackgroundColor(selectedColor);
                    }
                })
                .setNegativeButton(getString(R.string.dialog_pick_color_button_text_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    @Click(R.id.pathRadius5)
    void onClickPathRadius5() {
        clearSelectedRadius();
        mRadius = getRadius(mPathRadius5);
    }


    @Click(R.id.pathRadius6)
    void onClickPathRadius6() {
        clearSelectedRadius();
        mRadius = getRadius(mPathRadius6);
    }

    @Click(R.id.pathRadius7)
    void onClickPathRadius7() {
        clearSelectedRadius();
        mRadius = getRadius(mPathRadius7);
    }

    @Click(R.id.pathRadius8)
    void onClickPathRadius8() {
        clearSelectedRadius();
        mRadius = getRadius(mPathRadius8);
    }

    @Click(R.id.pathRadius9)
    void onClickPathRadius9() {
        clearSelectedRadius();
        mRadius = getRadius(mPathRadius9);
    }

    @Click(R.id.pathRadius10)
    void onClickPathRadius10() {
        clearSelectedRadius();
        mRadius = getRadius(mPathRadius10);
    }

    @Click(R.id.pathRadius11)
    void onClickPathRadius11() {
        clearSelectedRadius();
        mRadius = getRadius(mPathRadius11);
    }

    @Click(R.id.btnOk)
    void onClickButtonOk() {
        mOnPickPathStyle.onPicked(mColor, mRadius);
        this.dismiss();
    }

    @Click(R.id.btnCancel)
    void onClickButtonCancel() {
        this.dismiss();
    }

    private void clearSelectedRadius() {
        mPathRadius5.setBackground(null);
        mPathRadius6.setBackground(null);
        mPathRadius7.setBackground(null);
        mPathRadius8.setBackground(null);
        mPathRadius9.setBackground(null);
        mPathRadius10.setBackground(null);
        mPathRadius11.setBackground(null);
    }

    private void setColorPathRadius(int color) {
        mPathRadius5.setCircleColor(color);
        mPathRadius6.setCircleColor(color);
        mPathRadius7.setCircleColor(color);
        mPathRadius8.setCircleColor(color);
        mPathRadius9.setCircleColor(color);
        mPathRadius10.setCircleColor(color);
        mPathRadius11.setCircleColor(color);
    }

    private int getRadius(CustomCirclePath customCirclePath) {
        customCirclePath.setBackgroundColor(Color.GREEN);
        return customCirclePath.getCircleRadius();
    }

    public interface IOnPickPathStyle {
        void onPicked(int color, int radius);
    }
}
