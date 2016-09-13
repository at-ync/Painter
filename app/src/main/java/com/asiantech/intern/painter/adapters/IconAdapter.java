package com.asiantech.intern.painter.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asiantech.intern.painter.R;
import com.asiantech.intern.painter.beans.Icon;
import com.asiantech.intern.painter.interfaces.IPickIcon;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Copyright © 2016 YNC.
 * Created by Congybk on 9/13/2016.
 */
public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconViewHolder> {
    private final List<Icon> mIcons;
    private final IPickIcon mIPickIcon;

    public IconAdapter(IPickIcon iPickIcon, List<Icon> icons) {
        this.mIcons = icons;
        this.mIPickIcon = iPickIcon;
    }

    @Override
    public IconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_icon, parent, false);
        return new IconViewHolder(v);
    }

    @Override
    public void onBindViewHolder(IconViewHolder holder, final int position) {
        holder.mCircleImageViewIcon.setImageResource(mIcons.get(position).getIcon());
        holder.mCircleImageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIPickIcon.setPickIcon(mIcons.get(position).getIcon());

            }
        });
    }

    @Override
    public int getItemCount() {
        return mIcons != null ? mIcons.size() : 0;
    }

    public class IconViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView mCircleImageViewIcon;

        public IconViewHolder(View itemView) {
            super(itemView);
            mCircleImageViewIcon = (CircleImageView) itemView.findViewById(R.id.circleImageViewIcon);
        }
    }
}
