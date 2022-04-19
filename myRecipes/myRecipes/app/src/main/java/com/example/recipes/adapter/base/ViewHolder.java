package com.example.recipes.adapter.base;


import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recipes.R;


public class ViewHolder {
    private final SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;

    private ViewHolder(Context context, ViewGroup parent, int layoutId,
                       int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        // setTag
        mConvertView.setTag(this);
        mConvertView.setTag(R.id.tag, position);
    }

       
    public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        }
        convertView.setTag(R.id.tag, position);
        return (ViewHolder) convertView.getTag();
    }

    public View getConvertView() {
        return mConvertView;
    }

       
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

       
    public ViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        if (TextUtils.isEmpty(text)) {
            view.setText("");
        } else {
            view.setText(text);
        }
        return this;
    }

       
    public ViewHolder setTextColor(int viewId, int color) {
        TextView view = getView(viewId);
        view.setTextColor(color);
        return this;
    }

       
    public ViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);

        return this;
    }

       
    public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }


    public int getPosition() {
        try {
            return (int) mConvertView.getTag(R.id.tag);
        } catch (Exception e) {

        }
        return mPosition;
    }

}
