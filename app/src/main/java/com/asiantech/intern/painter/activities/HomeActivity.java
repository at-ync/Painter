package com.asiantech.intern.painter.activities;

import android.widget.ImageButton;

import com.asiantech.intern.painter.R;
import com.asiantech.intern.painter.beans.TextObject;
import com.asiantech.intern.painter.commo.Action;
import com.asiantech.intern.painter.dialogs.DialogInputText_;
import com.asiantech.intern.painter.interfaces.ITextLab;
import com.asiantech.intern.painter.views.CustomPainter;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_home)
public class HomeActivity extends BaseActivity implements ITextLab {
    @ViewById(R.id.viewPaint)
    CustomPainter mCustomPainter;
    @ViewById(R.id.imgButtonInputText)
    ImageButton mImgButtonInputText;
    @ViewById(R.id.imgButtonMove)
    ImageButton mImgButtonMove;

    void afterViews() {
    }

    @Override
    public void setTextObject(TextObject textObject) {
        mCustomPainter.setTextObject(textObject);
    }

    @Click(R.id.imgButtonMove)
    public void clickMove() {
        setActionText(Action.MOVE);
    }

    @Override
    public void setActionText(int action) {
        mCustomPainter.setActionText(action);
    }


    @Click(R.id.imgButtonInputText)
    public void inPutText() {
        setActionText(Action.STOP);
        DialogInputText_.builder().build().show(getFragmentManager(), "");
    }
}
