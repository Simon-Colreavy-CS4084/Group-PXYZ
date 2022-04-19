package com.example.recipes.ui.newsfeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.recipes.R;
import com.example.recipes.adapter.NewsFeedAdapter;
import com.example.recipes.data.NewsFeedData;
import com.example.recipes.util.Utils;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class NewsFeedSearchResultActivity extends AppCompatActivity {
    private NewsFeedAdapter newsFeedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_feed_search_result);

        ImmersionBar.with(this)
                .titleBarMarginTop(findViewById(R.id.title))
                .statusBarDarkFont(true, 0.2f)                   .keyboardEnable(true)                  .navigationBarWithKitkatEnable(false)                  .init();

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String data = getIntent().getStringExtra("data");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsFeedAdapter = new NewsFeedAdapter(null);
        recyclerView.setAdapter(newsFeedAdapter);
        getData(data);
        Utils.hideKeyboard(recyclerView);
    }

    private void getData(String data) {
        BmobQuery<NewsFeedData> query = new BmobQuery<NewsFeedData>();
        query.include("author");
        query.order("-createdAt");
        query.findObjects(new FindListener<NewsFeedData>() {
            @Override
            public void done(List<NewsFeedData> object1, BmobException e) {
                List<NewsFeedData> object = new ArrayList<>();
                if (object1 != null) {
                    for (NewsFeedData newsFeedData : object1) {
                        if (newsFeedData.getContent().contains(data)
                                || newsFeedData.getAuthor().getUser_no().contains(data)) {
                            object.add(newsFeedData);
                        }
                    }
                }
                newsFeedAdapter.setNewInstance(object);

                if (object == null || object.size() == 0) {
                    findViewById(R.id.ll_error).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.ll_error).setVisibility(View.GONE);
                }
            }
        });
    }
}