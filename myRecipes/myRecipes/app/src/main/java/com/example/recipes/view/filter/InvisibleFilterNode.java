package com.example.recipes.view.filter;

   
public class InvisibleFilterNode extends FilterNode {

    public InvisibleFilterNode(FilterNode node) {
        setDisplayName(node.getDisplayName());
        setSubInfo(node.getSubInfo());
        setID(node.getID());
        setData(node.getData());
    }

}
