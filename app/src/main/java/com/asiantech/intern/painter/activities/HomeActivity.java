package com.asiantech.intern.painter.activities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.asiantech.intern.painter.R;
import com.asiantech.intern.painter.adapters.ToolAdapter;
import com.asiantech.intern.painter.beans.TextDrawer;
import com.asiantech.intern.painter.beans.Tool;
import com.asiantech.intern.painter.commons.Constant;
import com.asiantech.intern.painter.dialogs.DialogInputText_;
import com.asiantech.intern.painter.interfaces.ITextLab;
import com.asiantech.intern.painter.utils.ClickItemRecyclerView;
import com.asiantech.intern.painter.utils.IClickItemRecyclerView;
import com.asiantech.intern.painter.views.CustomPainter;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_home)
public class HomeActivity extends BaseActivity implements ITextLab {
    @ViewById(R.id.viewPaint)
    CustomPainter mCustomPainter;
    @Extra
    Uri mUri;
    @ViewById(R.id.recyclerViewTool)
    RecyclerView mRecyclerViewTool;
    private List<Tool> mTools = new ArrayList<>();
    private static final int ICONS[] = {R.drawable.ic_move, R.drawable.ic_font, R.drawable.ic_paint, R.drawable.ic_eraser,
            R.drawable.ic_picture, R.drawable.ic_crop, R.drawable.ic_rotate, R.drawable.ic_save, R.drawable.ic_share};
    private Bitmap mBitmap;

    void afterViews() {
        if (mUri != null) {
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            showToast(getString(R.string.error_uri_null));
        }
        for (int icon : ICONS) {
            mTools.add(new Tool(icon));
        }
        mRecyclerViewTool.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewTool.setLayoutManager(layoutManager);
        final ToolAdapter toolAdapter = new ToolAdapter(this, mTools);
        mRecyclerViewTool.setAdapter(toolAdapter);
        mRecyclerViewTool.addOnItemTouchListener(new ClickItemRecyclerView(this, mRecyclerViewTool, new IClickItemRecyclerView() {
            @Override
            public void onClick(View view, int position) {
                onItemSelect(mTools.get(position).getIconTool());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    //TODO Tool click
    private void onItemSelect(int iconTool) {
        switch (iconTool) {
            case R.drawable.ic_font:
                mCustomPainter.setIsDrawing(false);
                setActionText(Constant.STOP);
                DialogInputText_.builder().build().show(getFragmentManager(), "");
                break;
            case R.drawable.ic_move:
                setActionText(Action.MOVE);
                break;
            case R.drawable.ic_crop:
                break;
            case R.drawable.ic_eraser:
                break;
            case R.drawable.ic_paint:
                break;
            case R.drawable.ic_photo:
                break;
            case R.drawable.ic_rotate:
                break;
            case R.drawable.ic_save:
                break;
            case R.drawable.ic_share:
                break;
        }
    }


    @Override
    public void setTextObject(TextDrawer textObject) {
        mCustomPainter.setTextObject(textObject);
    }

    @Override
    public void setActionText(int action) {
        mCustomPainter.setActionText(action);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Bitmap resizedBitmap = scalePhoto(mBitmap, mCustomPainter.getHeight(), false);
        mCustomPainter.setBackground(resizedBitmap);
    }

    private static Bitmap scalePhoto(Bitmap realImage, float maxImageSize,
                                     boolean filter) {
        float ratio = Math.min(maxImageSize / realImage.getWidth(), maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());
        return Bitmap.createScaledBitmap(realImage, width, height, filter);
    }

}
