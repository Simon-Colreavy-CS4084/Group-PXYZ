package com.example.recipes.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.recipes.R;
import com.example.recipes.data.NewsFeedData;
import com.example.recipes.data.RecipcData;
import com.example.recipes.ui.FinishActivity;
import com.example.recipes.ui.UserInfoActivity;
import com.example.recipes.ui.recipc.RecipcDetailActivity;
import com.example.recipes.ui.recipc.RecipcEditActivity;
import com.example.recipes.util.DataDispose;
import com.example.recipes.util.LattePreference;
import com.example.recipes.view.HintPopupWindow;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

  public class RecipcAdapter extends BaseQuickAdapter<RecipcData, BaseViewHolder> {

       
    public RecipcAdapter(List<RecipcData> list) {
        super(R.layout.item_recipc, list);
    }


    private UpDataListener upDataListener;

    public void setUpDataListener(UpDataListener upDataListener) {
        this.upDataListener = upDataListener;
    }

       
    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull RecipcData item) {
        helper.setText(R.id.tv_time, item.getCookingTime() + " 分钟");
        helper.setText(R.id.tv_show, item.getTitle());
        helper.setText(R.id.tv_top, item.getAuthor().showName());

        String hint = "@" + item.getAuthor().getUser_no() + " · " + DataDispose.getIntervalTime(item.getCreatedAt());
        helper.setText(R.id.tv_top_hint, hint);
        ImageView iv_top = helper.getView(R.id.iv_top);
        ImageView iv_more = helper.getView(R.id.iv_more);
        if (item.getAuthor().getObjectId().equals(LattePreference.getAppUserInfo().getObjectId())) {
            iv_more.setVisibility(View.VISIBLE);
        } else {
            iv_more.setVisibility(View.GONE);
        }

        String image = "http:          if (item.getAuthor().getImage() != null && !TextUtils.isEmpty(item.getAuthor().getImage().getUrl())) {
            image = item.getAuthor().getImage().getUrl();
        }
        Glide.with(iv_top).load(image)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(iv_top);

        ImageView iv_show = helper.getView(R.id.iv_show);
        String images = "http:          if (!TextUtils.isEmpty(item.getImg())) {
            images = item.getImg();
        }
        Glide.with(iv_show).load(images)
                .into(iv_show);

        iv_more.setTag(R.id.view_tag, item);
        iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipcData item = (RecipcData) v.getTag(R.id.view_tag);
                showListDialog(v, item);
            }
        });

        helper.getView(R.id.rl_user).setTag(R.id.view_tag, item);
        helper.getView(R.id.rl_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipcData item = (RecipcData) v.getTag(R.id.view_tag);
                Intent intent = new Intent(v.getContext(), UserInfoActivity.class);
                intent.putExtra("user", item.getAuthor());
                v.getContext().startActivity(intent);
            }
        });


        helper.setText(R.id.tv_bottom_like, "");
        ShineButton shineButton = (ShineButton) helper.getView(R.id.sb_bottom_like);
        shineButton.setChecked(false, false);
        if (!TextUtils.isEmpty(item.getLikess())) {

            helper.setText(R.id.tv_bottom_like, countChar(item.getLikess(), ',') + "");
            if (item.getLikess().contains(LattePreference.getAppUserInfo().getObjectId() + ",")) {
                shineButton.setChecked(true, false);
            }

        }


        helper.getView(R.id.cardview).setTag(R.id.view_tag, item);
        helper.getView(R.id.cardview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipcData item = (RecipcData) v.getTag(R.id.view_tag);
                Intent intent = new Intent(v.getContext(), RecipcDetailActivity.class);
                intent.putExtra("data", item);
                v.getContext().startActivity(intent);
            }
        });

        shineButton.setTag(R.id.view_tag, item);
        shineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipcData item = (RecipcData) v.getTag(R.id.view_tag);
                ShineButton shineButton = (ShineButton) v;
                String likes = item.getLikess();
                if (TextUtils.isEmpty(likes)) {
                    likes = "";
                }
                if (likes.contains(LattePreference.getAppUserInfo().getObjectId() + ",")) {
                    likes = likes.replace(LattePreference.getAppUserInfo().getObjectId() + ",", "");
                } else {
                    likes += LattePreference.getAppUserInfo().getObjectId() + ",";
                }

                item.setLikess(likes);
                String finalLikes = likes;
                item.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            item.setLikess(finalLikes);
                            notifyDataSetChanged();
                            v.getContext().startActivity(new Intent(v.getContext(), FinishActivity.class));
                            if (upDataListener != null) {
                                upDataListener.onUpData();
                            }
                        } else {
                            shineButton.setChecked(!shineButton.isChecked(), false);
                            Toast.makeText(v.getContext(), "Like failed", Toast.LENGTH_LONG).show();
                        }
                    }

                });

            }
        });


    }


          private int countChar(String str, char ch) {
          char[] chs = str.toCharArray();
          int count = 0;
        for (int i = 0; i < chs.length; i++) {
            if (chs[i] == ch) {
                count++;
            }
        }
        return count;
    }

    private HintPopupWindow hintPopupWindow = null;
    private RecipcData recipcData;

    private void showListDialog(View v, RecipcData recipcData1) {
        recipcData = recipcData1;
        if (v.getContext() instanceof Activity) {
            if (hintPopupWindow == null) {
                                  ArrayList<String> strList = new ArrayList<>();
                strList.add("edit");
                strList.add("delete");

                ArrayList<View.OnClickListener> clickList = new ArrayList<>();
                clickList.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), RecipcEditActivity.class);
                        intent.putExtra("data", recipcData);
                        v.getContext().startActivity(intent);
                        hintPopupWindow.dismissPopupWindow();
                    }
                });
                clickList.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showNormalDialog(v.getContext(), recipcData);
                        hintPopupWindow.dismissPopupWindow();
                    }
                });

                                  hintPopupWindow = new HintPopupWindow((Activity) v.getContext(), strList, clickList);

            }
                          hintPopupWindow.showPopupWindow(v);
        }


    }

    private void showNormalDialog(Context context, RecipcData recipcData1) {
        recipcData = recipcData1;
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setTitle("Tips");
        normalDialog.setMessage("Are you sure you want to delete?");
        normalDialog.setPositiveButton("confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                                  recipcData.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    remove(recipcData);
                                    context.startActivity(new Intent(context, FinishActivity.class));
                                } else {
                                    Toast.makeText(context, "Deletion failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
        normalDialog.setNegativeButton("close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                              }
                });
                  normalDialog.show();
    }


          public interface UpDataListener {
        void onUpData();
    }
}