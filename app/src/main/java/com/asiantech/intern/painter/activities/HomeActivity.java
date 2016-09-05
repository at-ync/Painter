package com.asiantech.intern.painter.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.asiantech.intern.painter.R;
import com.asiantech.intern.painter.adapters.FilterAdapter;
import com.asiantech.intern.painter.models.FilterImage;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_home)
public class HomeActivity extends BaseActivity {
    private Bitmap mBitmap;
    private static final String TAG = HomeActivity.class.getName();

    @Extra
    void setData(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    void afterViews() {
        if (mBitmap != null) {
        }
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
            Log.e(TAG, "getUriFromBitmap: " + e.toString());
        }
        return Uri.parse("file://" + sharefile);
    }
}
