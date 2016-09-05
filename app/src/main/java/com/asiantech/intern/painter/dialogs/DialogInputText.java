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
import com.asiantech.intern.painter.beans.TextObject;
import com.asiantech.intern.painter.interfaces.ITextLab;
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
    int colorText = Color.BLACK;
    ITextLab iTextLab;

    @AfterViews
    public void init() {
        getDialog().setTitle("Input Text");
        mEdtCodeColor.setBackgroundColor(colorText);
        iTextLab = (ITextLab) getActivity();
    }

    @Click(R.id.btnCancel)
    public void dismiss() {
        getDialog().dismiss();
    }

    @Click(R.id.btnOk)
    public void getText() {
        String content = mEditInputText.getText().toString();
        String size = mEdtSizeText.getText().toString();
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(size))
            return;
        int sizeText = Integer.parseInt(size);
        TextObject textObject = createNewTextObject(content, getPaint(sizeText, colorText));
        iTextLab.setTextObject(textObject);
        dismiss();
    }

    @Click(R.id.imgPickColor)
    public void setImgPickColor() {
        ColorPickerDialogBuilder
                .with(getActivity())
                .setTitle("Choose color")
                .initialColor(-1)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {

                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        colorText = selectedColor;
                        mEdtCodeColor.setBackgroundColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .build()
                .show();
    }

    private Paint getPaint(int size, int color) {
        Paint paint = new Paint();
        paint.setTextSize(size);
        paint.setColor(color);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "arizonia_regular.ttf");
        paint.setTypeface(typeface);
        return paint;
    }

    private TextObject createNewTextObject(String content, Paint paint) {
        TextObject textObject = new TextObject();
        textObject.setAngle(0);
        textObject.setPaint(paint);
        textObject.setContent(content);
        return textObject;
    }
}
