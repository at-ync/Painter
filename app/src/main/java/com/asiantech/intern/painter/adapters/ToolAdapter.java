package com.asiantech.intern.painter.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asiantech.intern.painter.R;
import com.asiantech.intern.painter.beans.Tool;
import com.asiantech.intern.painter.interfaces.IPickItems;

import java.util.List;

/**
 * Copyright © 2016 YNC.
 * Created by Congybk on 9/6/2016.
 */
public class ToolAdapter extends RecyclerView.Adapter<ToolAdapter.ToolViewHolder> {
    private final List<Tool> mTools;
    private int mIndex = -1;
    private IPickItems mIPickItems;

    public ToolAdapter(IPickItems iPickItems, List<Tool> tools) {
        mIPickItems = iPickItems;
        mTools = tools;
    }

    @Override
    public ToolViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tool, parent, false);
        return new ToolViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ToolViewHolder holder, final int position) {
        holder.mImgTool.setImageResource(mTools.get(position).getIconTool());
        holder.mView.setSelected(mIndex == position);
    }

    @Override
    public int getItemCount() {
        return mTools == null ? 0 : mTools.size();
    }

    /**
     * This class hold tool for recyclerview.
     */
    public class ToolViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mImgTool;
        private final View mView;

        public ToolViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mImgTool = (ImageView) itemView.findViewById(R.id.imgTool);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mIndex = getAdapterPosition();
                    mIPickItems.setItemSelect(mTools.get(mIndex).getIconTool());
                    notifyDataSetChanged();
                }
            });
        }
    }

}
