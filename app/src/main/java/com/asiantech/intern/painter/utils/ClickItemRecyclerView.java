package com.asiantech.intern.painter.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


public class ClickItemRecyclerView implements RecyclerView.OnItemTouchListener {
    private GestureDetector mGestureDetector;
    private IClickItemRecyclerView mIClickItemRecyclerView;

    public ClickItemRecyclerView(final Context context, final RecyclerView recyclerView, final IClickItemRecyclerView mIClickItemRecyclerView) {
        this.mIClickItemRecyclerView = mIClickItemRecyclerView;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View viewChild = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (viewChild != null && mIClickItemRecyclerView != null) {
                    mIClickItemRecyclerView.onLongClick(viewChild, recyclerView.getChildAdapterPosition(viewChild));
                }
            }
        });

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent e) {
        View viewChild = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (viewChild != null && mIClickItemRecyclerView != null && mGestureDetector.onTouchEvent(e)) {
            mIClickItemRecyclerView.onClick(viewChild, recyclerView.getChildAdapterPosition(viewChild));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
