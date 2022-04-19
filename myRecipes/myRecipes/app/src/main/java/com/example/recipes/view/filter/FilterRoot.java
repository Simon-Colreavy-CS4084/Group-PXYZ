package com.example.recipes.view.filter;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

   
public class FilterRoot extends FilterGroup {

    private Map<String, FilterNode> mChildrenMap = new HashMap<String, FilterNode>();

    @Override
    public synchronized void addNode(FilterNode node) {
        super.addNode(node);
        if (node instanceof FilterGroup) {
            FilterGroup group = (FilterGroup) node;
            String type = group.getType();
            if (!TextUtils.isEmpty(type)) {
                mChildrenMap.put(type, node);
            }
        }
    }

       
    public synchronized <T extends FilterNode> T getChild(String type) {
        return (T) mChildrenMap.get(type);
    }

    @Override
    public synchronized void requestSelect(FilterNode trigger, boolean selected) {
        if (!(trigger instanceof UnlimitedFilterNode)) {
            refreshSelectState(trigger, selected);
        }
    }

       
    public synchronized void resetFilterTree(boolean force) {
        if (force) {
            resetFilterGroup();
        } else {
            forceSelect(false);
        }
    }

       
    @Override
    public synchronized void save() {
        for (FilterNode child : mChildren) {
            FilterGroup group = (FilterGroup) child;
            group.save();
        }
    }

       
    @Override
    public synchronized void restore() {
        for (FilterNode child : mChildren) {
            FilterGroup group = (FilterGroup) child;
            group.restore();
        }
    }

       
    @Override
    public synchronized void discardHistory() {
        for (FilterNode child : mChildren) {
            FilterGroup group = (FilterGroup) child;
            group.discardHistory();
        }
    }

       
    @Override
    public synchronized boolean hasFilterChanged() {
        for (FilterNode child : mChildren) {
            FilterGroup group = (FilterGroup) child;
            if (group.hasFilterChanged()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean performOpen(FilterGroupOpenListener listener) {
        boolean hasOpened = true;
        for (FilterNode child : mChildren) {
            if (child instanceof FilterGroup) {
                FilterGroup group = (FilterGroup) child;
                if (group.canOpen() && !group.hasOpened()) {
                    hasOpened &= group.open(listener);
                }
            }
        }
        return hasOpened;
    }
}
