package com.example.recipes.fragment.user;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipes.R;
import com.example.recipes.adapter.NewsFeedAdapter;
import com.example.recipes.data.NewsFeedData;
import com.example.recipes.data.user_info;
import com.example.recipes.ui.newsfeed.NewsFeedEditActivity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

  public class FragmentUser1 extends Fragment {
    private View allView;
    private RecyclerView recyclerView;
    private NewsFeedAdapter newsFeedAdapter;
    private String userId;

    public static FragmentUser1 newInstance(String userId) {
        FragmentUser1 fragment = new FragmentUser1();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        userId = getArguments().getString("userId");
        allView = inflater.inflate(R.layout.fragment_user1, container, false);

        allView.findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                  startActivity(new Intent(getContext(), NewsFeedEditActivity.class));
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
        user_info userInfo = new user_info();
        userInfo.setObjectId(userId);

        BmobQuery<NewsFeedData> query = new BmobQuery<NewsFeedData>();
        query.addWhereEqualTo("author", userInfo);
        query.include("author");
        query.order("-createdAt");
        query.findObjects(new FindListener<NewsFeedData>() {
            @Override
            public void done(List<NewsFeedData> object, BmobException e) {
                newsFeedAdapter.setNewInstance(object);
                if (object == null || object.size() == 0) {
                    allView.findViewById(R.id.ll_error).setVisibility(View.VISIBLE);
                } else {
                    allView.findViewById(R.id.ll_error).setVisibility(View.GONE);
                }
            }
        });
    }

}
