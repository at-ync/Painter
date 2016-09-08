package com.asiantech.intern.painter.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asiantech.intern.painter.R;
import com.asiantech.intern.painter.beans.FilterImage;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDirectionalSobelEdgeDetectionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFalseColorFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSketchFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageVignetteFilter;

/**
 * Copyright © 2016 YNC.
 * Created by Congybk on 8/31/2016.
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    private final Context mContext;
    private final Bitmap mBitmap;
    private List<FilterImage> mFilterImages;

    public FilterAdapter(Context context, Bitmap bitmap) {
        this.mContext = context;
        mBitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, false);
        initListFilter();
    }

    @Override
    public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_image, parent, false);
        return new FilterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FilterViewHolder holder, int position) {
        holder.tvNameFilter.setText(mFilterImages.get(position).getNameFilter());
        GPUImage gpuImage = new GPUImage(mContext);
        gpuImage.setImage(mBitmap);
        Bitmap imageThumbnail;

        switch (mFilterImages.get(position).getFilterType()){
            case FilterType.CONTRAST:
                GPUImageContrastFilter filterContrast = new GPUImageContrastFilter();
                filterContrast.setContrast(2.0f);
                gpuImage.setFilter(filterContrast);
                break;
            case FilterType.INVERT:
                gpuImage.setFilter(new GPUImageColorInvertFilter());
                break;
            case FilterType.HUE:
                GPUImageHueFilter filterHue = new GPUImageHueFilter();
                filterHue.setHue(115.0f);
                gpuImage.setFilter(filterHue);
                break;
            case FilterType.SEPIA:
                GPUImageSepiaFilter filterSepia = new GPUImageSepiaFilter();
                filterSepia.setIntensity(1.15f);
                gpuImage.setFilter(filterSepia);
                break;
            case FilterType.GRAYSCALE:
                gpuImage.setFilter(new GPUImageGrayscaleFilter());
                break;
            case FilterType.SHARPNESS:
                GPUImageSharpenFilter filterSharpness = new GPUImageSharpenFilter();
                filterSharpness.setSharpness(4.0f);
                gpuImage.setFilter(filterSharpness);
                break;
            case FilterType.SOBEL_EDGE_DETECTION:
                GPUImageDirectionalSobelEdgeDetectionFilter filterSobel = new GPUImageDirectionalSobelEdgeDetectionFilter();
                filterSobel.setLineSize(5.0f);
                gpuImage.setFilter(filterSobel);
                break;
            case FilterType.VIGNETTE:
                GPUImageVignetteFilter filterVignette = new GPUImageVignetteFilter();
                filterVignette.setVignetteStart(0f);
                gpuImage.setFilter(filterVignette);
                break;
            case FilterType.SKETCH:
                gpuImage.setFilter(new GPUImageSketchFilter());
                break;
            case FilterType.FALSE_COLOR:
                gpuImage.setFilter(new GPUImageFalseColorFilter());
                break;
        }
        imageThumbnail = gpuImage.getBitmapWithFilterApplied(mBitmap);
        holder.imageViewFilter.setImageBitmap(imageThumbnail);
    }

    @Override
    public int getItemCount() {
        return mFilterImages.size();
    }

    private void initListFilter() {
        mFilterImages = new ArrayList<>();
        mFilterImages.add(new FilterImage(FilterType.CONTRAST, mContext.getString(R.string.filter_name_contrast)));
        mFilterImages.add(new FilterImage(FilterType.INVERT, mContext.getString(R.string.filter_name_invert)));
        mFilterImages.add(new FilterImage(FilterType.HUE, mContext.getString(R.string.filter_name_hue)));
        mFilterImages.add(new FilterImage(FilterType.SEPIA, mContext.getString(R.string.filter_name_sepia)));
        mFilterImages.add(new FilterImage(FilterType.GRAYSCALE, mContext.getString(R.string.filter_name_gray_scale)));
        mFilterImages.add(new FilterImage(FilterType.SHARPNESS, mContext.getString(R.string.filter_name_sharpness)));
        mFilterImages.add(new FilterImage(FilterType.SOBEL_EDGE_DETECTION, mContext.getString(R.string.filter_name_sobel_edge_detection)));
        mFilterImages.add(new FilterImage(FilterType.VIGNETTE, mContext.getString(R.string.filter_name_vignette)));
        mFilterImages.add(new FilterImage(FilterType.SKETCH, mContext.getString(R.string.filter_name_sketch)));
        mFilterImages.add(new FilterImage(FilterType.FALSE_COLOR, mContext.getString(R.string.filter_name_false_color)));
    }

    @IntDef({FilterType.CONTRAST, FilterType.INVERT, FilterType.HUE, FilterType.SEPIA,
            FilterType.GRAYSCALE, FilterType.SHARPNESS, FilterType.SOBEL_EDGE_DETECTION,
            FilterType.VIGNETTE, FilterType.SKETCH, FilterType.FALSE_COLOR})
    @Retention(RetentionPolicy.SOURCE)
    @interface FilterType {
        int CONTRAST = 1;
        int INVERT = 2;
        int HUE = 3;
        int SEPIA = 4;
        int GRAYSCALE = 5;
        int SHARPNESS = 6;
        int SOBEL_EDGE_DETECTION = 7;
        int VIGNETTE = 8;
        int SKETCH = 9;
        int FALSE_COLOR = 10;
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final CircleImageView imageViewFilter;
        private final TextView tvNameFilter;

        public FilterViewHolder(View itemView) {
            super(itemView);
            imageViewFilter = (CircleImageView) itemView.findViewById(R.id.circleImageViewFilter);
            tvNameFilter = (TextView) itemView.findViewById(R.id.tvNameFilter);
            imageViewFilter.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
    public interface OnClickItemAdapter{
        void onClick(int filterType);
    }
}
