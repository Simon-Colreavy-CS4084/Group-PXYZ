package com.example.recipes.view.filter;

   
public interface FilterParent {

       
    void remove(FilterNode node);

       
    void requestSelect(FilterNode trigger, boolean selected);

}
