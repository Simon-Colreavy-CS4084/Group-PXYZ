package com.example.recipes.adapter;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.DraggableModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.recipes.R;
import com.example.recipes.data.FoodsData;
import com.example.recipes.data.StepData;
import com.example.recipes.fragment.recipc.FragmentRecipc3;
import com.example.recipes.fragment.recipc.FragmentRecipc4;
import com.example.recipes.ui.recipc.RecipcStepActivity;
import com.example.recipes.util.FileUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;


   

public class RecipcStepAdapter extends BaseQuickAdapter<StepData, BaseViewHolder> implements DraggableModule {
    private FragmentRecipc4 fragmentRecipc4;

       
    public RecipcStepAdapter(List<StepData> list, FragmentRecipc4 fragmentRecipc4) {
        super(R.layout.item_recipc_step, list);
        this.fragmentRecipc4 = fragmentRecipc4;
    }

       
    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull StepData stepData) {
        helper.setText(R.id.tv_1, "step " + (helper.getLayoutPosition() + 1));
        helper.setText(R.id.tv_2, stepData.getDesc());

        ImageView iv_image = helper.getView(R.id.iv_image);
        ImageView iv_voide = helper.getView(R.id.iv_voide);
        if (TextUtils.isEmpty(stepData.getImage())) {
            iv_voide.setVisibility(View.GONE);
            Glide.with(iv_image).load(R.mipmap.ic_photograph)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                    .into(iv_image);
        } else {
            if (FileUtils.getFileType(stepData.getImage()) == 1) {
                iv_voide.setVisibility(View.GONE);
            } else {
                iv_voide.setVisibility(View.VISIBLE);
            }
            Glide.with(iv_image).load(stepData.getImage())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                    .into(iv_image);
        }

        helper.getView(R.id.iv_more).setTag(R.id.view_tag, helper.getPosition());
        helper.getView(R.id.iv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int) v.getTag(R.id.view_tag);
                showListDialog(index);
            }
        });

        helper.getView(R.id.ll_show).setTag(R.id.view_tag, helper.getPosition());
        helper.getView(R.id.ll_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int) v.getTag(R.id.view_tag);
                Intent intent = new Intent(v.getContext(), RecipcStepActivity.class);
                intent.putExtra("index", index);
                intent.putExtra("data", getData().get(index));
                fragmentRecipc4.startActivityForResult(intent, 1);
            }
        });

    }


    private void showListDialog(int index) {
        String[] items = new String[]{"edit", "delete"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(fragmentRecipc4.getContext());
        listDialog.setTitle("Please select your action");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                                                    if (which == 0) {
                    Intent intent = new Intent(fragmentRecipc4.getContext(), RecipcStepActivity.class);
                    intent.putExtra("index", index);
                    intent.putExtra("data", getData().get(index));
                    fragmentRecipc4.startActivityForResult(intent, 1);
                } else if (which == 1) {
                    removeAt(index);
                }
            }
        });
        listDialog.show();
    }

}