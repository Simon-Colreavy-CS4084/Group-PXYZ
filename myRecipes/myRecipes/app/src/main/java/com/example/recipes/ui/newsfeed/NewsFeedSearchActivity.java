package com.example.recipes.ui.newsfeed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.recipes.R;
import com.example.recipes.util.LattePreference;
import com.example.recipes.view.FlowsLayout;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


   
public class NewsFeedSearchActivity extends AppCompatActivity {
    private EditText mSearchView;
          private FlowsLayout mHotView;
          private FlowsLayout mHistoryView;

          private String[] hotVals = new String[]{"formula", "Storage method", "necessary"};

    private final String KEY_HISTORY = "key_historys";
    private final String LIEN_HISTORY = "&&~!";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_feed_search);

        ImmersionBar.with(this)
                .titleBar(findViewById(R.id.title))
                .statusBarDarkFont(true, 0.2f)                   .keyboardEnable(true)                  .navigationBarWithKitkatEnable(false)                  .init();

        initView();
        initClick();
        initHotData();
        String str = LattePreference.getAppString(KEY_HISTORY);
        resetHistory(str);
    }


    private void initView() {
        mSearchView = findViewById(R.id.et_search);
        mHotView = findViewById(R.id.flowlayout_hot);
        mHistoryView = findViewById(R.id.flowlayout_history);
           
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
                  mSearchView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {                      insertData(mSearchView.getText().toString().trim());
                }
                return false;
            }
        });

        String content = getIntent().getStringExtra("content");
        try {
            mSearchView.setText(content);
            mSearchView.setSelection(content.length());          } catch (Exception e) {
            e.printStackTrace();
        }


    }

          private void initHotData() {
        LayoutInflater mInflater = LayoutInflater.from(this);

        for (int i = 0; i < hotVals.length; i++) {
            if (!TextUtils.isEmpty(hotVals[i])) {
                View view = mInflater.inflate(
                        R.layout.item_search_label_tv, mHotView,
                        false);

                TextView tv = view.findViewById(R.id.tv_show);
                tv.setText(hotVals[i]);
                final String str = tv.getText().toString();
                                  tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSearchView.setText(str);
                        insertData(str);
                    }
                });
                mHotView.addView(view);
            }
        }
    }


       
    private void resetHistory(String list) {
        if (TextUtils.isEmpty(list)) {
            list = "";
        }

        LattePreference.setAppString(KEY_HISTORY, list);

        LayoutInflater mInflater = LayoutInflater.from(this);

        mHistoryView.removeAllViews();

        String[] strings = list.split(LIEN_HISTORY);
        for (String str : strings) {
            if (!TextUtils.isEmpty(str)) {
                View view = mInflater.inflate(
                        R.layout.item_search_label_tv, mHistoryView,
                        false);

                TextView tv = view.findViewById(R.id.tv_show);
                tv.setText(str);
                tv.setTag(R.id.view_tag, str);
                                  tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String str = (String) v.getTag(R.id.view_tag);
                        insertData(str);
                    }
                });
                mHistoryView.addView(view);
            }
        }

    }


    private void initClick() {
        findViewById(R.id.toolbar_iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                  finish();
            }
        });
        findViewById(R.id.tv_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                  insertData(mSearchView.getText().toString().trim());
            }
        });
        findViewById(R.id.tv_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                  resetHistory(null);
                Toast.makeText(NewsFeedSearchActivity.this, "History cleared successfully", Toast.LENGTH_LONG).show();
            }
        });
    }


       
    private void insertData(String data) {
        mSearchView.setText(data);


                  try {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
                  if (!TextUtils.isEmpty(data)) {
            mSearchView.setSelection(data.length());
            String set = LattePreference.getAppString(KEY_HISTORY);
            String[] strings = set.split(LIEN_HISTORY);
            List<String> arrayList = Arrays.asList(strings);
            List<String> list = new ArrayList(arrayList);
            boolean hasData = list.contains(data);

            if (hasData) {
              } else {
                list.add(data);
            }
            set = "";
            for (String string : list) {
                if (data.equals(string)) {
                    set = string + LIEN_HISTORY + set;
                } else {
                    set += string + LIEN_HISTORY;
                }
            }
            resetHistory(set);
        }


                  if (!TextUtils.isEmpty(data)) {
            Intent intent = new Intent(NewsFeedSearchActivity.this, NewsFeedSearchResultActivity.class);
            intent.putExtra("data", data);
            startActivity(intent);
        }


    }


}
