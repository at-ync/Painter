package com.asiantech.intern.painter.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asiantech.intern.painter.R;
import com.asiantech.intern.painter.views.CustomCirclePath;
import com.asiantech.intern.painter.views.CustomCirclePath_;

import java.util.List;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by HungTQB on 13/09/2016.
 */
public class CirclePathAdapter extends RecyclerView.Adapter<CirclePathAdapter.ViewHolder> {
    private List<CustomCirclePath_> mCustomCirclePaths;
    private int mIndex = -1;

    public CirclePathAdapter(List<CustomCirclePath_> customCirclePaths) {
        mCustomCirclePaths = customCirclePaths;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cicler_radius, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.customCirclePathRadius.setCircleColor(mCustomCirclePaths.get(position).getCircleColor());
        holder.customCirclePathRadius.setCircleRadius(mCustomCirclePaths.get(position).getCircleRadius());
        holder.customCirclePathRadius.setSelected(mIndex == position);
    }

    @Override
    public int getItemCount() {
        return mCustomCirclePaths.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private CustomCirclePath customCirclePathRadius;

        public ViewHolder(View itemView) {
            super(itemView);
            customCirclePathRadius = (CustomCirclePath) itemView.findViewById(R.id.customCirclePathRadius);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mIndex = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
