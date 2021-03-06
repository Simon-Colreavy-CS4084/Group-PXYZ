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
import com.example.recipes.data.Comment;
import com.example.recipes.data.NewsFeedData;
import com.example.recipes.ui.chat.MyChatActivity;
import com.example.recipes.ui.newsfeed.NewsFeedDetailsActivity;
import com.example.recipes.ui.newsfeed.NewsFeedEditActivity;
import com.example.recipes.ui.recipc.RecipcEditActivity;
import com.example.recipes.util.DataDispose;
import com.example.recipes.util.LattePreference;
import com.example.recipes.view.HintPopupWindow;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

  public class NewsFeedAdapter extends BaseQuickAdapter<NewsFeedData, BaseViewHolder> {
    private List<Comment> commentList = new ArrayList<>();

    public void setCommentList(List<Comment> commentList) {
        if (commentList == null) {
            commentList = new ArrayList<>();
        }
        this.commentList = commentList;
    }

       
    public NewsFeedAdapter(List<NewsFeedData> list) {
        super(R.layout.item_news_feed, list);
    }

       
    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull NewsFeedData item) {
        int commentNum = 0;
        for (Comment comment : commentList) {
            if (comment.getNewsFeed() != null && comment.getNewsFeed().getObjectId().equals(item.getObjectId())) {
                commentNum++;
            }
        }
        helper.setText(R.id.tv_bottom_comment, commentNum + "");

        helper.setText(R.id.tv_show, item.getContent());
        helper.setText(R.id.tv_top, item.getAuthor().showName());
        ImageView iv_top = helper.getView(R.id.iv_top);
        ImageView iv_more = helper.getView(R.id.iv_more);
         

        helper.getView(R.id.iv_more).setTag(R.id.view_tag, item);
        helper.getView(R.id.iv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsFeedData item = (NewsFeedData) v.getTag(R.id.view_tag);
                showListDialog(v, item);
            }
        });

        String image = "http:          if (item.getAuthor().getImage() != null && !TextUtils.isEmpty(item.getAuthor().getImage().getUrl())) {
            image = item.getAuthor().getImage().getUrl();
        }
        Glide.with(iv_top).load(image)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(iv_top);

        String hint = "@" + item.getAuthor().getUser_no() + " ?? " + DataDispose.getIntervalTime(item.getCreatedAt());
        helper.setText(R.id.tv_top_hint, hint);

        helper.setText(R.id.tv_bottom_like, "");
        ShineButton shineButton = (ShineButton) helper.getView(R.id.sb_bottom_like);
        shineButton.setChecked(false, false);
        if (!TextUtils.isEmpty(item.getLikess())) {

            helper.setText(R.id.tv_bottom_like, countChar(item.getLikess(), ',') + "");
            if (item.getLikess().contains(LattePreference.getAppUserInfo().getObjectId() + ",")) {
                shineButton.setChecked(true, false);
            }

        }

        helper.getView(R.id.ll_bottom_comment).setTag(R.id.view_tag, item);
        helper.getView(R.id.ll_bottom_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsFeedData item = (NewsFeedData) v.getTag(R.id.view_tag);
                Intent intent = new Intent(v.getContext(), NewsFeedDetailsActivity.class);
                intent.putExtra("data", item);
                v.getContext().startActivity(intent);
            }
        });

        helper.getView(R.id.cardview).setTag(R.id.view_tag, item);
        helper.getView(R.id.cardview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsFeedData item = (NewsFeedData) v.getTag(R.id.view_tag);
                Intent intent = new Intent(v.getContext(), NewsFeedDetailsActivity.class);
                intent.putExtra("data", item);
                v.getContext().startActivity(intent);
            }
        });

        shineButton.setTag(R.id.view_tag, item);
        shineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsFeedData item = (NewsFeedData) v.getTag(R.id.view_tag);
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
                        } else {
                            Log.i("bmob", "?????????" + e.getMessage());
                        }
                    }

                });

            }
        });

        setBannerData(helper, item);

    }

          private void setBannerData(BaseViewHolder helper, NewsFeedData newsFeedData) {
        BGABanner mContentBanner = helper.getView(R.id.banner);
        BannerFunAdapter adapter = new BannerFunAdapter(mContentBanner.getContext(), newsFeedData.getImgs());
        mContentBanner.setAdapter(adapter);

        mContentBanner.setData(R.layout.item_banner_image_voide_fun, newsFeedData.getImgs(), null);
        if (newsFeedData.getImgs() == null || newsFeedData.getImgs().size() == 0) {
            mContentBanner.setVisibility(View.GONE);
        } else {
            mContentBanner.setVisibility(View.VISIBLE);
        }
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
    private NewsFeedData newsFeedData;

    private void showListDialog(View v, NewsFeedData newsFeedData1) {
        newsFeedData = newsFeedData1;
        if (v.getContext() instanceof Activity) {
            if (hintPopupWindow == null) {
                                  ArrayList<String> strList = new ArrayList<>();
                strList.add("edit");
                strList.add("delete");

                ArrayList<View.OnClickListener> clickList = new ArrayList<>();
                clickList.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), NewsFeedEditActivity.class);
                        intent.putExtra("data", newsFeedData);
                        v.getContext().startActivity(intent);
                        hintPopupWindow.dismissPopupWindow();
                    }
                });
                clickList.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showNormalDialog(v.getContext(), newsFeedData);
                        hintPopupWindow.dismissPopupWindow();
                    }
                });

                                  hintPopupWindow = new HintPopupWindow((Activity) v.getContext(), strList, clickList);

            }
                          hintPopupWindow.showPopupWindow(v);
        }
    }

    private void showNormalDialog(Context context, NewsFeedData newsFeedData1) {
        newsFeedData = newsFeedData1;
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setTitle("Tips");
        normalDialog.setMessage("Are you sure you want to delete?");
        normalDialog.setPositiveButton("confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                                  newsFeedData.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    remove(newsFeedData);
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
}