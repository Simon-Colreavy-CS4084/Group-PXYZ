package com.example.recipes.ui.recipc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipes.R;
import com.example.recipes.adapter.CommentAdapter;
import com.example.recipes.adapter.base.CommonAdapter;
import com.example.recipes.adapter.base.ViewHolder;
import com.example.recipes.data.Comment;
import com.example.recipes.data.FoodsData;
import com.example.recipes.data.RecipcData;
import com.example.recipes.data.StepData;
import com.example.recipes.data.user_info;
import com.example.recipes.ui.FinishActivity;
import com.example.recipes.ui.UserInfoActivity;
import com.example.recipes.ui.newsfeed.NewsFeedDetailsActivity;
import com.example.recipes.util.DataDispose;
import com.example.recipes.util.FileUtils;
import com.example.recipes.util.GlideEngine;
import com.example.recipes.util.LattePreference;
import com.example.recipes.util.Utils;
import com.example.recipes.view.FlowsLayout;
import com.example.recipes.view.HeadZoomScrollView;
import com.example.recipes.view.waterwave_progress.WaterWaveProgress;
import com.gyf.immersionbar.ImmersionBar;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cc.shinichi.library.ImagePreview;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.jzvd.JzvdStd;

  public class RecipcDetailActivity extends AppCompatActivity implements HeadZoomScrollView.OnScrollChangedListener {
    private RecipcData recipcData;

    private HeadZoomScrollView scrollView = null;
    private Toolbar mToolbar = null;
    private AppCompatTextView mTitle = null;

    private float headerHeight;      private float minHeaderHeight;  
    private CommonAdapter<Comment> commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recipc_detail);

        ImmersionBar.with(this)
                .titleBar(findViewById(R.id.toolbar))
                .statusBarDarkFont(true, 0.2f)                   .keyboardEnable(true)                  .navigationBarWithKitkatEnable(false)                  .init();

        recipcData = (RecipcData) getIntent().getSerializableExtra("data");


        initView();
        initMeasure();
        setLayout1();
        setLayout2();
        setLayout3();
        setLayout4();
        setLayout5();
        setSend();
    }


    private void initMeasure() {
        headerHeight = getResources().getDimension(R.dimen.dp_300);
        minHeaderHeight = getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mTitle = findViewById(R.id.tb_title);
        scrollView = findViewById(R.id.scrollView);
        ((HeadZoomScrollView) findViewById(R.id.scrollView)).setOnScrollChangedListener(this);
        ((HeadZoomScrollView) findViewById(R.id.scrollView)).setZoomView(findViewById(R.id.layout1_iv_show));
                  alterWhiteToolbar();

    }

    public void alterWhiteToolbar() {
        if (mToolbar != null && mTitle != null) {
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            mTitle.setTextColor(Color.WHITE);
            mTitle.setText("");
            mToolbar.setBackgroundResource(R.color.c_00ffffff);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }


    @Override
    public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
        if (mToolbar == null) {
            return;
        }
                  float scrollY = who.getScrollY();

                  float headerBarOffsetY = headerHeight - minHeaderHeight;          float offset = 1 - Math.max((headerBarOffsetY - scrollY) / headerBarOffsetY, 0f);

                  mToolbar.setBackgroundColor(Color.argb((int) (offset * 255), 255, 255, 255));
        if ((int) (offset * 255) <= 155) {
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            mTitle.setTextColor(Color.WHITE);
            mTitle.setText("");
        } else {
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            mTitle.setTextColor(Color.BLACK);
            if ((int) (offset * 255) >= 255) {
                mTitle.setText(recipcData.getTitle());
            } else {
                mTitle.setText("");
            }
        }
        }

          private void setLayout1() {
        ((TextView) findViewById(R.id.layout1_tv_title)).setText(recipcData.getTitle());
        ((TextView) findViewById(R.id.layout1_tv_content)).setText(recipcData.getContent());

        ImageView layout1_iv_voide = findViewById(R.id.layout1_iv_voide);
        String images = "http://";
        if (!TextUtils.isEmpty(recipcData.getImg())) {
            images = recipcData.getImg();
        }
        if (FileUtils.getFileType(images) == 1) {
            layout1_iv_voide.setVisibility(View.GONE);
        } else {
            layout1_iv_voide.setVisibility(View.VISIBLE);
        }
        ImageView layout1_iv_show = findViewById(R.id.layout1_iv_show);
        Glide.with(layout1_iv_show).load(images)
                .into(layout1_iv_show);

        ImageView layout1_iv_top = findViewById(R.id.layout1_iv_top);
        String image = "http://";
        if (recipcData.getAuthor().getImage() != null && !TextUtils.isEmpty(recipcData.getAuthor().getImage().getUrl())) {
            image = recipcData.getAuthor().getImage().getUrl();
        }
        Glide.with(layout1_iv_top).load(image)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(layout1_iv_top);
        ((TextView) findViewById(R.id.layout1_tv_top)).setText(recipcData.getAuthor().showName());
        String hint = "@" + recipcData.getAuthor().getUser_no() + " · " + DataDispose.getIntervalTime(recipcData.getCreatedAt());
        ((TextView) findViewById(R.id.layout1_tv_top_hint)).setText(hint);


        ((TextView) findViewById(R.id.layout1_tv_like)).setText("");
        ShineButton shineButton = (ShineButton) findViewById(R.id.layout1_sb_like);
        shineButton.setChecked(false, false);
        if (!TextUtils.isEmpty(recipcData.getLikess())) {
            ((TextView) findViewById(R.id.layout1_tv_like)).setText(countChar(recipcData.getLikess(), ',') + "");
            if (recipcData.getLikess().contains(LattePreference.getAppUserInfo().getObjectId() + ",")) {
                shineButton.setChecked(true, false);
            }
        }

        shineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String likes = recipcData.getLikess();
                if (TextUtils.isEmpty(likes)) {
                    likes = "";
                }
                if (likes.contains(LattePreference.getAppUserInfo().getObjectId() + ",")) {
                    likes = likes.replace(LattePreference.getAppUserInfo().getObjectId() + ",", "");
                } else {
                    likes += LattePreference.getAppUserInfo().getObjectId() + ",";
                }

                recipcData.setLikess(likes);
                String finalLikes = likes;
                recipcData.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            recipcData.setLikess(finalLikes);
                            setLayout1();
                        } else {
                            shineButton.setChecked(!shineButton.isChecked(), false);
                            Toast.makeText(v.getContext(), "Like failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        layout1_iv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = recipcData.getImg();
                if (FileUtils.getFileType(path) == 2) {
                    JzvdStd.startFullscreenDirectly(v.getContext(), JzvdStd.class, path, "");
                    return;
                }
                ImagePreview
                        .getInstance()
                        .setContext(v.getContext())
                        .setIndex(0)
                        .setEnableDragClose(true)
                        .setShowDownButton(false)
                        .setImage(path)
                        .start();
            }
        });

        findViewById(R.id.layout1_rl_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserInfoActivity.class);
                intent.putExtra("user", recipcData.getAuthor());
                v.getContext().startActivity(intent);
            }
        });

    }

          private void setLayout2() {
        WaterWaveProgress layout2_progress1 = findViewById(R.id.layout2_progress1);
        layout2_progress1.setProgressToShowText(Integer.parseInt(recipcData.getCookingTime()));

        WaterWaveProgress layout2_progress2 = findViewById(R.id.layout2_progress2);
        layout2_progress2.setProgressToShowText(Integer.parseInt(recipcData.getPlacementTime()));

        WaterWaveProgress layout2_progress3 = findViewById(R.id.layout2_progress3);
        layout2_progress3.setProgressToShowText(Integer.parseInt(recipcData.getBakingTime()));
    }

          private void setLayout3() {
        final ListView layout3_lv_show = (ListView) findViewById(R.id.layout3_lv_show);
        layout3_lv_show.setAdapter(new CommonAdapter<FoodsData>(this, recipcData.getFoodsDataList(), R.layout.item_recipc_details_foods) {
            @Override
            public void convert(ViewHolder helper, FoodsData item) {
                helper.setText(R.id.tv_1, item.getNum());
                helper.setText(R.id.tv_2, item.getUnit());
                helper.setText(R.id.tv_3, item.getName());
            }
        });

    }

          private void setLayout4() {
        final ListView layout4_lv_show = (ListView) findViewById(R.id.layout4_lv_show);
        layout4_lv_show.setAdapter(new CommonAdapter<StepData>(this, recipcData.getStepDataList(), R.layout.item_recipc_details_step) {
            @Override
            public void convert(ViewHolder helper, StepData item) {
                helper.setText(R.id.tv_title, "Cooking steps  " + (helper.getPosition() + 1) + "/" + getDatas().size());
                helper.setText(R.id.tv_1, item.getDesc());

                ImageView iv_image = helper.getView(R.id.iv_image);
                                        ImageView iv_voide = helper.getView(R.id.iv_voide);
                String path = item.getImage();
                if (TextUtils.isEmpty(path)) {
                    helper.getView(R.id.ll_show).setVisibility(View.GONE);
                } else {
                    helper.getView(R.id.ll_show).setVisibility(View.VISIBLE);

                    if (FileUtils.getFileType(path) == 1) {
                        iv_voide.setVisibility(View.GONE);
                    } else {
                        iv_voide.setVisibility(View.VISIBLE);
                    }

                    GlideEngine.createGlideEngine().loadImage(iv_image.getContext(), path, iv_image);

                    iv_image.setTag(R.id.view_tag, path);
                    iv_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String path = (String) view.getTag(R.id.view_tag);
                            if (FileUtils.getFileType(path) == 2) {
                                JzvdStd.startFullscreenDirectly(view.getContext(), JzvdStd.class, path, "");
                                return;
                            }
                            ImagePreview
                                    .getInstance()
                                    .setContext(view.getContext())
                                    .setIndex(0)
                                    .setEnableDragClose(true)
                                    .setShowDownButton(false)
                                    .setImage(path)
                                    .start();
                        }
                    });
                }


            }
        });

    }


          private void setLayout5() {
        FlowsLayout layout5_flowlayout = findViewById(R.id.layout5_flowlayout);

        LayoutInflater mInflater = LayoutInflater.from(this);

        for (int i = 0; i < recipcData.getLabelList().size(); i++) {
            if (!TextUtils.isEmpty(recipcData.getLabelList().get(i))) {
                View view = mInflater.inflate(
                        R.layout.item_recipc_label_tv, layout5_flowlayout,
                        false);

                TextView tv = view.findViewById(R.id.tv_show);
                tv.setText("#" + recipcData.getLabelList().get(i));
                tv.setTag(R.id.view_tag, recipcData.getLabelList().get(i));
                                  tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String str = (String) v.getTag(R.id.view_tag);
                        insertData(str);
                    }
                });
                layout5_flowlayout.addView(view);
            }
        }
    }

    private void setSend() {
        commentAdapter = new CommonAdapter<Comment>(this, null, R.layout.item_comment) {
            @Override
            public void convert(ViewHolder helper, Comment item) {
                helper.setText(R.id.tv_show, item.getContent());
                helper.setText(R.id.tv_top, item.getAuthor().showName());
                ImageView iv_top = helper.getView(R.id.iv_top);


                String image = "http://";
                if (item.getAuthor().getImage() != null && !TextUtils.isEmpty(item.getAuthor().getImage().getUrl())) {
                    image = item.getAuthor().getImage().getUrl();
                }
                Glide.with(iv_top).load(image)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(iv_top);

                String hint = "@" + item.getAuthor().getUser_no() + " · " + DataDispose.getIntervalTime(item.getCreatedAt());
                helper.setText(R.id.tv_top_hint, hint);


                helper.getView(R.id.rl_top).setTag(R.id.view_tag, item);
                helper.getView(R.id.rl_top).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Comment item = (Comment) v.getTag(R.id.view_tag);
                        Intent intent = new Intent(v.getContext(), UserInfoActivity.class);
                        intent.putExtra("user", item.getAuthor());
                        v.getContext().startActivity(intent);
                    }
                });
            }
        };
        final ListView layout6_lv_show = (ListView) findViewById(R.id.layout6_lv_show);
        layout6_lv_show.setAdapter(commentAdapter);
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
                    comment.setRecipcData(recipcData);
                    comment.save(new SaveListener<String>() {

                        @Override
                        public void done(String objectId, BmobException e) {
                            if (e == null) {
                                commentAdapter.addData(0, comment);

                                                                  inputText.setText("");

                                                                  scrollView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        scrollView.smoothScrollTo(0, findViewById(R.id.layout6_ll_show).getTop());
                                    }
                                });
                            } else {
                                Toast.makeText(RecipcDetailActivity.this, "Failure：" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                    });
                }

            }
        });
        getCommentData();
    }

    private void getCommentData() {
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("recipcData", recipcData);
                  query.include("recipcData,author");
        query.order("-createdAt");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> object, BmobException e) {
                if (e == null) {
                                          commentAdapter.newDatas(object);
                } else {
                    Log.i("bmob", "Failure：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }


    private void insertData(String data) {
        final String KEY_HISTORY = "key_recipc_historys";
        final String LIEN_HISTORY = "&&~!";
                  if (!TextUtils.isEmpty(data)) {

            String set = LattePreference.getAppString(KEY_HISTORY);
            String[] strings = set.split(LIEN_HISTORY);
            List<String> arrayList = Arrays.asList(strings);
            List<String> list = new ArrayList(arrayList);
            boolean hasData = list.contains(data);

            if (!hasData) {
                list.add(data);
            }
            set = "";
            for (String string : list) {
                if (data.equals(string)) {
                    set = string + LIEN_HISTORY + set;
                } else {
                    set += string + LIEN_HISTORY;
                }
            }
            if (TextUtils.isEmpty(set)) {
                set = "";
            }
            LattePreference.setAppString(KEY_HISTORY, set);
        }



        if (!TextUtils.isEmpty(data)) {
            Intent intent = new Intent(RecipcDetailActivity.this, RecipcSearchResultActivity.class);
            intent.putExtra("label", data);
            startActivity(intent);
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

}