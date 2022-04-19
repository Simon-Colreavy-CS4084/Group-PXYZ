package com.example.recipes.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.recipes.R;
import com.example.recipes.adapter.FollowListAdapter;
import com.example.recipes.adapter.MsgAdapter;
import com.example.recipes.data.FollowData;
import com.example.recipes.data.user_info;
import com.example.recipes.util.LattePreference;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

  public class FollowActivity extends AppCompatActivity {
    private user_info userInfo;      private List<FollowData> meFollowDatas = new ArrayList<>();      private FollowListAdapter adapter;
    private int type = 0;  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_follow);

        ImmersionBar.with(this)
                .titleBarMarginTop(findViewById(R.id.title))
                .statusBarDarkFont(true, 0.2f)                   .keyboardEnable(true)                  .navigationBarWithKitkatEnable(false)                  .init();

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        userInfo = (user_info) getIntent().getSerializableExtra("user");
        type = getIntent().getIntExtra("type", 0);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        if (type == 0) {
            tv_title.setText("follow");
        } else {
            tv_title.setText("following");
        }


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                  LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FollowListAdapter(null,type);
        recyclerView.setAdapter(adapter);

    }

          @Override
    public void onResume() {
        super.onResume();
        getNewsFeedData();
    }


          public void getNewsFeedData() {
        BmobQuery<FollowData> query = new BmobQuery<FollowData>();
                  query.include("meUser,followUser");
        query.findObjects(new FindListener<FollowData>() {
            @Override
            public void done(List<FollowData> object, BmobException e) {
                meFollowDatas = new ArrayList<>();  
                List<FollowData> followDatas = new ArrayList<>();                  if (e == null) {
                                          if (object != null && object.size() > 0) {
                        for (FollowData followData : object) {
                            if (followData.getMeUser().getObjectId().equals(userInfo.getObjectId()) && type == 0) {
                                followDatas.add(followData);
                            } else if (followData.getFollowUser().getObjectId().equals(userInfo.getObjectId()) && type == 1) {
                                followDatas.add(followData);
                            }

                            if (followData.getMeUser().getObjectId().equals(LattePreference.getAppUserInfo().getObjectId())) {
                                meFollowDatas.add(followData);
                            }
                        }
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }

                adapter.setNewInstance(followDatas);
                adapter.setMeFollowData(meFollowDatas);
                if (followDatas == null || followDatas.size() == 0) {
                    findViewById(R.id.ll_error).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.ll_error).setVisibility(View.GONE);
                }

            }
        });
    }
}