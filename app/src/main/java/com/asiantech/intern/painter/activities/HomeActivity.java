package com.asiantech.intern.painter.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageButton;

import com.asiantech.intern.painter.R;
import com.asiantech.intern.painter.beans.TextObject;
import com.asiantech.intern.painter.commo.Action;
import com.asiantech.intern.painter.dialogs.DialogInputText_;
import com.asiantech.intern.painter.interfaces.ITextLab;
import com.asiantech.intern.painter.views.CustomPainter;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@EActivity(R.layout.activity_home)
public class HomeActivity extends BaseActivity implements ITextLab {
    @ViewById(R.id.viewPaint)
    CustomPainter mCustomPainter;
    @ViewById(R.id.imgButtonInputText)
    ImageButton mImgButtonInputText;
    @ViewById(R.id.imgButtonMove)
    ImageButton mImgButtonMove;
    @Extra
    Bitmap mBitmap;
    @ViewById(R.id.imgButtonDraw)
    ImageButton mImgButtonDraw;

    void afterViews() {
    }

    @Click(R.id.imgBtnShare)
    void share() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, getUriFromBitmap(mBitmap));
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
    }

    public Uri getUriFromBitmap(Bitmap bitmap) {
        File cache = getApplicationContext().getExternalCacheDir();
        File sharefile = new File(cache, getString(R.string.share_photo));
        try {
            FileOutputStream out = new FileOutputStream(sharefile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
        }
        return Uri.parse("file://" + sharefile);
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
        mCustomPainter.setIsDrawing(true);
    }
}
