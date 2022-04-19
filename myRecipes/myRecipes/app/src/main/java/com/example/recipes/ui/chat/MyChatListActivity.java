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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipes.R;
import com.example.recipes.adapter.MsgAdapter;
import com.example.recipes.adapter.MsgListAdapter;
import com.example.recipes.data.Msg;
import com.example.recipes.data.user_info;
import com.example.recipes.util.LattePreference;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

   

public class MyChatListActivity extends AppCompatActivity {
    private List<Msg> msgList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MsgListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mychat_list);

        ImmersionBar.with(this)
                .titleBarMarginTop(findViewById(R.id.title))
                .statusBarDarkFont(true, 0.2f)                   .keyboardEnable(true)                  .navigationBarWithKitkatEnable(false)                  .init();

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                  LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MsgListAdapter(msgList);
        recyclerView.setAdapter(adapter);
                  initMsgs();
    }

    private void initMsgs() {

        user_info userInfo = LattePreference.getAppUserInfo();

                            BmobQuery<Msg> eq1 = new BmobQuery<Msg>();
        eq1.addWhereEqualTo("otherUser", userInfo);
                  BmobQuery<Msg> eq2 = new BmobQuery<Msg>();
        eq2.addWhereEqualTo("meUser", userInfo);

                  List<BmobQuery<Msg>> queries1 = new ArrayList<BmobQuery<Msg>>();
        queries1.add(eq1);
        queries1.add(eq2);
        BmobQuery<Msg> msgBmobQuery = new BmobQuery<Msg>().or(queries1);
                  msgBmobQuery.include("meUser,otherUser");
        msgBmobQuery.order("createdAt");
        msgBmobQuery.findObjects(new FindListener<Msg>() {
            @Override
            public void done(List<Msg> object, BmobException e) {
                handler.sendEmptyMessageDelayed(1, 3000);
                if (e == null) {
                    msgList = new ArrayList<>();
                                          for (Msg msg : object) {
                        boolean isAdd = false;
                        for (int i = 0; i < msgList.size(); i++) {
                            Msg msg1=msgList.get(i);
                            String id1 = msg1.getMeUser().getObjectId() +
                                    msg1.getOtherUser().getObjectId();
                            String id2 = msg1.getOtherUser().getObjectId() +
                                    msg1.getMeUser().getObjectId();
                            if (msg.getMsgListId().equals(id1) || msg.getMsgListId().equals(id2)) {
                                msgList.set(i,msg);
                                isAdd = true;
                                break;
                            }
                        }
                        if (!isAdd) {
                            msgList.add(msg);
                        }
                    }

                    adapter = new MsgListAdapter(msgList);
                    recyclerView.setAdapter(adapter);
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