package com.example.recipes.ui.recipc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipes.R;
import com.example.recipes.adapter.RecipcEditViewPagerAdapter;
import com.example.recipes.data.RecipcData;
import com.example.recipes.util.LattePreference;
import com.example.recipes.util.Utils;
import com.example.recipes.util.callback.CallbackManager;
import com.example.recipes.util.callback.CallbackType;
import com.example.recipes.util.callback.IGlobalCallback;
import com.gyf.immersionbar.ImmersionBar;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


  public class RecipcEditActivity extends AppCompatActivity {
    private RecipcData recipcData;
    private String title = "";
    private TextView tv_title, btn_confirm;
    private ViewPager mViewPager;
    private int currentPage = 0;

    public RecipcData getRecipcData() {
        return recipcData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recipc_edit);

        ImmersionBar.with(this)
                .titleBarMarginTop(findViewById(R.id.title))
                .statusBarDarkFont(true, 0.2f)                   .keyboardEnable(true)                  .navigationBarWithKitkatEnable(false)                  .init();

        recipcData = (RecipcData) getIntent().getSerializableExtra("data");
        if (recipcData == null) {
            recipcData = new RecipcData();
            title = "Create Recipes";
        } else {
            title = "Modify Recipes";
        }
        recipcData.setAuthor(LattePreference.getAppUserInfo());
        initView();

        setUI();
    }

    private void initView() {
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        tv_title = (TextView) findViewById(R.id.tv_title);
        btn_confirm = (TextView) findViewById(R.id.btn_confirm);
                  mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(8);
        RecipcEditViewPagerAdapter myFragmentPagerAdapter = new RecipcEditViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myFragmentPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Utils.hideKeyboard(mViewPager);
                currentPage = position;
                setUI();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(v);
                final IGlobalCallback<RecipcData> callback = new IGlobalCallback<RecipcData>() {
                    @Override
                    public void executeCallback(@Nullable RecipcData recipcData) {
                        RecipcEditActivity.this.recipcData = recipcData;
                        if (currentPage < 4) {
                            mViewPager.setCurrentItem(currentPage + 1);
                        } else {
                            if (TextUtils.isEmpty(RecipcEditActivity.this.recipcData.getObjectId())){
                                RecipcEditActivity.this.recipcData.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e==null){
                                            Toast.makeText(RecipcEditActivity.this, "Submitted successfully", Toast.LENGTH_LONG).show();
                                            finish();
                                        }else{
                                            Toast.makeText(RecipcEditActivity.this, "Submission failed, please check the network and try again!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }else{
                                RecipcEditActivity.this.recipcData.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e==null){
                                            Toast.makeText(RecipcEditActivity.this, "Submitted successfully", Toast.LENGTH_LONG).show();
                                            finish();
                                        }else{
                                            Toast.makeText(RecipcEditActivity.this, "Submission failed, please check the network and try again!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                };
                CallbackManager
                        .getInstance()
                        .addCallback(CallbackType.RECIPC_EDIT_RESULT, callback);
                if (currentPage == 0) {
                    final IGlobalCallback<RecipcData> callback1 = CallbackManager
                            .getInstance()
                            .getCallback(CallbackType.RECIPC_EDIT1);
                    if (callback1 != null) {
                        callback1.executeCallback(recipcData);
                    }
                } else if (currentPage == 1) {
                    final IGlobalCallback<RecipcData> callback1 = CallbackManager
                            .getInstance()
                            .getCallback(CallbackType.RECIPC_EDIT2);
                    if (callback1 != null) {
                        callback1.executeCallback(recipcData);
                    }
                }else if (currentPage == 2) {
                    final IGlobalCallback<RecipcData> callback1 = CallbackManager
                            .getInstance()
                            .getCallback(CallbackType.RECIPC_EDIT3);
                    if (callback1 != null) {
                        callback1.executeCallback(recipcData);
                    }
                }else if (currentPage == 3) {
                    final IGlobalCallback<RecipcData> callback1 = CallbackManager
                            .getInstance()
                            .getCallback(CallbackType.RECIPC_EDIT4);
                    if (callback1 != null) {
                        callback1.executeCallback(recipcData);
                    }
                }else if (currentPage == 4) {
                    final IGlobalCallback<RecipcData> callback1 = CallbackManager
                            .getInstance()
                            .getCallback(CallbackType.RECIPC_EDIT5);
                    if (callback1 != null) {
                        callback1.executeCallback(recipcData);
                    }
                }
            }
        });
    }

    private void setUI() {
        tv_title.setText(title + "      " + (currentPage + 1) + "/5");
        if (currentPage < 4) {
            btn_confirm.setText("next step");
        } else {
            btn_confirm.setText("Submit");
        }

        if (currentPage == 0) {
            ((ImageView) findViewById(R.id.iv_back)).setImageResource(R.drawable.ic_baseline_clear_24);
        } else {
            ((ImageView) findViewById(R.id.iv_back)).setImageResource(R.drawable.ic_arrow_back_black_24dp);
        }

    }

    private void onBack() {
        if (currentPage == 0) {
            finish();
        } else {
            mViewPager.setCurrentItem(currentPage - 1);
        }
    }


       
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            onBack();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}