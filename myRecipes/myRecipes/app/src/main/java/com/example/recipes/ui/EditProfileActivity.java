package com.example.recipes.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipes.R;
import com.example.recipes.data.FollowData;
import com.example.recipes.data.user_info;
import com.example.recipes.util.LattePreference;
import com.example.recipes.util.NormalProgressDialog;
import com.example.recipes.util.PictureSelectorUtil;
import com.gyf.immersionbar.ImmersionBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_profile);

        ImmersionBar.with(this)
                .titleBarMarginTop(findViewById(R.id.title))
                .statusBarDarkFont(true, 0.2f)                   .keyboardEnable(true)                  .navigationBarWithKitkatEnable(false)                  .init();

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.tv_log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                  LattePreference.setAppUserInfo(null);
                startActivity(new Intent(EditProfileActivity.this, LoginActivity.class));
            }
        });

        findViewById(R.id.iv_head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                  PictureSelectorUtil.openRoundImage(EditProfileActivity.this, 1);
            }
        });
        findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                  save();
            }
        });

        initUserInfo();
    }

    private void save() {
        String name = ((EditText) findViewById(R.id.et_name)).getText().toString();
        String nameNo = ((EditText) findViewById(R.id.et_user_no)).getText().toString();
        if (name.equals(LattePreference.getAppUserInfo().getUser_name()) && nameNo.equals(LattePreference.getAppUserInfo().getUser_no())) {
            finish();
        } else if (nameNo.equals(LattePreference.getAppUserInfo().getUser_no())) {
            if (name.length()<3) {
                Toast.makeText(EditProfileActivity.this, "Please enter a name with at least 3 digits", Toast.LENGTH_LONG).show();
                return;
            }
            if (nameNo.length()<3) {
                Toast.makeText(EditProfileActivity.this, "Please enter a user_id with at least 3 digits", Toast.LENGTH_LONG).show();
                return;
            }
            user_info userInfo = LattePreference.getAppUserInfo();
            userInfo.setUser_name(name);
            userInfo.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        LattePreference.setAppUserInfo(userInfo);
                        Toast.makeText(EditProfileActivity.this, "User information modified successfully", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Failed to modify user information", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            if (name.length()<3) {
                Toast.makeText(EditProfileActivity.this, "Please enter a name with at least 3 digits", Toast.LENGTH_LONG).show();
                return;
            }
            if (nameNo.length()<3) {
                Toast.makeText(EditProfileActivity.this, "Please enter a user_id with at least 3 digits", Toast.LENGTH_LONG).show();
                return;
            }
            BmobQuery<user_info> query = new BmobQuery<user_info>();
            query.addWhereEqualTo("user_no", nameNo);
            query.findObjects(new FindListener<user_info>() {
                @Override
                public void done(List<user_info> object, BmobException e) {
                    if (e == null) {
                                                  if (object != null && object.size() > 0) {
                            Toast.makeText(EditProfileActivity.this, "UserId already exists", Toast.LENGTH_LONG).show();
                            return;
                        }
                        user_info userInfo1 = LattePreference.getAppUserInfo();
                        userInfo1.setUser_name(name);
                        userInfo1.setUser_no(nameNo);
                        userInfo1.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    LattePreference.setAppUserInfo(userInfo1);
                                    Toast.makeText(EditProfileActivity.this, "User information modified successfully", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(EditProfileActivity.this, "Failed to modify user information", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Failed to modify user information", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void initUserInfo() {
        ((EditText) findViewById(R.id.et_name)).setText(LattePreference.getAppUserInfo().getUser_name());
        ((EditText) findViewById(R.id.et_user_no)).setText(LattePreference.getAppUserInfo().getUser_no());

        ImageView iv_head = findViewById(R.id.iv_head);
        String image = "http://";
        if (LattePreference.getAppUserInfo().getImage() != null && !TextUtils.isEmpty(LattePreference.getAppUserInfo().getImage().getUrl())) {
            image = LattePreference.getAppUserInfo().getImage().getUrl();
        }
        Glide.with(iv_head).load(image)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(iv_head);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                                          List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList != null && selectList.size() > 0) {
                        String path = "";
                        for (LocalMedia media : selectList) {
                            if (media.isCut()) {
                                path = media.getCutPath();
                            } else {
                                path = media.getRealPath();
                            }
                        }
                        uploadFiles(path);
                    }
                    break;
                default:
                    break;
            }
        }
    }


    private void uploadFiles(String picPath) {


        try {
            NormalProgressDialog.showLoading(EditProfileActivity.this, "Uploading...");
            BmobFile bmobFile = new BmobFile(new File(picPath));
            bmobFile.uploadblock(new UploadFileListener() {

                @Override
                public void done(BmobException e) {
                    NormalProgressDialog.stopLoading();
                    if (e == null) {
                                                  user_info userInfo = LattePreference.getAppUserInfo();
                        userInfo.setImage(bmobFile);
                        userInfo.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    LattePreference.setAppUserInfo(userInfo);
                                    initUserInfo();
                                    Toast.makeText(EditProfileActivity.this, "Successfully modified Avatar", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(EditProfileActivity.this, "Failed to modify Avatar", Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                    } else {
                        Toast.makeText(EditProfileActivity.this, "Failed to modify Avatar", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onProgress(Integer value) {
                                      }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(EditProfileActivity.this, "Failed to modify Avatar", Toast.LENGTH_LONG).show();
        }

    }
}