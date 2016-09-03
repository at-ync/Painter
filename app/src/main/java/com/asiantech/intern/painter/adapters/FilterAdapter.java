package com.asiantech.intern.painter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asiantech.intern.painter.R;
import com.asiantech.intern.painter.models.FilterImage;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Copyright © 2016 YNC.
 * Created by Congybk on 8/31/2016.
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    private final Context mContext;
    private final List<FilterImage> mListFilterImage;

    public FilterAdapter(Context context, List<FilterImage> listFilterImage){
        this.mContext = context;
        this.mListFilterImage = listFilterImage;
    }

    @Override
    public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_image, parent, false);
        return new FilterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FilterViewHolder holder, int position) {
        holder.tvNameFilter.setText(mListFilterImage.get(position).getNameFilter());
        holder.imageViewFilter.setImageResource(mListFilterImage.get(position).getImageFilter());
    }

    @Override
    public int getItemCount() {
        return mListFilterImage.size();
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView imageViewFilter;
        private final TextView tvNameFilter;
        public FilterViewHolder(View itemView) {
            super(itemView);
            imageViewFilter = (CircleImageView) itemView.findViewById(R.id.circleImagViewFilter);
            tvNameFilter = (TextView)itemView.findViewById(R.id.tvNameFilter);
        }
    }
}
