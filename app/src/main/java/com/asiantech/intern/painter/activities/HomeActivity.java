package com.asiantech.intern.painter.activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.asiantech.intern.painter.R;
import com.asiantech.intern.painter.adapters.FilterAdapter;
import com.asiantech.intern.painter.adapters.IconAdapter;
import com.asiantech.intern.painter.adapters.ToolAdapter;
import com.asiantech.intern.painter.beans.BitmapDrawer;
import com.asiantech.intern.painter.beans.Icon;
import com.asiantech.intern.painter.beans.Tool;
import com.asiantech.intern.painter.commons.Constant;
import com.asiantech.intern.painter.dialogs.DialogInputText_;
import com.asiantech.intern.painter.interfaces.IAction;
import com.asiantech.intern.painter.utils.ClickItemRecyclerView;
import com.asiantech.intern.painter.utils.IClickItemRecyclerView;
import com.asiantech.intern.painter.utils.ImageUtil;
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
    private static final int ICONIMAGES[] = {R.drawable.ic_happy, R.drawable.ic_hipster, R.drawable.ic_laughing, R.drawable.ic_love,
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
    private Bitmap mBitmap;

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
        for (int iconImage : ICONIMAGES) {
            mIconImages.add(new Icon(iconImage));
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
        sendBitmap();
        LinearLayoutManager layoutManagerFilter = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewFilter.setLayoutManager(layoutManagerFilter);
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
                break;
            case R.drawable.ic_picture:
                addIconImage();
                mLlTool.setVisibility(View.VISIBLE);
                break;
            case R.drawable.ic_rotate:
                mLlTool.setVisibility(View.GONE);
                break;
            case R.drawable.ic_save:
                mLlTool.setVisibility(View.GONE);
                break;
            case R.drawable.ic_share:
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
        IconAdapter iconAdapter = new IconAdapter(mIconImages);
        mRecyclerViewFilter.setAdapter(iconAdapter);
        mRecyclerViewFilter.addOnItemTouchListener(new ClickItemRecyclerView(this, mRecyclerViewFilter, new IClickItemRecyclerView() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void addFilter() {
        final FilterAdapter filterAdapter = new FilterAdapter(this, mBitmap);
        mRecyclerViewFilter.setAdapter(filterAdapter);
        mRecyclerViewFilter.addOnItemTouchListener(new ClickItemRecyclerView(this, mRecyclerViewFilter, new IClickItemRecyclerView() {
            @Override
            public void onClick(View view, final int position) {
                final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(HomeActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setTitle(getString(R.string.rendering));
                progressDialog.setMessage(getString(R.string.please_wait));
                progressDialog.show();
                doSetBitmapBackground(filterAdapter, position, progressDialog);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
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
}
