package com.example.recipes.fragment.user;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipes.R;
import com.example.recipes.adapter.NewsFeedAdapter;
import com.example.recipes.adapter.RecipcAdapter;
import com.example.recipes.data.NewsFeedData;
import com.example.recipes.data.RecipcData;
import com.example.recipes.data.user_info;
import com.example.recipes.ui.newsfeed.NewsFeedEditActivity;
import com.example.recipes.ui.recipc.RecipcEditActivity;
import com.example.recipes.view.FullyGridLayoutManager;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.tools.ScreenUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

  public class FragmentUser2 extends Fragment {
    private View allView;
    private RecyclerView recyclerView;
    private RecipcAdapter recipcAdapter;
    private String userId;

    public static FragmentUser2 newInstance(String userId) {
        FragmentUser2 fragment = new FragmentUser2();
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
        allView = inflater.inflate(R.layout.fragment_user2, container, false);

        allView.findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                  startActivity(new Intent(getContext(), RecipcEditActivity.class));
            }
        });

        recyclerView = allView.findViewById(R.id.rvShow);
          FullyGridLayoutManager manager = new FullyGridLayoutManager(getContext(),
                2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4,
                ScreenUtils.dip2px(getContext(), 8), false));


        recipcAdapter = new RecipcAdapter(null);
        recipcAdapter.setUpDataListener(new RecipcAdapter.UpDataListener() {
            @Override
            public void onUpData() {
                getData();
            }
        });
        recyclerView.setAdapter(recipcAdapter);
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

        BmobQuery<RecipcData> query = new BmobQuery<RecipcData>();
        query.addWhereEqualTo("author", userInfo);
        query.include("author");
        query.order("-createdAt");
        query.findObjects(new FindListener<RecipcData>() {
            @Override
            public void done(List<RecipcData> object, BmobException e) {
                recipcAdapter.setNewInstance(object);
                if (object==null || object.size()==0){
                    allView.findViewById(R.id.ll_error).setVisibility(View.VISIBLE);
                }else{
                    allView.findViewById(R.id.ll_error).setVisibility(View.GONE);
                }
            }
        });
    }

}
