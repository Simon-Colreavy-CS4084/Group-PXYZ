package com.example.recipes.ui.newsfeed;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipes.R;
import com.example.recipes.adapter.GridImageAdapter;
import com.example.recipes.adapter.SelImagsAdapter;
import com.example.recipes.data.NewsFeedData;
import com.example.recipes.fragment.recipc.FragmentRecipc1;
import com.example.recipes.util.GlideEngine;
import com.example.recipes.util.LattePreference;
import com.example.recipes.util.NormalProgressDialog;
import com.example.recipes.util.PictureSelectorUtil;
import com.example.recipes.util.Utils;
import com.example.recipes.view.FullyGridLayoutManager;
import com.gyf.immersionbar.ImmersionBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.ScreenUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;


  public class NewsFeedEditActivity extends AppCompatActivity {
    private EditText et_1, et_2;
    private SelImagsAdapter mAdapter;
    private NewsFeedData newsFeedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_feed_edit);

        ImmersionBar.with(this)
                .titleBarMarginTop(findViewById(R.id.title))
                .statusBarDarkFont(true, 0.2f)                   .keyboardEnable(true)                  .navigationBarWithKitkatEnable(false)                  .init();

        newsFeedData = (NewsFeedData) getIntent().getSerializableExtra("data");
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView tv_title = findViewById(R.id.tv_title);
        et_1 = findViewById(R.id.et_1);
        et_2 = findViewById(R.id.et_2);
        initRecyclerView();


        if (newsFeedData == null) {
            tv_title.setText("Add NewsFeed");
        } else {
            tv_title.setText("Edit NewsFeed");

            et_1.setText(newsFeedData.getTitle());
            et_2.setText(newsFeedData.getContent());
            mAdapter.setList(newsFeedData.getImgs());
        }
        findViewById(R.id.btn_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string1 = et_1.getText().toString();
                String string2 = et_2.getText().toString();
                        if (TextUtils.isEmpty(string2)) {
                    Toast.makeText(NewsFeedEditActivity.this, "input content", Toast.LENGTH_LONG).show();
                    return;
                }
                uploadFiles();
            }
        });

    }

    private void initRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.recycler);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this,
                4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);

        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4,
                ScreenUtils.dip2px(this, 8), false));
        mAdapter = new SelImagsAdapter(this, new SelImagsAdapter.onAddPicClickListener() {
            @Override
            public void onAddPicClick() {
                Utils.hideKeyboard(mRecyclerView);
                PictureSelectorUtil.openImageVideo(NewsFeedEditActivity.this, 9-mAdapter.getData().size(), null);
            }
        });
        mAdapter.setSelectMax(8);
        mRecyclerView.setAdapter(mAdapter);


    }



    private void insertNewsFeed(List<String> files) {
        if (newsFeedData != null) {
            newsFeedData.setTitle(et_1.getText().toString());
            newsFeedData.setContent(et_2.getText().toString());
            newsFeedData.setAuthor(LattePreference.getAppUserInfo());
            newsFeedData.setImgs(files);
            newsFeedData.update(new UpdateListener() {

                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Toast.makeText(NewsFeedEditActivity.this, "update completed",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(NewsFeedEditActivity.this, "failure：" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            newsFeedData = new NewsFeedData();
            newsFeedData.setTitle(et_1.getText().toString());
            newsFeedData.setContent(et_2.getText().toString());
            newsFeedData.setAuthor(LattePreference.getAppUserInfo());
            newsFeedData.setImgs(files);
            newsFeedData.save(new SaveListener<String>() {

                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
                        Toast.makeText(NewsFeedEditActivity.this, "Added successfully",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(NewsFeedEditActivity.this, "failure：" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            });

        }

    }


    private void uploadFiles() {

        List<String> files = new ArrayList<>();
        List<String> filesY = new ArrayList<>();
        boolean isUpload = false;
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            try {
                String path = mAdapter.getData().get(i);
                if (path.startsWith("http")) {
                    filesY.add(path);
                }else{
                    File file = new File(path);
                    files.add(path);
                    isUpload = true;
                }

            } catch (Exception e) {
              }
        }
        if (!isUpload) {
            insertNewsFeed(filesY);
            return;
        }

        final String[] filePaths = new String[files.size()];
        for (int i = 0; i < files.size(); i++) {
            filePaths[i] = files.get(i);
        }
        NormalProgressDialog.showLoading(NewsFeedEditActivity.this, "Uploading...");
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {

                                                    if (urls.size() == filePaths.length) {                                            NormalProgressDialog.stopLoading();
                    for (BmobFile bmobFile:files) {
                        filesY.add(bmobFile.getUrl());
                    }
                    insertNewsFeed(filesY);
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                NormalProgressDialog.stopLoading();
                Log.e("asd", errormsg);
                Toast.makeText(NewsFeedEditActivity.this, "error code" + statuscode + ",wrong description:" + errormsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                                                                                    }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                                          List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList != null && selectList.size() > 0) {
                        List<String> path = new ArrayList<>();
                        for (LocalMedia media : selectList) {
                            if (media.isCut()) {
                                path.add(media.getCutPath());
                            } else {
                                path.add(media.getRealPath());
                            }
                        }
                        mAdapter.addLists(path);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}