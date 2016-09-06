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
    @ViewById(R.id.imgButtonDraw)
    ImageButton mImgButtonDraw;
    @ViewById(R.id.imgButtonEraser)
    ImageButton mImgButtonEraser;

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
        mCustomPainter.setIsDrawing(false);
        setActionText(Action.STOP);
        DialogInputText_.builder().build().show(getFragmentManager(), "");
    }

    @Click(R.id.imgButtonDraw)
    public void onClickButtonDraw(){
        setDrawing(true);
    }

    @Click(R.id.imgButtonEraser)
    public void onClickButtonEraser(){
        setDrawing(true);
    }

    private void setDrawing(boolean isEraser){
        mCustomPainter.setIsDrawing(true);
        mCustomPainter.getDrawingPainter().setIsEraser(isEraser);
    }
}
