package com.asiantech.intern.painter.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.asiantech.intern.painter.R;
import com.asiantech.intern.painter.adapters.CirclePathAdapter;
import com.asiantech.intern.painter.utils.ClickItemRecyclerView;
import com.asiantech.intern.painter.utils.IClickItemRecyclerView;
import com.asiantech.intern.painter.views.CustomCirclePath;
import com.asiantech.intern.painter.views.CustomCirclePath_;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by HungTQB on 13/09/2016.
 */
@EFragment(R.layout.dialog_path_color)
public class DialogPathColor extends DialogFragment {
    @ViewById(R.id.recyclerViewPathRadius)
    RecyclerView mRecyclerViewPathRadius;
    @ViewById(R.id.btnOk)
    Button mBtnOk;
    @ViewById(R.id.btnCancel)
    Button mBtnCancel;
    @FragmentArg
    int mColor;
    @FragmentArg
    int mRadius;
    @ViewById(R.id.customColorPicker)
    ColorPickerView mCustomColorPicker;
    private IOnPickPathStyle mOnPickPathStyle;
    private List<CustomCirclePath_> mCustomCirclePaths;
    private CirclePathAdapter mCirclePathAdapter;

    @AfterViews
    void init() {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        initListCirclePath();
        setColorPathRadius(Color.WHITE);
        mCirclePathAdapter = new CirclePathAdapter(mCustomCirclePaths);
        mRecyclerViewPathRadius.setAdapter(mCirclePathAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewPathRadius.setLayoutManager(layoutManager);
        mRecyclerViewPathRadius.addOnItemTouchListener(new ClickItemRecyclerView(getContext(), mRecyclerViewPathRadius, new IClickItemRecyclerView() {
            @Override
            public void onClick(View view, int position) {
                clearSelectedRadius();
                CustomCirclePath customCirclePath = mCustomCirclePaths.get(position);
                mRadius = customCirclePath.getCircleRadius();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        mCustomColorPicker.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                mColor = color;
                setColorPathRadius(color);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IOnPickPathStyle) {
            mOnPickPathStyle = (IOnPickPathStyle) context;
        }
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
        for (CustomCirclePath customCirclePath : mCustomCirclePaths) {
            customCirclePath.setBackground(null);
        }
    }

    private void setColorPathRadius(int color) {
        for (CustomCirclePath customCirclePath : mCustomCirclePaths) {
            customCirclePath.setCircleColor(color);
        }
        if (mCirclePathAdapter != null) {
            mCirclePathAdapter.notifyDataSetChanged();
        }
    }

    private void initListCirclePath() {
        mCustomCirclePaths = new ArrayList<>();
        mCustomCirclePaths.add(new CustomCirclePath_(getContext(), getContext().getResources().getDimensionPixelSize(R.dimen.circle_radius_5)));
        mCustomCirclePaths.add(new CustomCirclePath_(getContext(), getContext().getResources().getDimensionPixelSize(R.dimen.circle_radius_6)));
        mCustomCirclePaths.add(new CustomCirclePath_(getContext(), getContext().getResources().getDimensionPixelSize(R.dimen.circle_radius_7)));
        mCustomCirclePaths.add(new CustomCirclePath_(getContext(), getContext().getResources().getDimensionPixelSize(R.dimen.circle_radius_8)));
        mCustomCirclePaths.add(new CustomCirclePath_(getContext(), getContext().getResources().getDimensionPixelSize(R.dimen.circle_radius_9)));
        mCustomCirclePaths.add(new CustomCirclePath_(getContext(), getContext().getResources().getDimensionPixelSize(R.dimen.circle_radius_10)));
        mCustomCirclePaths.add(new CustomCirclePath_(getContext(), getContext().getResources().getDimensionPixelSize(R.dimen.circle_radius_11)));
    }

    public interface IOnPickPathStyle {
        void onPicked(int color, int radius);
    }
}
