package com.example.recipes.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipes.R;
import com.example.recipes.adapter.MeViewPagerAdapter;
import com.example.recipes.data.FollowData;
import com.example.recipes.data.user_info;
import com.example.recipes.ui.EditProfileActivity;
import com.example.recipes.ui.FollowActivity;
import com.example.recipes.util.LattePreference;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

  public class Fragment3 extends Fragment {
    private View allView;
    private LinearLayout head_layout;
    private TabLayout toolbar_tab;
    private ViewPager main_vp_container;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private CoordinatorLayout root_layout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        allView = inflater.inflate(R.layout.fragment3, container, false);
        initUserInfo();
        AppBarLayout app_bar_layout = (AppBarLayout) allView.findViewById(R.id.app_bar_layout);
        Toolbar mToolbar = (Toolbar) allView.findViewById(R.id.toolbar);
                        head_layout = (LinearLayout) allView.findViewById(R.id.head_layout);
        root_layout = (CoordinatorLayout) allView.findViewById(R.id.root_layout);
                  mCollapsingToolbarLayout = (CollapsingToolbarLayout) allView.findViewById(R.id
                .collapsing_toolbar_layout);
        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -head_layout.getHeight() / 2) {
                    mCollapsingToolbarLayout.setTitle(LattePreference.getAppUserInfo().getUser_no());
                } else {
                    mCollapsingToolbarLayout.setTitle(" ");
                }
            }
        });
        toolbar_tab = (TabLayout) allView.findViewById(R.id.toolbar_tab);
        main_vp_container = (ViewPager) allView.findViewById(R.id.main_vp_container);
        main_vp_container.setOffscreenPageLimit(3);

        MeViewPagerAdapter vpAdapter = new MeViewPagerAdapter(getActivity().getSupportFragmentManager(), getContext(),
                LattePreference.getAppUserInfo().getObjectId());
        main_vp_container.setAdapter(vpAdapter);
        main_vp_container.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener
                (toolbar_tab));
        toolbar_tab.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener
                (main_vp_container));

                      
    
        return allView;
    }

          @Override
    public void onResume() {
        super.onResume();
        initUserInfo();
        getNewsFeedData();
    }


    private void initUserInfo() {
        ((TextView) allView.findViewById(R.id.tv_userName)).setText(LattePreference.getAppUserInfo().showName());
        ((TextView) allView.findViewById(R.id.tv_userId)).setText( LattePreference.getAppUserInfo().getUser_no());

        ImageView iv_head = allView.findViewById(R.id.iv_head);
        String image = "http:          if (LattePreference.getAppUserInfo().getImage() != null && !TextUtils.isEmpty(LattePreference.getAppUserInfo().getImage().getUrl())) {
            image = LattePreference.getAppUserInfo().getImage().getUrl();
        }
        Glide.with(iv_head).load(image)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(iv_head);


        allView.findViewById(R.id.iv_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                  startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });

        allView.findViewById(R.id.ll_follow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FollowActivity.class);
                intent.putExtra("user", LattePreference.getAppUserInfo());
                intent.putExtra("type", 0);
                v.getContext().startActivity(intent);
            }
        });
        allView.findViewById(R.id.ll_following).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FollowActivity.class);
                intent.putExtra("user", LattePreference.getAppUserInfo());
                intent.putExtra("type", 1);
                v.getContext().startActivity(intent);
            }
        });
    }

          private void getNewsFeedData() {
        user_info userInfo = new user_info();
        userInfo.setObjectId(LattePreference.getAppUserInfo().getObjectId());
                            BmobQuery<FollowData> eq1 = new BmobQuery<FollowData>();
        eq1.addWhereEqualTo("meUser", userInfo);
                  BmobQuery<FollowData> eq2 = new BmobQuery<FollowData>();
        eq2.addWhereEqualTo("followUser", userInfo);

                  List<BmobQuery<FollowData>> queries1 = new ArrayList<BmobQuery<FollowData>>();
        queries1.add(eq1);
        queries1.add(eq2);
        BmobQuery<FollowData> query = new BmobQuery<FollowData>().or(queries1);
                  query.include("meUser,followUser");
        query.findObjects(new FindListener<FollowData>() {
            @Override
            public void done(List<FollowData> object, BmobException e) {
                if (e == null) {
                                          int follow = 0;
                    int following = 0;
                    if (object != null && object.size() > 0) {
                        for (FollowData followData : object) {
                            if (followData.getMeUser().getObjectId().equals(LattePreference.getAppUserInfo().getObjectId())) {
                                follow++;
                            } else if (followData.getFollowUser().getObjectId().equals(LattePreference.getAppUserInfo().getObjectId())) {
                                following++;
                            }
                        }
                    }
                    ((TextView) allView.findViewById(R.id.tv_follow)).setText("" + follow);
                    ((TextView) allView.findViewById(R.id.tv_following)).setText("" + following);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    ((TextView) allView.findViewById(R.id.tv_follow)).setText("0");
                    ((TextView) allView.findViewById(R.id.tv_following)).setText("0");
                }
            }
        });
    }
}
