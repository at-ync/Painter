package com.asiantech.intern.painter.activities;

import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.asiantech.intern.painter.R;
import com.asiantech.intern.painter.adapters.ToolAdapter;
import com.asiantech.intern.painter.beans.TextObject;
import com.asiantech.intern.painter.beans.Tool;
import com.asiantech.intern.painter.commo.Action;
import com.asiantech.intern.painter.dialogs.DialogInputText_;
import com.asiantech.intern.painter.interfaces.ITextLab;
import com.asiantech.intern.painter.utils.ClickItemRecyclerView;
import com.asiantech.intern.painter.utils.IClickItemRecyclerView;
import com.asiantech.intern.painter.views.CustomPainter;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_home)
public class HomeActivity extends BaseActivity implements ITextLab {
    @ViewById(R.id.viewPaint)
    CustomPainter mCustomPainter;
    @Extra
    Bitmap mBitmap;
    @ViewById(R.id.recyclerViewTool)
    RecyclerView mRecyclerViewTool;
    List<Tool> mTools = new ArrayList<>();
    private final int mIcons[] = {R.drawable.ic_move, R.drawable.ic_font, R.drawable.ic_paint, R.drawable.ic_eraser,
            R.drawable.ic_picture, R.drawable.ic_crop, R.drawable.ic_rotate, R.drawable.ic_save, R.drawable.ic_share};

    void afterViews() {
        for (int icon : mIcons) {
            Tool tool = new Tool(icon);
            mTools.add(tool);
        }
        mRecyclerViewTool.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewTool.setLayoutManager(layoutManager);
        final ToolAdapter toolAdapter = new ToolAdapter(this, mTools);
        toolAdapter.notifyDataSetChanged();
        mRecyclerViewTool.setAdapter(toolAdapter);
        mRecyclerViewTool.addOnItemTouchListener(new ClickItemRecyclerView(this, mRecyclerViewTool, new IClickItemRecyclerView() {
            @Override
            public void onClick(View view, int position) {
                onItemSelect(view,mTools.get(position).getIconTool());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }
    //TODO Tool click
    private void onItemSelect(View view,int iconTool) {
        switch (iconTool){
            case R.drawable.ic_font:
                mCustomPainter.setIsDrawing(false);
                setActionText(Action.STOP);
                DialogInputText_.builder().build().show(getFragmentManager(), "");
                break;
            case R.drawable.ic_move:
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
    public void setTextObject(TextObject textObject) {
        mCustomPainter.setTextObject(textObject);
    }


    @Override
    public void setActionText(int action) {
        mCustomPainter.setActionText(action);
    }


    private void setDrawing(boolean isEraser) {
        mCustomPainter.setIsDrawing(true);
        mCustomPainter.getDrawingPainter().setIsEraser(isEraser);
    }
}
