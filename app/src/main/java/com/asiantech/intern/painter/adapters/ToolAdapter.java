package com.asiantech.intern.painter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asiantech.intern.painter.R;
import com.asiantech.intern.painter.beans.Tool;

import java.util.List;

/**
 * Copyright © 2016 YNC.
 * Created by Congybk on 9/6/2016.
 */
public class ToolAdapter extends RecyclerView.Adapter<ToolAdapter.ToolViewHolder> {
    private final Context mContext;
    private List<Tool> mTools;

    public ToolAdapter(Context context, List<Tool> tools) {
        mContext = context;
        mTools = tools;
    }

    @Override
    public ToolViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tool, parent, false);
        return new ToolViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ToolViewHolder holder, final int position) {
        holder.imgTool.setImageResource(mTools.get(position).getIconTool());
        if (mTools.get(position).isClick()) {
            holder.imgTool.setBackgroundResource(R.drawable.custom_icon_selected);
        }else{
            holder.imgTool.setBackgroundResource(R.drawable.custom_select_imagebutton);
        }
    }

    @Override
    public int getItemCount() {
        return mTools == null ? 0 : mTools.size();
    }

    public class ToolViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgTool;

        public ToolViewHolder(View itemView) {
            super(itemView);
            imgTool = (ImageView) itemView.findViewById(R.id.imgTool);
        }
    }

}
