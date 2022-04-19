package com.example.recipes.adapter;


import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.recipes.R;
import com.example.recipes.data.FollowData;
import com.example.recipes.data.Msg;
import com.example.recipes.data.user_info;
import com.example.recipes.ui.FollowActivity;
import com.example.recipes.ui.UserInfoActivity;
import com.example.recipes.util.DataDispose;
import com.example.recipes.util.LattePreference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


   

public class FollowListAdapter extends BaseQuickAdapter<FollowData, BaseViewHolder> {
    private List<FollowData> meFollowData = new ArrayList<>();      private int type = 0;
    public void setMeFollowData(List<FollowData> meFollowData) {
        this.meFollowData = meFollowData;
    }

       
    public FollowListAdapter(List<FollowData> list, int type) {
        super(R.layout.item_follow_list, list);
        this.type = type;
    }

       
    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull FollowData followData) {
        ImageView iv_show = helper.getView(R.id.iv_show);
        iv_show.setTag(R.id.view_tag, followData);
        if (type == 0) {
            helper.setText(R.id.tv_show, followData.getFollowUser().showName());
            String image = "http://";
            if (followData.getFollowUser().getImage() != null && !TextUtils.isEmpty(followData.getFollowUser().getImage().getUrl())) {
                image = followData.getFollowUser().getImage().getUrl();
            }

            Glide.with(iv_show).load(image)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(iv_show);
        } else {
            iv_show.setTag(R.id.view_tag, followData);
            helper.setText(R.id.tv_show, followData.getMeUser().showName());

            String image = "http://";
            if (followData.getMeUser().getImage() != null && !TextUtils.isEmpty(followData.getMeUser().getImage().getUrl())) {
                image = followData.getMeUser().getImage().getUrl();
            }
            Glide.with(iv_show).load(image)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(iv_show);
        }

        iv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowData followData = (FollowData) v.getTag(R.id.view_tag);
                Intent intent = new Intent(v.getContext(), UserInfoActivity.class);
                if (type == 0) {
                    intent.putExtra("user", followData.getFollowUser());
                } else {
                    intent.putExtra("user", followData.getMeUser());
                }

                v.getContext().startActivity(intent);
            }
        });

        TextView btn_follow = helper.getView(R.id.btn_follow);
        boolean isFollow = false;
        btn_follow.setTag(R.id.view_tag, followData);
        for (FollowData followData1 : meFollowData) {
            if (type == 0) {
                if (followData1.getFollowUser().getObjectId().equals(followData.getFollowUser().getObjectId())) {
                    isFollow = true;
                    btn_follow.setTag(R.id.view_tag, followData1);
                    break;
                }
            } else {
                if (followData1.getFollowUser().getObjectId().equals(followData.getMeUser().getObjectId())) {
                    isFollow = true;
                    btn_follow.setTag(R.id.view_tag, followData1);
                    break;
                }
            }
        }

        if (isFollow) {
            btn_follow.setText("Cancel Follow");
            btn_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FollowData followData = (FollowData) v.getTag(R.id.view_tag);
                    if (followData != null) {
                        followData.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    if (getContext() instanceof FollowActivity) {
                                        ((FollowActivity) getContext()).getNewsFeedData();
                                    }

                                } else {
                                    Toast.makeText(v.getContext(), "Cancel Follow Fail", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            });
        } else {
            btn_follow.setText("follow");
            btn_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FollowData followData = (FollowData) v.getTag(R.id.view_tag);
                    FollowData followData1 = new FollowData();
                    followData1.setMeUser(LattePreference.getAppUserInfo());
                    if (type == 0) {
                        followData1.setFollowUser(followData.getFollowUser());
                    } else {
                        followData1.setFollowUser(followData.getMeUser());
                    }
                    if (followData1.getFollowUser().getObjectId().equals(LattePreference.getAppUserInfo().getObjectId())) {
                        Toast.makeText(v.getContext(), "You can't send messages to yourself", Toast.LENGTH_LONG).show();
                        return;
                    }

                    followData1.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                                                  Toast.makeText(v.getContext(), "Follow successful", Toast.LENGTH_LONG).show();
                                if (getContext() instanceof FollowActivity) {
                                    ((FollowActivity) getContext()).getNewsFeedData();
                                }
                            } else {
                                                                  Log.e("asd", e.getMessage());
                                Toast.makeText(v.getContext(), "Follow Fail", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        }

    }


}