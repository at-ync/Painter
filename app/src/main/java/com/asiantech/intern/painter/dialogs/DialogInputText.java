package com.asiantech.intern.painter.dialogs;

import android.app.DialogFragment;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.asiantech.intern.painter.R;
import com.asiantech.intern.painter.activities.HomeActivity;
import com.asiantech.intern.painter.adapters.SpinerFontAdapter;
import com.asiantech.intern.painter.beans.TextDrawer;
import com.asiantech.intern.painter.interfaces.IAction;
import com.asiantech.intern.painter.models.BitmapFactory;
import com.flask.colorpicker.ColorPickerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;


/**
 * Copyright @2016 AsianTech Inc.
 * Created by LyHV on 8/30/2016.
 */
@EFragment(R.layout.dialog_input_text)
public class DialogInputText extends DialogFragment implements AdapterView.OnItemSelectedListener {
    private static final int SIZE_CHANGE = 50;
    @ViewById(R.id.edtInputText)
    EditText mEditInputText;
    @ViewById(R.id.customColorPicker)
    ColorPickerView mCustomColorPicker;
    @ViewById(R.id.tvSizeText)
    TextView mTvSizeText;
    @ViewById(R.id.seekBar)
    SeekBar mSeekBar;
    @ViewById(R.id.spinerFont)
    Spinner mSpinerFont;
    private IAction mIAction;
    private int mSizeText;
    private String mFont;
    private String[] mFonts;

    @AfterViews
    public void init() {
        mFonts = getResources().getStringArray(R.array.font);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (getActivity() instanceof HomeActivity) {
            mIAction = (IAction) getActivity();
        }
        SpinerFontAdapter spinnerAdapter = new SpinerFontAdapter(getActivity(), R.id.tvRow, mFonts);
        mSpinerFont.setAdapter(spinnerAdapter);
        mSpinerFont.setOnItemSelectedListener(this);
        mSeekBar.setProgress(SIZE_CHANGE);
        mSizeText = SIZE_CHANGE + SIZE_CHANGE;
        updateTextViewSize(mSizeText);
        handlerSeekBarChangeSizeText();
    }

    @Click(R.id.tvCancel)
    public void onClickCancel() {
        dismiss();
    }

    @Click(R.id.tvOk)
    public void onClickOk() {
        String contentText = mEditInputText.getText().toString().trim();
        if (TextUtils.isEmpty(contentText)) {
            return;
        }
        TextDrawer textDrawer = createTextDrawer(contentText, getPaint(mSizeText, mCustomColorPicker.getSelectedColor(),mFont));
        BitmapFactory bitmapFactory = new BitmapFactory();
        mIAction.setBitmapDrawer(bitmapFactory.convertTextToBitmap(textDrawer));
        dismiss();
    }

    private Paint getPaint(int size, int color,String font) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(size);
        paint.setColor(color);
        paint.setTypeface(getTypeface(font));
        return paint;
    }

    private TextDrawer createTextDrawer(String content, Paint paint) {
        TextDrawer textObject = new TextDrawer();
        textObject.setPaint(paint);
        textObject.setContent(content);
        return textObject;
    }

    private Typeface getTypeface(String name) {
        return Typeface.createFromAsset(getActivity().getAssets(), name);
    }

    private void handlerSeekBarChangeSizeText() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mSizeText = i + SIZE_CHANGE;
                updateTextViewSize(mSizeText);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @UiThread
    public void updateTextViewSize(int size) {
        mTvSizeText.setText(String.valueOf(size));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mFont = mFonts[i];
        Log.i("TAG",mFonts[i]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
