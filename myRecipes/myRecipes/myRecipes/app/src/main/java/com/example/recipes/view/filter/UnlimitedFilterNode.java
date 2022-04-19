package com.example.recipes.view.filter;

   
public class UnlimitedFilterNode extends FilterNode {

    @Override
    public void requestSelect(boolean selected) {
        if (selected) {
            super.requestSelect(selected);
        }
    }
}
