package com.example.recipes.adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.recipes.R;
import com.example.recipes.data.FoodsData;
import com.example.recipes.data.NewsFeedData;
import com.example.recipes.fragment.recipc.FragmentRecipc3;
import com.example.recipes.ui.newsfeed.NewsFeedEditActivity;
import com.example.recipes.ui.recipc.RecipcFoodsActivity;
import com.example.recipes.view.HintPopupWindow;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


   

public class RecipcFoodsAdapter extends BaseQuickAdapter<FoodsData, BaseViewHolder> {
    private FragmentRecipc3 fragmentRecipc3;

       
    public RecipcFoodsAdapter(List<FoodsData> list, FragmentRecipc3 fragmentRecipc3) {
        super(R.layout.item_recipc_foods, list);
        this.fragmentRecipc3 = fragmentRecipc3;
    }

       
    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull FoodsData foodsData) {
        helper.setText(R.id.tv_1, foodsData.getNum());
        helper.setText(R.id.tv_2, foodsData.getUnit());
        helper.setText(R.id.tv_3, foodsData.getName());

        helper.getView(R.id.iv_more).setTag(R.id.view_tag, helper.getPosition());
        helper.getView(R.id.iv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int) v.getTag(R.id.view_tag);
                showListDialog(v, index);
            }
        });

        helper.getView(R.id.ll_show).setTag(R.id.view_tag, helper.getPosition());
        helper.getView(R.id.ll_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int) v.getTag(R.id.view_tag);
                Intent intent = new Intent(v.getContext(), RecipcFoodsActivity.class);
                intent.putExtra("index", index);
                intent.putExtra("data", getData().get(index));
                fragmentRecipc3.startActivityForResult(intent, 1);
            }
        });

    }


    private HintPopupWindow hintPopupWindow = null;
    private int index;

    private void showListDialog(View v, int index1) {
        index = index1;

        if (v.getContext() instanceof Activity) {
            if (hintPopupWindow == null) {
                                  ArrayList<String> strList = new ArrayList<>();
                strList.add("edit");
                strList.add("delete");

                ArrayList<View.OnClickListener> clickList = new ArrayList<>();
                clickList.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(fragmentRecipc3.getContext(), RecipcFoodsActivity.class);
                        intent.putExtra("index", index);
                        intent.putExtra("data", getData().get(index));
                        fragmentRecipc3.startActivityForResult(intent, 1);
                    }
                });
                clickList.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeAt(index);
                    }
                });

                                  hintPopupWindow = new HintPopupWindow((Activity) v.getContext(), strList, clickList);

            }
                          hintPopupWindow.showPopupWindow(v);
        }

    }

}