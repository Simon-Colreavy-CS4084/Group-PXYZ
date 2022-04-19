package com.example.recipes.adapter;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;


import com.example.recipes.R;
import com.example.recipes.util.FileUtils;
import com.example.recipes.util.GlideEngine;
import com.youth.banner.util.BannerUtils;

import java.util.ArrayList;
import java.util.List;

import cc.shinichi.library.ImagePreview;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bmob.v3.datatype.BmobFile;
import cn.jzvd.JzvdStd;

public class BannerFunAdapter implements BGABanner.Adapter<RelativeLayout, String> {
    private List<String> mDatas;
    private Context context;

    public BannerFunAdapter(Context context, List<String> mDatas) {
        this.mDatas = mDatas;
        this.context = context;
    }

    @Override
    public void fillBannerItem(BGABanner banner, RelativeLayout itemView, @Nullable String model, int position) {
        ImageView ivShow = itemView.findViewById(R.id.iv_show);
                        ImageView iv_voide = itemView.findViewById(R.id.iv_voide);
        String path = model;
        if (FileUtils.getFileType(path) == 1) {
            iv_voide.setVisibility(View.GONE);
        } else {
            iv_voide.setVisibility(View.VISIBLE);
        }

        GlideEngine.createGlideEngine().loadImage(ivShow.getContext(),path,ivShow);

        ivShow.setTag(R.id.view_tag, model);
        ivShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = (String) view.getTag(R.id.view_tag);
                if (FileUtils.getFileType(path) == 2) {
                        JzvdStd.startFullscreenDirectly(context, JzvdStd.class, path, "");
                        return;
                }
                ImagePreview
                        .getInstance()
                        .setContext(context)
                        .setIndex(0)
                        .setEnableDragClose(true)
                        .setShowDownButton(false)
                        .setImage(path)
                        .start();
            }
        });
    }
}
