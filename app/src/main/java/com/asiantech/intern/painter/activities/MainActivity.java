package com.asiantech.intern.painter.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.asiantech.intern.painter.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.OnActivityResult;

import java.io.IOException;

@Fullscreen
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private static final int REQUEST_CAMERA = 101;
    private static final int REQUEST_PHOTO = 202;
    private static final String TAG = MainActivity.class.getName();

    @Override
    void afterViews() {

    }

    @Click(R.id.circleImageViewCamera)
    void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Click(R.id.circleImageViewPhoto)
    void selectPhotoFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_PHOTO);
    }

    @OnActivityResult(REQUEST_CAMERA)
    void captureImageResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            HomeActivity_.intent(this).mBitmap(onCaptureImageResult(data)).start();
        }
    }

    @OnActivityResult(REQUEST_PHOTO)
    void selectFromGalleryResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            HomeActivity_.intent(this).mBitmap(onSelectFromGalleryResult(data)).start();
        }
    }

    private Bitmap onCaptureImageResult(Intent data) {
        Bitmap bitmap = null;
        if (data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
        }
        return bitmap;
    }

    private Bitmap onSelectFromGalleryResult(Intent data) {
        Bitmap bitmap = null;
        if (data.getData() != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(
                    selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            bitmap = BitmapFactory.decodeFile(filePath);
        }
        return bitmap;
    }
}
