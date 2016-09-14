package com.asiantech.intern.painter.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.asiantech.intern.painter.R;
import com.asiantech.intern.painter.adapters.FilterAdapter;
import com.asiantech.intern.painter.adapters.IconAdapter;
import com.asiantech.intern.painter.adapters.ToolAdapter;
import com.asiantech.intern.painter.beans.BitmapDrawer;
import com.asiantech.intern.painter.beans.Icon;
import com.asiantech.intern.painter.beans.Tool;
import com.asiantech.intern.painter.commons.Constant;
import com.asiantech.intern.painter.dialogs.DialogInputText_;
import com.asiantech.intern.painter.dialogs.DialogPathColor;
import com.asiantech.intern.painter.dialogs.DialogPathColor_;
import com.asiantech.intern.painter.interfaces.IAction;
import com.asiantech.intern.painter.interfaces.IPickFilter;
import com.asiantech.intern.painter.interfaces.IPickIcon;
import com.asiantech.intern.painter.interfaces.IPickItems;
import com.asiantech.intern.painter.utils.ImageUtil;
import com.asiantech.intern.painter.views.CustomPainter;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EActivity(R.layout.activity_home)
public class HomeActivity extends BaseActivity implements IAction, IPickFilter, IPickItems, IPickIcon, DialogPathColor.IOnPickPathStyle {
    private static final int ICONS[] = {R.drawable.ic_filter, R.drawable.ic_move, R.drawable.ic_rotate, R.drawable.ic_font, R.drawable.ic_paint, R.drawable.ic_eraser,
            R.drawable.ic_picture, R.drawable.ic_crop, R.drawable.ic_save, R.drawable.ic_share};
    private static final int ICON_IMAGES[] = {R.drawable.ic_happy, R.drawable.ic_hipster, R.drawable.ic_laughing, R.drawable.ic_love,
            R.drawable.ic_relieved, R.drawable.ic_rich, R.drawable.ic_sick, R.drawable.ic_smile, R.drawable.ic_smiling};
    @ViewById(R.id.viewPaint)
    CustomPainter mCustomPainter;
    @Extra
    Uri mUri;
    @ViewById(R.id.recyclerViewTool)
    RecyclerView mRecyclerViewTool;
    @ViewById(R.id.llTool)
    LinearLayout mLlTool;
    @ViewById(R.id.recyclerViewFilter)
    RecyclerView mRecyclerViewFilter;
    private List<Tool> mTools = new ArrayList<>();
    private List<Icon> mIconImages = new ArrayList<>();
    private Bitmap mBitmapSource;
    private Bitmap mBitmap;
    private FilterAdapter mFilterAdapter;
    private int mPickedFilter = -1;
    private String mNameImage;

    void afterViews() {
        mLlTool.setVisibility(View.GONE);
        if (mUri != null) {
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                mBitmapSource = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showToast(getString(R.string.error_uri_null));
        }
        for (int icon : ICONS) {
            mTools.add(new Tool(icon));
        }
        for (int iconImage : ICON_IMAGES) {
            mIconImages.add(new Icon(iconImage));
        }
        mRecyclerViewTool.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewTool.setLayoutManager(layoutManager);
        final ToolAdapter toolAdapter = new ToolAdapter(this, mTools);
        mRecyclerViewTool.setAdapter(toolAdapter);
        sendBitmap();
        LinearLayoutManager layoutManagerFilter = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewFilter.setLayoutManager(layoutManagerFilter);
        mNameImage = Environment.getExternalStorageDirectory() + "/Pictures/" + UUID.randomUUID().toString() + ".png";
    }

