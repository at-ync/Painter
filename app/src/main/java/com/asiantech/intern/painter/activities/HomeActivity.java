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

    void afterViews() {
        int[] icons = {R.drawable.ic_move, R.drawable.ic_font, R.drawable.ic_paint, R.drawable.ic_eraser,
                R.drawable.ic_picture, R.drawable.ic_crop, R.drawable.ic_rotate, R.drawable.ic_save, R.drawable.ic_share};
        for (int icon : icons) {
            Tool tool = new Tool();
            tool.setIconTool(icon);
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
                if (mTools.get(position).isClick()) {
                    mTools.get(position).setClick(false);
                } else {
                    mTools.get(position).setClick(true);
                }
                int size = mTools.size();
                for (int i = 0; i < size; i++) {
                    if (i != position) {
                        mTools.get(i).setClick(false);
                    }
                }
                toolAdapter.notifyDataSetChanged();

                setToolClick(mTools, position);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void setToolClick(List<Tool> tools, int position) {
        switch (tools.get(position).getIconTool()) {
            case R.drawable.ic_font:
                if (tools.get(position).isClick()) {
                    mCustomPainter.setIsDrawing(false);
                    setActionText(Action.STOP);
                    DialogInputText_.builder().build().show(getFragmentManager(), "");
                }
                break;
            case R.drawable.ic_move:
                if (tools.get(position).isClick()) {
                    setActionText(Action.MOVE);
                } else {
                    setActionText(Action.STOP);
                }
                break;
            case R.drawable.ic_eraser:
                if (tools.get(position).isClick()) {
                    setDrawing(true);
                }
                break;
            case R.drawable.ic_paint:
                if (tools.get(position).isClick()) {
                    setDrawing(true);
                }
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
