package com.example.recipes.ui.recipc;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.recipes.R;
import com.example.recipes.adapter.SelImagsAdapter;
import com.example.recipes.data.FoodsData;
import com.example.recipes.data.RecipcData;
import com.example.recipes.data.StepData;
import com.example.recipes.fragment.recipc.FragmentRecipc1;
import com.example.recipes.util.NormalProgressDialog;
import com.example.recipes.util.PictureSelectorUtil;
import com.example.recipes.util.Utils;
import com.example.recipes.util.callback.CallbackManager;
import com.example.recipes.util.callback.CallbackType;
import com.example.recipes.util.callback.IGlobalCallback;
import com.example.recipes.view.FullyGridLayoutManager;
import com.gyf.immersionbar.ImmersionBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.ScreenUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

  public class RecipcStepActivity extends AppCompatActivity {

    private StepData stepData;
    private int index = -1;

    private SelImagsAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recipc_step);

        ImmersionBar.with(this)
                .titleBarMarginTop(findViewById(R.id.title))
                .statusBarDarkFont(true, 0.2f)                   .keyboardEnable(true)                  .navigationBarWithKitkatEnable(false)                  .init();

        initRecyclerView();

        stepData = (StepData) getIntent().getSerializableExtra("data");
        if (stepData == null) {
            stepData = new StepData();
        } else {
            ((EditText) findViewById(R.id.et_1)).setText(stepData.getDesc());
            if (!TextUtils.isEmpty(stepData.getImage())) {
                List<String> list = new ArrayList<>();
                list.add(stepData.getImage());
                mAdapter.setList(list);
            }
        }
        index = getIntent().getIntExtra("index", -1);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string1 = ((EditText) findViewById(R.id.et_1)).getText().toString();
                if (TextUtils.isEmpty(string1)) {
                    Toast.makeText(RecipcStepActivity.this, "Please enter step description", Toast.LENGTH_LONG).show();
                    return;
                }
                stepData.setDesc(string1);
                stepData.setImage(null);
                if (mAdapter.getData() == null || mAdapter.getData().size() <= 0) {
                    Intent intent = new Intent();
                    intent.putExtra("index", index);
                    intent.putExtra("data", stepData);
                    setResult(3, intent);
                    finish();
                    return;
                } else {
                    uploadFiles();
                }
            }
        });


    }

    private void uploadFiles() {
        try {
            String path = mAdapter.getData().get(0);
            if (path.startsWith("http")) {
                stepData.setImage(mAdapter.getData().get(0));
                Intent intent = new Intent();
                intent.putExtra("index", index);
                intent.putExtra("data", stepData);
                setResult(3, intent);
                finish();
                return;
            }
            NormalProgressDialog.showLoading(RecipcStepActivity.this, "Uploading...");
            BmobFile bmobFile = new BmobFile(new File(path));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    NormalProgressDialog.stopLoading();
                    if (e == null) {
                        stepData.setImage(bmobFile.getUrl());
                        Intent intent = new Intent();
                        intent.putExtra("index", index);
                        intent.putExtra("data", stepData);
                        setResult(3, intent);
                        finish();
                    } else {
                        Toast.makeText(RecipcStepActivity.this, "Failed to upload file", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onProgress(Integer value) {
                                      }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(RecipcStepActivity.this, "Failed to upload file", Toast.LENGTH_LONG).show();
        }

    }


    private void initRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.recycler);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this,
                1, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);

        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4,
                ScreenUtils.dip2px(this, 8), false));
        mAdapter = new SelImagsAdapter(this, new SelImagsAdapter.onAddPicClickListener() {
            @Override
            public void onAddPicClick() {
                Utils.hideKeyboard(mRecyclerView);
                PictureSelectorUtil.openImageVideo(RecipcStepActivity.this, 1, null);
            }
        });
        mAdapter.setSelectMax(1);
        mRecyclerView.setAdapter(mAdapter);
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
                        mAdapter.setList(path);
                    }
                    break;
                default:
                    break;
            }
        }
    }

}