package com.example.recipes.view.filter;

import android.text.TextUtils;

import java.util.HashSet;
import java.util.Set;

   
public class FilterNode {

           private String mDisplayName;
    private String mSubInfo;

           private String mID = "";

    private Set<String> mMutexCodes = new HashSet<String>();

    private FilterParent mParent;
    boolean mIsSelected = false;

    private Object mData;

    private Object mTag;

       
    public final void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

       
    public final String getDisplayName() {
        return mDisplayName;
    }

    public void setSubInfo(String info) {
        mSubInfo = info;
    }

    public String getSubInfo() {
        return mSubInfo;
    }

       
    public final void setID(String id) {
        mID = id;
    }

       
    public final String getID() {
        return mID;
    }

       
    public void setData(Object data) {
        mData = data;
    }

       
    public <T> T getData() {
        return (T) mData;
    }

       
    public void setTag(Object tag) {
        mTag = tag;
    }

       
    public <T> T getTag() {
        return (T) mTag;
    }

       
    public final void setParent(FilterParent parent) {
        mParent = parent;
    }

       
    public final FilterParent getParent() {
        return mParent;
    }

       
    public boolean isLeaf() {
        return true;
    }

       
    public void requestSelect(boolean selected) {
        if (mIsSelected != selected && mParent != null) {
            mParent.requestSelect(this, selected);
        }
    }

       
    protected boolean forceSelect(boolean selected) {
        return setSelected(selected);
    }

       
    protected boolean setSelected(boolean selected) {
        if (mIsSelected != selected) {
            mIsSelected = selected;
            if (mOnSelectChangeListener != null) {
                mOnSelectChangeListener.onSelectChange(this, selected);
            }
            return selected;
        }
        return false;
    }

       
    public boolean isSelected() {
        return mIsSelected;
    }

       
    protected boolean refreshSelectState(FilterNode trigger, boolean selected) {
        if (trigger.isEquals(this)) {
            return forceSelect(selected);
        } else if (selected && isExclusive(trigger)) {
            forceSelect(false);
        }
        return false;
    }

       
    public void addMutexCode(String mutexCode) {
        mMutexCodes.add(mutexCode);
    }

    private boolean isExclusive(FilterNode node) {
        return !TextUtils.isEmpty(node.mID) && mMutexCodes.contains(node.mID);
    }

    public boolean isEquals(Object o) {
        if (o instanceof FilterNode) {
            FilterNode right = (FilterNode) o;
            if (TextUtils.isEmpty(mID) || TextUtils.isEmpty(right.mID)) {
                return this == right;
            }
            return mID.equals(right.mID);
        }
        return false;
    }

    public boolean contain(FilterNode node, boolean accordingReference) {
        return (accordingReference && this == node) || (!accordingReference && isEquals(node));
    }

    public FilterNode findNode(FilterNode node, boolean accordingReference) {
        if (accordingReference && this == node || !accordingReference && isEquals(node)) {
            return this;
        }
        return null;
    }

    private OnSelectChangeListener mOnSelectChangeListener;

       
    public void setOnSelectChangeListener(OnSelectChangeListener listener) {
        mOnSelectChangeListener = listener;
    }

    public interface OnSelectChangeListener {
           
        void onSelectChange(FilterNode node, boolean selected);
    }
}
