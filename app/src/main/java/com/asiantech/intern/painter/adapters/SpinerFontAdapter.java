package com.asiantech.intern.painter.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.asiantech.intern.painter.R;

/**
 * Copyright © 2016 YNC.
 * Created by Congybk on 9/15/2016.
 */
public class SpinerFontAdapter extends ArrayAdapter<String> {
    private final Context mContext;
    private final String[] mFonts;
    public SpinerFontAdapter(Context context, int textViewResourceId, String[] fonts) {
        super(context, textViewResourceId, fonts);
        mContext = context;
        mFonts = fonts;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View row = inflater.inflate(R.layout.item_row_spiner_font, parent, false);
        TextView tvRow = (TextView) row.findViewById(R.id.tvRow);
        tvRow.setTypeface(Typeface.createFromAsset(mContext.getAssets(),mFonts[position]));
        return row;
    }

}
