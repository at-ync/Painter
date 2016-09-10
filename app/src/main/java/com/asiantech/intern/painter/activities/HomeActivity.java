package com.asiantech.intern.painter.activities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.asiantech.intern.painter.R;
import com.asiantech.intern.painter.adapters.FilterAdapter;
import com.asiantech.intern.painter.adapters.ToolAdapter;
import com.asiantech.intern.painter.beans.FilterImage;
import com.asiantech.intern.painter.beans.TextDrawer;
import com.asiantech.intern.painter.beans.Tool;
import com.asiantech.intern.painter.commons.Constant;
import com.asiantech.intern.painter.dialogs.DialogInputText_;
import com.asiantech.intern.painter.interfaces.IAction;
import com.asiantech.intern.painter.utils.ClickItemRecyclerView;
import com.asiantech.intern.painter.utils.IClickItemRecyclerView;
import com.asiantech.intern.painter.views.CustomPainter;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_home)
public class HomeActivity extends BaseActivity implements IAction {
    private static final int ICONS[] = {R.drawable.ic_filter, R.drawable.ic_move, R.drawable.ic_font, R.drawable.ic_paint, R.drawable.ic_eraser,
            R.drawable.ic_picture, R.drawable.ic_crop, R.drawable.ic_rotate, R.drawable.ic_save, R.drawable.ic_share};
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
    private Bitmap mBitmap;

    private static Bitmap scalePhoto(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(maxImageSize / realImage.getWidth(), maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());
        return Bitmap.createScaledBitmap(realImage, width, height, filter);
    }

    void afterViews() {
        mLlTool.setVisibility(View.GONE);
        if (mUri != null) {
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showToast(getString(R.string.error_uri_null));
        }
        for (int icon : ICONS) {
            mTools.add(new Tool(icon));
        }
        mRecyclerViewTool.setHasFixedSize(true);
        LinearLayoutManager layoutManagerTools = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewTool.setLayoutManager(layoutManagerTools);
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
        sendBitmap();
        LinearLayoutManager layoutManagerFilter = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewFilter.setLayoutManager(layoutManagerFilter);
        final FilterAdapter filterAdapter = new FilterAdapter(this, mBitmap);
        mRecyclerViewFilter.setAdapter(filterAdapter);
        mRecyclerViewFilter.addOnItemTouchListener(new ClickItemRecyclerView(this, mRecyclerViewFilter, new IClickItemRecyclerView() {
            @Override
            public void onClick(View view, int position) {
                FilterImage filterImage = filterAdapter.getPositionItem(position);
                mBitmap = filterAdapter.getFilterBitmap(filterImage.getTypeFilter(), false);
                loadBitmap();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    //TODO Tool click
    private void onItemSelect(int iconTool) {
        switch (iconTool) {
            case R.drawable.ic_filter:
                mLlTool.setVisibility(View.VISIBLE);
                break;
            case R.drawable.ic_font:
                setActionText(Constant.ACTION_INPUT_TEXT);
                DialogInputText_.builder().build().show(getFragmentManager(), "");
                break;
            case R.drawable.ic_move:
                setActionText(Constant.ACTION_MOVE);
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
    public void setTextDrawer(TextDrawer textDrawer) {
        mCustomPainter.setTextDrawer(textDrawer);
    }

    @Override
    public void setActionText(int action) {
        mCustomPainter.setActionText(action);
    }

    @UiThread
    public void loadBitmap() {
        Bitmap resizedBitmap = scalePhoto(mBitmap, mCustomPainter.getHeight(), false);
        mCustomPainter.setBackground(resizedBitmap);
    }

    @Background
    public void sendBitmap() {
        loadBitmap();
    }

}
