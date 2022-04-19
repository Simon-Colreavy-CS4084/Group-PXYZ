package com.example.recipes.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.recipes.R;
import com.example.recipes.adapter.MeViewPagerAdapter;
import com.example.recipes.data.FollowData;
import com.example.recipes.data.user_info;
import com.example.recipes.ui.chat.MyChatActivity;
import com.example.recipes.util.LattePreference;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserInfoActivity extends AppCompatActivity {
    private LinearLayout head_layout;
    private TabLayout toolbar_tab;
    private ViewPager main_vp_container;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private CoordinatorLayout root_layout;
    private user_info userInfo;
    private FollowData followData = null;  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        ImmersionBar.with(this)
                .titleBarMarginTop(findViewById(R.id.toolbar))
                .statusBarDarkFont(true, 0.2f)                   .keyboardEnable(true)                  .navigationBarWithKitkatEnable(false)                  .init();

        userInfo = (user_info) getIntent().getSerializableExtra("user");
        initUserInfo();
        AppBarLayout app_bar_layout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        head_layout = (LinearLayout) findViewById(R.id.head_layout);
        root_layout = (CoordinatorLayout) findViewById(R.id.root_layout);
                  mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id
                .collapsing_toolbar_layout);
        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -head_layout.getHeight() / 2) {
                    mCollapsingToolbarLayout.setTitle(userInfo.getUser_no());
                } else {
                    mCollapsingToolbarLayout.setTitle(" ");
                }
            }
        });
        toolbar_tab = (TabLayout) findViewById(R.id.toolbar_tab);
        main_vp_container = (ViewPager) findViewById(R.id.main_vp_container);
        main_vp_container.setOffscreenPageLimit(3);

        MeViewPagerAdapter vpAdapter = new MeViewPagerAdapter(getSupportFragmentManager(), this,
                userInfo.getObjectId());
        main_vp_container.setAdapter(vpAdapter);
        main_vp_container.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener
                (toolbar_tab));
        toolbar_tab.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener
                (main_vp_container));


    }

          @Override
    public void onResume() {
        super.onResume();
        initUserInfo();
        getNewsFeedData();
    }


    private void initUserInfo() {
        ((TextView) findViewById(R.id.tv_userName)).setText(userInfo.showName());
        ((TextView) findViewById(R.id.tv_userId)).setText(userInfo.getUser_no());

        ImageView iv_head = findViewById(R.id.iv_head);
        String image = "http:          if (userInfo.getImage() != null && !TextUtils.isEmpty(userInfo.getImage().getUrl())) {
            image = userInfo.getImage().getUrl();
        }
        Glide.with(iv_head).load(image)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(iv_head);

        findViewById(R.id.btn_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfo.getObjectId().equals(LattePreference.getAppUserInfo().getObjectId())) {
                    Toast.makeText(UserInfoActivity.this, "You can't send messages to yourself", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(v.getContext(), MyChatActivity.class);
                intent.putExtra("userData", userInfo);
                intent.putExtra("otherId", userInfo.getObjectId());
                intent.putExtra("name", userInfo.showName());
                v.getContext().startActivity(intent);
            }
        });

        findViewById(R.id.ll_follow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FollowActivity.class);
                intent.putExtra("user", userInfo);
                intent.putExtra("type", 0);
                v.getContext().startActivity(intent);
            }
        });
        findViewById(R.id.ll_following).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FollowActivity.class);
                intent.putExtra("user", userInfo);
                intent.putExtra("type", 1);
                v.getContext().startActivity(intent);
            }
        });

    }

          private void getNewsFeedData() {
        user_info userInfo1 = new user_info();
        userInfo1.setObjectId(userInfo.getObjectId());
                            BmobQuery<FollowData> eq1 = new BmobQuery<FollowData>();
        eq1.addWhereEqualTo("meUser", userInfo1);
                  BmobQuery<FollowData> eq2 = new BmobQuery<FollowData>();
        eq2.addWhereEqualTo("followUser", userInfo1);

                  List<BmobQuery<FollowData>> queries1 = new ArrayList<BmobQuery<FollowData>>();
        queries1.add(eq1);
        queries1.add(eq2);
          BmobQuery<FollowData> query = new BmobQuery<FollowData>();
                  query.include("meUser,followUser");
        query.findObjects(new FindListener<FollowData>() {
            @Override
            public void done(List<FollowData> object, BmobException e) {
                followData = null;                  if (e == null) {
                                          int follow = 0;
                    int following = 0;
                    if (object != null && object.size() > 0) {
                        for (FollowData followData : object) {
                            if (followData.getMeUser().getObjectId().equals(userInfo1.getObjectId())) {
                                follow++;
                            } else if (followData.getFollowUser().getObjectId().equals(userInfo1.getObjectId())) {
                                following++;
                            }

                            if (followData.getMeUser().getObjectId().equals(LattePreference.getAppUserInfo().getObjectId()) &&
                                    followData.getFollowUser().getObjectId().equals(userInfo1.getObjectId())) {
                                UserInfoActivity.this.followData = followData;
                            }
                        }
                    }
                    ((TextView) findViewById(R.id.tv_follow)).setText("" + follow);
                    ((TextView) findViewById(R.id.tv_following)).setText("" + following);

                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    ((TextView) findViewById(R.id.tv_follow)).setText("0");
                    ((TextView) findViewById(R.id.tv_following)).setText("0");
                }
                initFollowBtn();
            }
        });
    }

    private void initFollowBtn() {
        if (followData != null) {
            ((TextView) findViewById(R.id.btn_follow)).setText("Cancel Follow");
        } else {
            ((TextView) findViewById(R.id.btn_follow)).setText("follow");
        }
        findViewById(R.id.btn_follow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfo.getObjectId().equals(LattePreference.getAppUserInfo().getObjectId())) {
                    Toast.makeText(UserInfoActivity.this, "You cannot operate on yourself", Toast.LENGTH_LONG).show();
                    return;
                }
                if (followData != null) {
                    followData.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                getNewsFeedData();
                            } else {
                                Toast.makeText(UserInfoActivity.this, "Cancel Follow Fail", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    FollowData followData1 = new FollowData();
                    followData1.setMeUser(LattePreference.getAppUserInfo());
                    followData1.setFollowUser(userInfo);
                    followData1.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                                                  Toast.makeText(UserInfoActivity.this, "Follow successful", Toast.LENGTH_LONG).show();
                                getNewsFeedData();
                            } else {
                                                                  Log.e("asd", e.getMessage());
                                Toast.makeText(UserInfoActivity.this, "Follow Fail", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}