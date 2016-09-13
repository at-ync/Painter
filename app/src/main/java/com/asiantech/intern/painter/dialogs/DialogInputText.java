package com.asiantech.intern.painter.dialogs;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.asiantech.intern.painter.R;
import com.asiantech.intern.painter.activities.HomeActivity;
import com.asiantech.intern.painter.beans.BitmapDrawer;
import com.asiantech.intern.painter.beans.TextDrawer;
import com.asiantech.intern.painter.interfaces.IAction;
import com.asiantech.intern.painter.models.BitmapFactory;
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
    @ViewById(R.id.btnCancel)
    Button btnCancel;
    @ViewById(R.id.btnOk)
    Button btnOk;
    @ViewById(R.id.edtInputText)
    EditText mEditInputText;
    @ViewById(R.id.edtSizeText)
    EditText mEdtSizeText;
    @ViewById(R.id.imgPickColor)
    ImageView mImgPickColor;
    @ViewById(R.id.edtCodeColor)
    EditText mEdtCodeColor;
    private int mColorText = Color.BLACK;
    private IAction mIAction;
    private BitmapFactory mBitmapFactory;

    @AfterViews
    public void init() {
        getDialog().setTitle(R.string.dialog_input_text_title);
        mBitmapFactory = new BitmapFactory();
        mEdtCodeColor.setBackgroundColor(mColorText);
        if (getActivity() instanceof HomeActivity) {
            mIAction = (IAction) getActivity();
        }
    }

    @Click(R.id.btnCancel)
    public void onClickCancel() {
        dismiss();
    }

    @Click(R.id.btnOk)
    public void onClickOk() {
        String content = mEditInputText.getText().toString();
        String sizeText = mEdtSizeText.getText().toString().trim();
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(sizeText)) {
            return;
        }
        int size = Integer.parseInt(sizeText);
        TextDrawer textDrawer = createNewTextObject(content, getPaint(size, mColorText));
        BitmapDrawer bitmapDrawer = mBitmapFactory.convertTextToBitmap(textDrawer);
        mIAction.setBitmapDrawer(bitmapDrawer);
        onClickCancel();
    }

    @Click(R.id.imgPickColor)
    public void setImgPickColor() {
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
                        mColorText = selectedColor;
                        mEdtCodeColor.setBackgroundColor(selectedColor);
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

    private Paint getPaint(int size, int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(size);
        paint.setColor(color);
        paint.setTypeface(getTypeface(getString(R.string.typeface_arizonia_regular_ttf)));
        return paint;
    }

    private TextDrawer createNewTextObject(String content, Paint paint) {
        TextDrawer textObject = new TextDrawer();
        textObject.setPaint(paint);
        textObject.setContent(content);
        return textObject;
    }

    private Typeface getTypeface(String name) {
        return Typeface.createFromAsset(getActivity().getAssets(), name);
    }
}