    //TODO Tool click
    private void onItemSelect(int iconTool) {
        switch (iconTool) {
            case R.drawable.ic_filter:
                addFilter();
                mLlTool.setVisibility(View.VISIBLE);
                break;
            case R.drawable.ic_font:
                mLlTool.setVisibility(View.GONE);
                setAction(Constant.ACTION_INPUT_TEXT);
                DialogInputText_.builder().build().show(getFragmentManager(), "");
                break;
            case R.drawable.ic_move:
                mLlTool.setVisibility(View.GONE);
                setAction(Constant.ACTION_MOVE_BITMAP);
                break;
            case R.drawable.ic_crop:
                mLlTool.setVisibility(View.GONE);
                break;
            case R.drawable.ic_eraser:
                setAction(Constant.ACTION_ERASER);
                mLlTool.setVisibility(View.GONE);
                break;
            case R.drawable.ic_paint:
                mLlTool.setVisibility(View.GONE);
                setAction(Constant.ACTION_DRAWING);
                DialogPathColor_.builder().mColor(mCustomPainter.getPathColor()).build().show(getSupportFragmentManager(), "");
                break;
            case R.drawable.ic_picture:
                addIconImage();
                mLlTool.setVisibility(View.VISIBLE);
                break;
            case R.drawable.ic_rotate:
                setAction(Constant.ACTION_ROTATE_BITMAP);
                mLlTool.setVisibility(View.GONE);
                break;
            case R.drawable.ic_save:
                doingBackgroundSaveImage();
                mLlTool.setVisibility(View.GONE);
                break;
            case R.drawable.ic_share:
                doingBackgroundShareImage();
                mLlTool.setVisibility(View.GONE);
                break;
        }
    }


    @UiThread
    public void loadBitmap() {
        Bitmap resizedBitmap = ImageUtil.getInstance().scaleBitmap(mBitmap, mCustomPainter.getHeight(), false);
        mCustomPainter.setBackground(resizedBitmap);
    }

    @Background
    public void sendBitmap() {
        loadBitmap();
    }

    private void addIconImage() {
        IconAdapter iconAdapter = new IconAdapter(this, mIconImages);
        mRecyclerViewFilter.setAdapter(iconAdapter);
    }

    private void addFilter() {
        mFilterAdapter = new FilterAdapter(this, mBitmapSource);
        mRecyclerViewFilter.setAdapter(mFilterAdapter);
    }

    @Background
    void doSetBitmapBackground(FilterAdapter filterAdapter, int position, ProgressDialog progressDialog) {
        mBitmap = filterAdapter.getFilterBitmap(filterAdapter.getPositionItem(position).getTypeFilter(), false);
        loadBitmap();
        doDismissProgressDialog(progressDialog);
    }

    @UiThread
    void doDismissProgressDialog(ProgressDialog progressDialog) {
        progressDialog.dismiss();
    }

    @Override
    public void setAction(int action) {
        mCustomPainter.setAction(action);
    }

    @Override
    public void setBitmapDrawer(BitmapDrawer bitmapDrawer) {
        mCustomPainter.setBitmapDrawer(bitmapDrawer);
    }

    @Override
    public void setPickIcon(int idIcon) {
        BitmapDrawer bitmapDrawer = new BitmapDrawer();
        bitmapDrawer.setBitmap(BitmapFactory.decodeResource(getResources(), idIcon));
        setBitmapDrawer(bitmapDrawer);
    }

    public void onPicked(int color, int radius) {
        mCustomPainter.setPathColor(color);
        mCustomPainter.setPathRadius(radius);
    }

    @Override
    public void setPickFilter(int position) {
        if (mPickedFilter != position) {
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(HomeActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle(getString(R.string.rendering));
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.setCancelable(false);
            progressDialog.show();
            doSetBitmapBackground(mFilterAdapter, position, progressDialog);
            mPickedFilter = position;
        }
    }

    @Override
    public void setItemSelect(int iconTool) {
        onItemSelect(iconTool);
    }

    @Background
    public void doingBackgroundSaveImage() {
        saveImage();
        showToast();
    }

    private Uri saveImage() {
        Uri imageUri = null;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(mNameImage);
            Bitmap bitmap = Bitmap.createBitmap(mCustomPainter.getWidth(), mCustomPainter.getHeight(), Bitmap.Config.ARGB_8888);
            mCustomPainter.draw(new Canvas(bitmap));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            imageUri = Uri.fromFile(new File(mNameImage));
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageUri;
    }

    @UiThread
    public void showToast() {
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }

    @Background
    public void doingBackgroundShareImage() {
        Uri uriImage = saveImage();
        if (uriImage != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uriImage);
            intent.setType("image/jpeg");
            startActivity(Intent.createChooser(intent, getResources().getText(R.string.send_to)));
        }
    }
}
