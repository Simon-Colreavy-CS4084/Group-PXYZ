package com.example.recipes.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipes.R;
import com.example.recipes.adapter.NewsFeedAdapter;
import com.example.recipes.data.Comment;
import com.example.recipes.data.FollowData;
import com.example.recipes.data.NewsFeedData;
import com.example.recipes.data.user_info;
import com.example.recipes.ui.newsfeed.NewsFeedEditActivity;
import com.example.recipes.ui.chat.MyChatListActivity;
import com.example.recipes.ui.newsfeed.NewsFeedSearchActivity;
import com.example.recipes.util.LattePreference;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

  public class Fragment1 extends Fragment {
    private View allView;
    private RecyclerView recyclerView;
    private NewsFeedAdapter newsFeedAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        allView = inflater.inflate(R.layout.fragment1, container, false);

        allView.findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                  startActivity(new Intent(getContext(), NewsFeedEditActivity.class));
            }
        });
        allView.findViewById(R.id.iv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                  startActivity(new Intent(getContext(), NewsFeedSearchActivity.class));
            }
        });
        allView.findViewById(R.id.iv_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                  startActivity(new Intent(getContext(), MyChatListActivity.class));

            }
        });


        recyclerView = allView.findViewById(R.id.rvShow);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsFeedAdapter = new NewsFeedAdapter(null);
        recyclerView.setAdapter(newsFeedAdapter);
        return allView;
    }

          @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        BmobQuery<Comment> query1 = new BmobQuery<Comment>();
                  query1.include("newsFeed,author");
        query1.order("-createdAt");
        query1.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> object, BmobException e) {
                if (e == null) {
                    getNewsFeedData(object);
                } else {
                    Log.i("bmob", "failure：" + e.getMessage() + "," + e.getErrorCode());
                    allView.findViewById(R.id.ll_error).setVisibility(View.VISIBLE);
                }
            }
        });

    }


    private void getNewsFeedData(List<Comment> commentList) {
        BmobQuery<FollowData> query = new BmobQuery<FollowData>();
                  query.include("meUser,followUser");
        query.findObjects(new FindListener<FollowData>() {
            @Override
            public void done(List<FollowData> object, BmobException e) {

                List<user_info> userInfos = new ArrayList<>();                  user_info userInfo0 = new user_info();
                userInfo0.setObjectId(LattePreference.getAppUserInfo().getObjectId());
                userInfos.add(userInfo0);
                if (e == null) {
                                          if (object != null && object.size() > 0) {
                        for (FollowData followData : object) {
                            if (followData.getMeUser().getObjectId().equals(LattePreference.getAppUserInfo().getObjectId())) {
                                userInfos.add(followData.getFollowUser());
                            } else if (followData.getFollowUser().getObjectId().equals(LattePreference.getAppUserInfo().getObjectId())) {
                                userInfos.add(followData.getMeUser());
                            }
                        }
                    }
                    if (userInfos.size() == 0) {
                        allView.findViewById(R.id.ll_error).setVisibility(View.VISIBLE);
                    } else {
                                                  List<BmobQuery<NewsFeedData>> queries1 = new ArrayList<BmobQuery<NewsFeedData>>();
                        for (user_info userInfo : userInfos) {
                            user_info userInfo1 = new user_info();
                            userInfo1.setObjectId(userInfo.getObjectId());

                            BmobQuery<NewsFeedData> eq = new BmobQuery<NewsFeedData>();
                            eq.addWhereEqualTo("author", userInfo1);
                            queries1.add(eq);
                        }
                        BmobQuery<NewsFeedData> query = new BmobQuery<NewsFeedData>().or(queries1);
                        query.include("author");
                        query.order("-createdAt");
                        query.findObjects(new FindListener<NewsFeedData>() {
                            @Override
                            public void done(List<NewsFeedData> object, BmobException e) {
                                newsFeedAdapter.setCommentList(commentList);
                                newsFeedAdapter.setNewInstance(object);
                                if (object == null || object.size() == 0) {
                                    allView.findViewById(R.id.ll_error).setVisibility(View.VISIBLE);
                                } else {
                                    allView.findViewById(R.id.ll_error).setVisibility(View.GONE);
                                }
                            }
                        });
                    }

                } else {
                    Log.i("bmob", "failure：" + e.getMessage() + "," + e.getErrorCode());
                    allView.findViewById(R.id.ll_error).setVisibility(View.VISIBLE);
                }

            }
        });
    }

}
