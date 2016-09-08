package com.asiantech.intern.painter.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.asiantech.intern.painter.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

@Fullscreen
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private static final int REQUEST_CAMERA = 101;
    private static final int REQUEST_PHOTO = 202;
    private String mCurrentPhotoPath;

    @Override
    void afterViews() {

    }

    @Click(R.id.circleImageViewCamera)
    void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
            }
            if (photoFile != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        }

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
        return Uri.parse(mCurrentPhotoPath);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat(getString(R.string.format_date_name_image_camera)).format(new Date());
        String imageFileName = String.format(getString(R.string.image_file_name), timeStamp);
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = String.format(getString(R.string.path_photo), image.getAbsoluteFile());
        return image;
    }

}
