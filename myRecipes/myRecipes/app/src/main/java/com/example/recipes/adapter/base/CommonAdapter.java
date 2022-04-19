package com.example.recipes.adapter.base;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;


public abstract class CommonAdapter<T> extends BaseAdapter implements AbsListView.OnScrollListener {
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mDatas;
    protected final int mItemLayoutId;


    public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;

    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public T getItem(int position) {
        if (mDatas == null) {
            return null;
        }
        if (position >= mDatas.size()) {
            return null;
        }
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, convertView,
                parent);
        convert(viewHolder, getItem(position));
        return viewHolder.getConvertView();

    }

    public abstract void convert(ViewHolder helper, T item);

    private ViewHolder getViewHolder(int position, View convertView,
                                     ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
                position);
    }


    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {//list停止滚动时加载图片
            pageImgLoad(_start_index, _end_index);
        }
    }

    public void pageImgLoad(int start_index, int end_index) {

    }

    private int _start_index = 0;
    private int _end_index = 0;

    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub

        _start_index = firstVisibleItem;
        _end_index = firstVisibleItem + visibleItemCount;
        if (_end_index >= totalItemCount) {
            _end_index = totalItemCount - 1;
        }
    }


    public List<T> getDatas() {
        if (mDatas == null) {
            return new ArrayList<>();
        }
        return mDatas;
    }


    public void addDatas(List<T> datas) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        if (datas != null) {
            mDatas.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public void addData(int index, T data) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        if (data != null) {
            mDatas.add(index, data);
        }
        notifyDataSetChanged();
    }

    public void newDatas(List<T> datas) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.clear();
        if (datas != null) {
            mDatas.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public int getDataSize() {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        return mDatas.size();
    }

}

