package com.asiantech.intern.painter.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.asiantech.intern.painter.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

@Fullscreen
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private static final int REQUEST_CAMERA = 101;
    private static final int REQUEST_PHOTO = 202;
    @ViewById(R.id.circleImageViewPhoto)
    CircleImageView mCircleImageViewPhoto;

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
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PHOTO);
    }

    @OnActivityResult(REQUEST_CAMERA)
    void captureImageResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            HomeActivity_.intent(this).mUri(onCaptureImageResult(data)).start();
        }
    }

    @OnActivityResult(REQUEST_PHOTO)
    void selectFromGalleryResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            HomeActivity_.intent(this).mUri(data.getData()).start();
        }
    }

    private Uri onCaptureImageResult(Intent data) {
        Bitmap bitmap = null;
        if (data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
        }
        return getImageUri(this, bitmap);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,"", null);
        return Uri.parse(path);
    }
}
