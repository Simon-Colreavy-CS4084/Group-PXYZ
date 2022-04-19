package com.example.recipes.view.filter;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

   
public class FilterGroup extends FilterNode implements FilterParent {

    private static final String TAG = FilterGroup.class.getSimpleName();

    protected List<FilterNode> mChildren = new ArrayList<FilterNode>();

    protected String mType;

          private boolean mSingleChoice = false;

    protected boolean mHasOpened = false;

    protected List<FilterNode> mHistorySelectList;

       
    public synchronized void addNode(FilterNode node) {
        node.setParent(this);
        mChildren.add(node);
    }

       
    @Override
    public synchronized void remove(FilterNode node) {
        if (mChildren.remove(node)) {
            node.setParent(null);
        }
    }

       
    public synchronized List<FilterNode> getChildren(boolean containUnlimited) {
        List<FilterNode> children = new ArrayList<FilterNode>(mChildren);
        int childrenCount = children.size();
        for (int i = childrenCount - 1; i >= 0; i--) {
            FilterNode child = children.get(i);
            if (child instanceof InvisibleFilterNode || (!containUnlimited && child instanceof UnlimitedFilterNode)) {
                children.remove(i);
            }
        }
        return children;
    }

       
    public synchronized boolean isEmpty(boolean containUnlimited) {
        List<FilterNode> children = getChildren(containUnlimited);
        return children.isEmpty();
    }

       
    public void setType(String type) {
        mType = type;
    }

       
    public String getType() {
        return mType;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    public synchronized void addSelectNode(FilterNode node) {
        if (!contain(node)) {
            InvisibleFilterNode invisibleNode = new InvisibleFilterNode(node);
            dispatchUnknownNode(invisibleNode);
        }
        requestSelect(node, true);
    }

    protected void dispatchUnknownNode(FilterNode node) {
        addNode(node);
    }

       
    public synchronized void removeUnselectedInvisibleNode() {
        int childrenCount = mChildren.size();
        for (int i = childrenCount - 1; i >= 0; i--) {
            FilterNode child = mChildren.get(i);
            if (child instanceof FilterGroup) {
                FilterGroup group = (FilterGroup) child;
                group.removeUnselectedInvisibleNode();
            } else if (child instanceof InvisibleFilterNode && !child.isSelected()) {
                remove(child);
            }
        }
    }

    @Override
    protected synchronized boolean forceSelect(boolean selected) {
        if (mIsSelected && !selected) {
            for (FilterNode child : mChildren) {
                if (child instanceof UnlimitedFilterNode) {
                    child.setSelected(true);
                } else {
                    child.forceSelect(false);
                }
            }
        }
        return super.setSelected(selected);
    }

    @Override
    protected synchronized boolean setSelected(boolean selected) {
        if (mIsSelected && !selected) {
            for (FilterNode child : mChildren) {
                if (child instanceof UnlimitedFilterNode) {
                    child.setSelected(true);
                }
            }
        }
        return super.setSelected(selected);

    }

    @Override
    public void requestSelect(boolean selected) {
        if (!selected) {
            List<FilterNode> selectedLeafNodes = getSelectedLeafNodes();
            for (FilterNode node : selectedLeafNodes) {
                node.requestSelect(false);
            }
        }
    }

    private synchronized List<FilterNode> getTriggerFirstChildren(FilterNode trigger) {
        if (contain(trigger, true)) {
            List<FilterNode> children = new ArrayList<FilterNode>(mChildren);
            int childrenCount = children.size();
            for (int i = 0; i < childrenCount; i++) {
                FilterNode child = children.get(i);
                if (child.contain(trigger, true)) {
                    children.remove(child);
                    children.add(0, child);
                    break;
                }
            }
            return children;
        } else {
            return mChildren;
        }
    }

       
    public void setSingleChoice() {
        mSingleChoice = true;
    }

       
    public boolean isSingleChoice() {
        return mSingleChoice;
    }

    @Override
    public synchronized void requestSelect(FilterNode trigger, boolean selected) {
        if (trigger instanceof UnlimitedFilterNode) {
            if (selected) {
                List<FilterNode> selectedLeafNodes = getSelectedLeafNodes();
                for (FilterNode child : selectedLeafNodes) {
                    child.requestSelect(false);
                }
                trigger.setSelected(true);
            }
            return;
        } else if (trigger instanceof AllFilterNode && selected) {
            FilterParent triggerParent = trigger.getParent();
            if (triggerParent == this) {
                List<FilterNode> selectedLeafNodes = getSelectedLeafNodes();
                for (FilterNode node : selectedLeafNodes) {
                    node.requestSelect(false);
                }
            }
        }

        FilterParent parent = getParent();
        if (parent != null) {
            parent.requestSelect(trigger, selected);
        }

    }

    @Override
    protected synchronized boolean refreshSelectState(FilterNode trigger, boolean selected) {
        if (mSingleChoice) {
            List<FilterNode> selectedChildrenList = getSelectedChildren();
                          List<FilterNode> children = getTriggerFirstChildren(trigger);
            for (FilterNode child : children) {
                boolean newSelected = child.refreshSelectState(trigger, selected);
                                  if (newSelected || child.contain(trigger, false)) {
                    if (newSelected && !selectedChildrenList.isEmpty()) {
                        FilterNode node = selectedChildrenList.get(0);
                                                  if (node instanceof FilterGroup) {
                            FilterGroup group = (FilterGroup) node;
                            List<FilterNode> selectLeafNodes = group.getSelectedLeafNodes();
                            for (FilterNode selectNode : selectLeafNodes) {
                                selectNode.requestSelect(false);
                            }
                        } else {
                            node.requestSelect(false);
                        }
                    }
                    break;
                }
            }
        } else {
            FilterNode allNode = findAllNode();
                          boolean shouldUnSelectAllNode = !(trigger instanceof AllFilterNode) && contain(trigger) && !trigger.isEquals(allNode);
            for (FilterNode child : mChildren) {
                if (shouldUnSelectAllNode && child instanceof AllFilterNode) {
                    child.setSelected(false);
                } else {
                    child.refreshSelectState(trigger, selected);
                }
            }
        }
        boolean isSelected = mIsSelected;
        int selectedChildrenCount = getSelectedChildrenCount();
        if (selectedChildrenCount > 0) {
            FilterNode unlimitedNode = findUnlimitedNode();
            if (unlimitedNode != null) {
                unlimitedNode.setSelected(false);
            }
                          setSelected(true);
        } else {
            setSelected(false);
        }

        return !isSelected && mIsSelected;
    }

       
    public synchronized FilterNode findUnlimitedNode() {
        for (FilterNode child : mChildren) {
            if (child instanceof UnlimitedFilterNode) {
                return child;
            }
        }
        return null;
    }

       
    private synchronized FilterNode findAllNode() {
        for (FilterNode child : mChildren) {
            if (child instanceof AllFilterNode) {
                return child;
            }
        }
        return null;
    }

       
    public synchronized List<FilterNode> getSelectedChildren() {
        List<FilterNode> selectedChildrenList = new ArrayList<FilterNode>();
        for (FilterNode child : mChildren) {
            if (!(child instanceof UnlimitedFilterNode) && child.isSelected()) {
                selectedChildrenList.add(child);
            }
        }
        return selectedChildrenList;
    }

       
    public synchronized int getSelectedChildrenCount() {
        List<FilterNode> selectedChildrenList = getSelectedChildren();
        return selectedChildrenList.size();
    }

       
    public synchronized int getFirstSelectChildPosition(boolean containUnlimited) {
        List<FilterNode> children = getChildren(containUnlimited);
        int childrenCount = children.size();
        for (int i = 0; i < childrenCount; i++) {
            FilterNode child = children.get(i);
            if (child.isSelected()) {
                return i;
            }
        }
        return -1;
    }

       
    public synchronized List<FilterNode> getSelectedLeafNodes() {
        List<FilterNode> selectLeafNodeList = new ArrayList<FilterNode>();
        for (FilterNode child : mChildren) {
            if (child.isSelected()) {
                if (child instanceof FilterGroup) {
                    FilterGroup group = (FilterGroup) child;
                    List<FilterNode> childSelectLeafNodeList = group.getSelectedLeafNodes();
                    selectLeafNodeList.addAll(childSelectLeafNodeList);
                } else if (!(child instanceof UnlimitedFilterNode)) {
                    selectLeafNodeList.add(child);
                }
            }
        }

                  Set<String> selectedIDs = new HashSet<String>();
        for (int i = 0; i < selectLeafNodeList.size(); i++) {
            FilterNode node = selectLeafNodeList.get(i);
            String id = node.getID();
            if (!TextUtils.isEmpty(id)) {
                if (selectedIDs.contains(id)) {
                    selectLeafNodeList.remove(i);
                    --i;
                } else {
                    selectedIDs.add(id);
                }
            }
        }
        return selectLeafNodeList;
    }

    protected synchronized void resetFilterGroup() {
        for (FilterNode child : mChildren) {
            if (child instanceof FilterGroup) {
                FilterGroup group = (FilterGroup) child;
                group.resetFilterGroup();
            } else if (child instanceof UnlimitedFilterNode) {
                child.setSelected(true);
            } else {
                child.setSelected(false);
            }
        }
        super.setSelected(false);
    }

       
    public synchronized void save() {
        mHistorySelectList = getSelectedLeafNodes();
    }

       
    public synchronized void restore() {
        restore(mHistorySelectList);
        if (mHistorySelectList != null) {
            discardHistory();
        }
    }

       
    private synchronized void restore(List<FilterNode> selectLeafNodes) {
        if (selectLeafNodes != null) {
            forceSelect(false);

            for (FilterNode node : selectLeafNodes) {
                addSelectNode(node);
            }
        }
    }

       
    public synchronized void discardHistory() {
        mHistorySelectList = null;
    }

       
    public synchronized boolean hasFilterChanged() {
        if (mHistorySelectList == null) {
            return true;
        }
        Set<String> selectLeafIDs = new HashSet<String>();
        Set<FilterNode> selectUnknownLeafNodes = new HashSet<FilterNode>();
        for (FilterNode node : mHistorySelectList) {
            String id = node.getID();
            if (!TextUtils.isEmpty(id)) {
                selectLeafIDs.add(id);
            } else {
                selectUnknownLeafNodes.add(node);
            }
        }
        List<FilterNode> selectLeafNodeList = getSelectedLeafNodes();
        for (FilterNode node : selectLeafNodeList) {
            String id = node.getID();
            if (!TextUtils.isEmpty(id)) {
                if (!selectLeafIDs.remove(id)) {
                    return true;
                }
            } else if (!selectUnknownLeafNodes.remove(node)) {
                return true;
            }
        }
        return !selectLeafIDs.isEmpty() || !selectUnknownLeafNodes.isEmpty();
    }

       
    public synchronized boolean contain(FilterNode node) {
        return contain(node, false);
    }

       
    @Override
    public synchronized boolean contain(FilterNode node, boolean accordingReference) {
        String id = node.getID();
        if (!accordingReference && TextUtils.isEmpty(id)) {
            return false;
        }
        for (FilterNode child : mChildren) {
            if (child.contain(node, accordingReference)) {
                return true;
            } else if (child == node) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized FilterNode findNode(FilterNode node, boolean accordingReference) {
        String id = node.getID();
        if (!accordingReference && TextUtils.isEmpty(id)) {
            return null;
        }
        for (FilterNode child : mChildren) {
            FilterNode result = child.findNode(node, accordingReference);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public boolean canOpen() {
        return mFilterGroupOpenPerformer != null;
    }

    public boolean hasOpened() {
        return mHasOpened;
    }

    private Object mOpenLock = new Object();

    public boolean open(FilterGroupOpenListener listener) {
        synchronized (mOpenLock) {
            if (!mHasOpened) {
                if (listener != null) {
                    listener.onOpenStart(this);
                }
                mHasOpened = performOpen(listener);

                dispatchUnknownNodeToChildren();

                if (listener != null) {
                    if (mHasOpened) {
                        listener.onOpenSuccess(this);
                    } else {
                        listener.onOpenFail(this, "");
                    }
                }
            }
            return mHasOpened;
        }
    }

    protected boolean performOpen(FilterGroupOpenListener listener) {
        if (mFilterGroupOpenPerformer != null) {
            return mFilterGroupOpenPerformer.performOpen(this);
        }
        return false;
    }

    protected void dispatchUnknownNodeToChildren() {
        List<FilterNode> selectLeafNodes = getSelectedLeafNodes();
        resetFilterGroup();
        removeUnselectedInvisibleNode();
        restore(selectLeafNodes);
    }

    private FilterGroupOpenPerformer mFilterGroupOpenPerformer;

    public void setFilterGroupOpenPerformer(FilterGroupOpenPerformer performer) {
        mFilterGroupOpenPerformer = performer;
    }

    public interface FilterGroupOpenPerformer {
        boolean performOpen(FilterGroup group);
    }

    public interface FilterGroupOpenListener {

        void onOpenStart(FilterGroup group);

        void onOpenSuccess(FilterGroup group);

        void onOpenFail(FilterGroup group, String errorMessage);

    }

}
