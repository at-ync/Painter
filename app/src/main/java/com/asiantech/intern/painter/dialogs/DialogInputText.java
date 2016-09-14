package com.asiantech.intern.painter.dialogs;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.asiantech.intern.painter.R;
import com.asiantech.intern.painter.activities.HomeActivity;
import com.asiantech.intern.painter.beans.TextDrawer;
import com.asiantech.intern.painter.interfaces.IAction;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


/**
 * Copyright @2016 AsianTech Inc.
 * Created by LyHV on 8/30/2016.
 */
@EFragment(R.layout.dialog_input_text)
public class DialogInputText extends DialogFragment {
    @ViewById(R.id.edtInputText)
    EditText mEditInputText;

    @AfterViews
    public void init() {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    @Click(R.id.tvCancel)
    public void onClickCancel() {
        dismiss();
    }

    private Paint getPaint(int size, int color) {
        Paint paint = new Paint();
        paint.setTextSize(size);
        paint.setColor(color);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(getTypeface(getString(R.string.typeface_arizonia_regular_ttf)));
        return paint;
    }

    private TextDrawer createNewTextObject(String content, Paint paint) {
        TextDrawer textObject = new TextDrawer();
        textObject.setAngle(0);
        textObject.setPaint(paint);
        textObject.setContent(content);
        return textObject;
    }

    private Typeface getTypeface(String name) {
        return Typeface.createFromAsset(getActivity().getAssets(), name);
    }
}
