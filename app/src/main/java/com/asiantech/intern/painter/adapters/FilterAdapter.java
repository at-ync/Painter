package com.asiantech.intern.painter.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.asiantech.intern.painter.R;
import com.asiantech.intern.painter.beans.FilterImage;
import com.asiantech.intern.painter.commons.Constant;
import com.asiantech.intern.painter.interfaces.IPickFilter;
import com.asiantech.intern.painter.utils.ImageFilterUtils;
import com.asiantech.intern.painter.utils.ImageUtil;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Copyright © 2016 YNC.
 * Created by Congybk on 8/31/2016.
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {
    private static final int DEFAULT_CONTRAST = 1;
    private static final int DEFAULT_HUE = 50;
    private static final float DEFAULT_SEPIA = 0.45f;
    private static final float DEFAULT_VIGNETTE = 0.8f;
    private static final int DEFAULT_BRIGHTNESS = 0;
    private static final int IMAGE_DEFAULT_SIZE = 50;

    private final Context mContext;
    private final Bitmap mBitmapFilter;
    private final Bitmap mBitmapSource;
    private List<FilterImage> mFilterImages;
    private final IPickFilter mIPickFilter;

    public FilterAdapter(Context context, Bitmap bitmap) {
        mContext = context;
        if (context instanceof IPickFilter) {
            mIPickFilter = (IPickFilter) context;
        } else {
            mIPickFilter = null;
        }
        mBitmapFilter = ImageUtil.getInstance().compressBitmap(bitmap, 10).copy(Bitmap.Config.ARGB_8888, true);
        mBitmapSource = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        initListFilters();
    }

    @Override
    public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_image, parent, false);
        return new FilterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FilterViewHolder holder, final int position) {
        new AsyncTask<Integer, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Integer... integers) {
                return getFilterBitmap(integers[0], true);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                holder.imageViewFilter.setImageBitmap(bitmap);
                holder.tvNameFilter.setText(mFilterImages.get(position).getNameFilter());
            }
        }.execute(mFilterImages.get(position).getTypeFilter());
    }

    @Override
    public int getItemCount() {
        return mFilterImages != null ? mFilterImages.size() : 0;
    }

    private void initListFilters() {
        mFilterImages = new ArrayList<>();
        mFilterImages.add(new FilterImage(Constant.CONTRAST, mContext.getString(R.string.filter_name_contrast)));
        mFilterImages.add(new FilterImage(Constant.INVERT, mContext.getString(R.string.filter_name_invert)));
        mFilterImages.add(new FilterImage(Constant.HUE, mContext.getString(R.string.filter_name_hue)));
        mFilterImages.add(new FilterImage(Constant.SEPIA, mContext.getString(R.string.filter_name_sepia)));
        mFilterImages.add(new FilterImage(Constant.GRAYSCALE, mContext.getString(R.string.filter_name_gray_scale)));
        mFilterImages.add(new FilterImage(Constant.VIGNETTE, mContext.getString(R.string.filter_name_vignette)));
        mFilterImages.add(new FilterImage(Constant.SKETCH, mContext.getString(R.string.filter_name_sketch)));
        mFilterImages.add(new FilterImage(Constant.BRIGHTNESS, mContext.getString(R.string.filter_name_brightness)));
    }

    public FilterImage getPositionItem(int position) {
        return mFilterImages.get(position);
    }

    public Bitmap getFilterBitmap(int type, boolean resize) {
        if (resize) {
            switch (type) {
                case Constant.CONTRAST:
                    return ImageUtil.getInstance().scaleBitmap(ImageFilterUtils.getInstance().doContrast(mBitmapFilter, DEFAULT_CONTRAST), IMAGE_DEFAULT_SIZE, false);
                case Constant.INVERT:
                    return ImageUtil.getInstance().scaleBitmap(ImageFilterUtils.getInstance().doInvertImage(mBitmapFilter), IMAGE_DEFAULT_SIZE, false);
                case Constant.HUE:
                    return ImageUtil.getInstance().scaleBitmap(ImageFilterUtils.getInstance().doHue(mBitmapFilter, DEFAULT_HUE), IMAGE_DEFAULT_SIZE, false);
                case Constant.SEPIA:
                    return ImageUtil.getInstance().scaleBitmap(ImageFilterUtils.getInstance().doSepia(mBitmapFilter, DEFAULT_SEPIA), IMAGE_DEFAULT_SIZE, false);
                case Constant.GRAYSCALE:
                    return ImageUtil.getInstance().scaleBitmap(ImageFilterUtils.getInstance().doGrayScale(mBitmapFilter), IMAGE_DEFAULT_SIZE, false);
                case Constant.VIGNETTE:
                    return ImageUtil.getInstance().scaleBitmap(ImageFilterUtils.getInstance().doVignette(mBitmapFilter, DEFAULT_VIGNETTE), IMAGE_DEFAULT_SIZE, false);
                case Constant.SKETCH:
                    return ImageUtil.getInstance().scaleBitmap(ImageFilterUtils.getInstance().doSketch(mBitmapFilter, mContext), IMAGE_DEFAULT_SIZE, false);
                case Constant.BRIGHTNESS:
                    return ImageUtil.getInstance().scaleBitmap(ImageFilterUtils.getInstance().doBrightness(mBitmapFilter, DEFAULT_BRIGHTNESS), IMAGE_DEFAULT_SIZE, false);
            }
        } else {
            switch (type) {
                case Constant.CONTRAST:
                    return ImageFilterUtils.getInstance().doContrast(mBitmapSource, DEFAULT_CONTRAST);
                case Constant.INVERT:
                    return ImageFilterUtils.getInstance().doInvertImage(mBitmapSource);
                case Constant.HUE:
                    return ImageFilterUtils.getInstance().doHue(mBitmapSource, DEFAULT_HUE);
                case Constant.SEPIA:
                    return ImageFilterUtils.getInstance().doSepia(mBitmapSource, DEFAULT_SEPIA);
                case Constant.GRAYSCALE:
                    return ImageFilterUtils.getInstance().doGrayScale(mBitmapSource);
                case Constant.VIGNETTE:
                    return ImageFilterUtils.getInstance().doVignette(mBitmapSource, DEFAULT_VIGNETTE);
                case Constant.SKETCH:
                    Bitmap sketchBitmap = ImageUtil.getInstance().compressBitmap(mBitmapSource, 100).copy(Bitmap.Config.ARGB_8888, true);
                    return ImageFilterUtils.getInstance().doSketch(sketchBitmap, mContext);
                case Constant.BRIGHTNESS:
                    return ImageFilterUtils.getInstance().doBrightness(mBitmapSource, DEFAULT_BRIGHTNESS);
            }
        }
        return null;
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView imageViewFilter;
        private final TextView tvNameFilter;

        public FilterViewHolder(View itemView) {
            super(itemView);
            imageViewFilter = (CircleImageView) itemView.findViewById(R.id.circleImageViewFilter);
            tvNameFilter = (TextView) itemView.findViewById(R.id.tvNameFilter);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mIPickFilter.setPickFilter(getAdapterPosition());
                }
            });
        }
    }

    public Bitmap getEditedFilterBitmap(int type, float value) {
        switch (type) {
            case Constant.CONTRAST:
                return ImageFilterUtils.getInstance().doContrast(mBitmapSource, value);
            case Constant.INVERT:
                return ImageFilterUtils.getInstance().doInvertImage(mBitmapSource);
            case Constant.HUE:
                return ImageFilterUtils.getInstance().doHue(mBitmapSource, (int) value);
            case Constant.SEPIA:
                return ImageFilterUtils.getInstance().doSepia(mBitmapSource, value);
            case Constant.GRAYSCALE:
                return ImageFilterUtils.getInstance().doGrayScale(mBitmapSource);
            case Constant.VIGNETTE:
                return ImageFilterUtils.getInstance().doVignette(mBitmapSource, value);
            case Constant.SKETCH:
                Bitmap sketchBitmap = ImageUtil.getInstance().compressBitmap(mBitmapSource, 100).copy(Bitmap.Config.ARGB_8888, true);
                return ImageFilterUtils.getInstance().doSketch(sketchBitmap, mContext);
            case Constant.BRIGHTNESS:
                return ImageFilterUtils.getInstance().doBrightness(mBitmapSource, value);
        }
        return null;
    }
}

