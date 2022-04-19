package com.example.recipes.ui.chat;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipes.R;
import com.example.recipes.adapter.MsgAdapter;
import com.example.recipes.data.Msg;
import com.example.recipes.data.user_info;
import com.example.recipes.util.DataDispose;
import com.example.recipes.util.LattePreference;
import com.example.recipes.util.Utils;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

   

public class MyChatActivity extends AppCompatActivity {
    private List<Msg> msgList = new ArrayList<>();
    private EditText inputText;
    private Button send;
    private RecyclerView recyclerView;
    private MsgAdapter adapter;
    private String otherId = "5ea8b14809";      private user_info userData = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mychat);

        ImmersionBar.with(this)
                .titleBarMarginTop(findViewById(R.id.title))
                .statusBarDarkFont(true, 0.2f)                   .keyboardEnable(true)                  .navigationBarWithKitkatEnable(false)                  .init();

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        otherId = getIntent().getStringExtra("otherId");
        userData = (user_info) getIntent().getSerializableExtra("userData");
        String name = getIntent().getStringExtra("name");

        TextView tv_top = (TextView) findViewById(R.id.tv_top);
        TextView tv_top_hint = (TextView) findViewById(R.id.tv_top_hint);
        tv_top.setText(userData.showName());
        String hint = "@" + userData.getUser_no();
        tv_top_hint.setText(hint);

        ImageView iv_top = findViewById(R.id.iv_top);
        String image = "http://";
        if (userData.getImage() != null && !TextUtils.isEmpty(userData.getImage().getUrl())) {
            image = userData.getImage().getUrl();
        }
        Glide.with(iv_top).load(image)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(iv_top);


        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                  LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        recyclerView.setAdapter(adapter);
                  initMsgs();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(v);
                String content = inputText.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                                          inputText.setText("");

                    user_info userInfo2 = LattePreference.getAppUserInfo();
                    user_info userInfo1 = new user_info();                      userInfo1.setObjectId(otherId);
                    final Msg comment = new Msg();
                    comment.setContent(content);
                    comment.setMsgListId(userInfo2.getObjectId() + otherId);
                    comment.setMeUser(userInfo2);
                    comment.setOtherUser(userInfo1);
                    comment.save(new SaveListener<String>() {

                        @Override
                        public void done(String objectId, BmobException e) {
                            if (e == null) {
                                                                      adapter.addData(comment);
                                                                  recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (recyclerView.canScrollVertically(1)) {
                                            ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(adapter.getData().size() - 1, 0);
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(MyChatActivity.this, "failure：" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                    });
                }

            }
        });
    }

    private void initMsgs() {
        user_info userInfo1 = new user_info();
        userInfo1.setObjectId(otherId);

        user_info userInfo2 = new user_info();
        userInfo2.setObjectId(LattePreference.getAppUserInfo().getObjectId());

                            BmobQuery<Msg> eq1 = new BmobQuery<Msg>();
        eq1.addWhereEqualTo("msgListId", otherId + LattePreference.getAppUserInfo().getObjectId());
                  BmobQuery<Msg> eq2 = new BmobQuery<Msg>();
        eq2.addWhereEqualTo("msgListId", LattePreference.getAppUserInfo().getObjectId() + otherId);

                  List<BmobQuery<Msg>> queries1 = new ArrayList<BmobQuery<Msg>>();
        queries1.add(eq1);
        queries1.add(eq2);
        BmobQuery<Msg> query = new BmobQuery<Msg>().or(queries1);
                  query.include("meUser,otherUser");
        query.findObjects(new FindListener<Msg>() {
            @Override
            public void done(List<Msg> object, BmobException e) {
                handler.sendEmptyMessageDelayed(1, 3000);
                if (e == null) {
                                          boolean isScroll = false;

                    if (object.size() != adapter.getData().size()) {
                        isScroll = true;
                    }

                    adapter.setNewInstance(object);
                                            if (recyclerView.canScrollVertically(1) && isScroll) {
                        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(adapter.getData().size() - 1, 0);
                    }
                } else {
                    Log.i("bmob", "failure：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            handler.removeMessages(1);
        } catch (Exception e) {

        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 1:
                        initMsgs();
                        break;
                }
            } catch (Exception e) {

            }
        }
    };
}