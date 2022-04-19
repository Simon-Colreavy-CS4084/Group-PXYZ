package com.example.recipes.ui.newsfeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipes.R;
import com.example.recipes.adapter.BannerFunAdapter;
import com.example.recipes.adapter.CommentAdapter;
import com.example.recipes.data.Comment;
import com.example.recipes.data.NewsFeedData;
import com.example.recipes.data.user_info;
import com.example.recipes.ui.UserInfoActivity;
import com.example.recipes.util.DataDispose;
import com.example.recipes.util.LattePreference;
import com.example.recipes.util.Utils;
import com.gyf.immersionbar.ImmersionBar;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

  public class NewsFeedDetailsActivity extends AppCompatActivity {
    private NewsFeedData newsFeedData;
    private CommentAdapter commentAdapter;
    private TextView tvComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_feed_details);

        ImmersionBar.with(this)
                .titleBarMarginTop(findViewById(R.id.title))
                .statusBarDarkFont(true, 0.2f)                   .keyboardEnable(true)                  .navigationBarWithKitkatEnable(false)                  .init();

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        newsFeedData = (NewsFeedData) getIntent().getSerializableExtra("data");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(null);
        recyclerView.setAdapter(commentAdapter);
        commentAdapter.addHeaderView(getHeaderView());

        EditText inputText = (EditText) findViewById(R.id.input_text);
        Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(v);
                String content = inputText.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    user_info userInfo2 = LattePreference.getAppUserInfo();
                    final Comment comment = new Comment();
                    comment.setContent(content);
                    comment.setAuthor(userInfo2);
                    comment.setNewsFeed(newsFeedData);
                    comment.save(new SaveListener<String>() {

                        @Override
                        public void done(String objectId, BmobException e) {
                            if (e == null) {
                                commentAdapter.addData(0, comment);
                                                                                                    recyclerView.scrollToPosition(1);
                                                                  inputText.setText("");
                                tvComment.setText(""+commentAdapter.getData().size());
                            } else {
                                Toast.makeText(NewsFeedDetailsActivity.this, "Failure：" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                    });
                }

            }
        });

        getData();
    }

    private View getHeaderView() {
        View headerView = LayoutInflater.from(this).inflate(R.layout.layout_news_feed_details, null);

        headerView.findViewById(R.id.rl_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserInfoActivity.class);
                intent.putExtra("user", newsFeedData.getAuthor());
                v.getContext().startActivity(intent);
            }
        });

        ((TextView) headerView.findViewById(R.id.tv_show)).setText(newsFeedData.getContent());
        ((TextView) headerView.findViewById(R.id.tv_top)).setText(newsFeedData.getAuthor().showName());
        ImageView iv_top = headerView.findViewById(R.id.iv_top);


        String image = "http://";
        if (newsFeedData.getAuthor().getImage() != null && !TextUtils.isEmpty(newsFeedData.getAuthor().getImage().getUrl())) {
            image = newsFeedData.getAuthor().getImage().getUrl();
        }
        Glide.with(iv_top).load(image)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(iv_top);

        String hint = "@" + newsFeedData.getAuthor().getUser_no() + " · " + DataDispose.getIntervalTime(newsFeedData.getCreatedAt());
        ((TextView) headerView.findViewById(R.id.tv_top_hint)).setText(hint);

        ((TextView) headerView.findViewById(R.id.tv_bottom_like)).setText("");

        tvComment =  ((TextView) headerView.findViewById(R.id.tv_bottom_comment));
        ShineButton shineButton = (ShineButton) headerView.findViewById(R.id.sb_bottom_like);
        shineButton.setChecked(false, false);
        if (!TextUtils.isEmpty(newsFeedData.getLikess())) {
            ((TextView) headerView.findViewById(R.id.tv_bottom_like)).setText(countChar(newsFeedData.getLikess(), ',') + "");
            if (newsFeedData.getLikess().contains(LattePreference.getAppUserInfo().getObjectId() + ",")) {
                shineButton.setChecked(true, false);
            }

        }


        shineButton.setTag(R.id.view_tag, newsFeedData);
        shineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShineButton shineButton = (ShineButton) v;
                String likes = newsFeedData.getLikess();
                if (TextUtils.isEmpty(likes)) {
                    likes = "";
                }
                if (likes.contains(LattePreference.getAppUserInfo().getObjectId() + ",")) {
                    likes = likes.replace(LattePreference.getAppUserInfo().getObjectId() + ",", "");
                } else {
                    likes += LattePreference.getAppUserInfo().getObjectId() + ",";
                }

                newsFeedData.setLikess(likes);
                String finalLikes = likes;
                newsFeedData.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            newsFeedData.setLikess(finalLikes);
                            ((TextView) headerView.findViewById(R.id.tv_bottom_like)).setText("");
                            ShineButton shineButton = (ShineButton) headerView.findViewById(R.id.sb_bottom_like);
                            shineButton.setChecked(false, false);
                            if (!TextUtils.isEmpty(newsFeedData.getLikess())) {
                                ((TextView) headerView.findViewById(R.id.tv_bottom_like)).setText(countChar(newsFeedData.getLikess(), ',') + "");
                                if (newsFeedData.getLikess().contains(LattePreference.getAppUserInfo().getObjectId() + ",")) {
                                    shineButton.setChecked(true, false);
                                }

                            }
                        } else {
                            Log.i("bmob", "failure：" + e.getMessage());
                        }
                    }

                });

            }
        });

        BGABanner mContentBanner = headerView.findViewById(R.id.banner);
        setBannerData(mContentBanner);

        return headerView;

    }


          private void setBannerData(BGABanner mContentBanner) {

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


    private void getData() {
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("newsFeed", newsFeedData);
                  query.include("newsFeed,author");
        query.order("-createdAt");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> object, BmobException e) {
                if (e == null) {
                                          commentAdapter.setNewInstance(object);
                    if (object==null || object.size()==0){
                        tvComment.setText("");
                    }else{
                        tvComment.setText(""+object.size());
                    }

                } else {
                    Log.i("bmob", "failure：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }


}