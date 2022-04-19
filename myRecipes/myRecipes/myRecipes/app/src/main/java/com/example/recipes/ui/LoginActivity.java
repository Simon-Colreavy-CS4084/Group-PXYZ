package com.example.recipes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipes.R;
import com.example.recipes.data.user_info;
import com.example.recipes.util.LattePreference;
import com.example.recipes.util.PwdCheckUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        final EditText et_account = findViewById(R.id.et_account);
        final EditText et_password = findViewById(R.id.et_password);
        et_account.setText("chiayu@mail.com");
        et_password.setText("abc1234");
        findViewById(R.id.btn_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = et_account.getText().toString();
                final String password = et_password.getText().toString();

                if (TextUtils.isEmpty(account)) {
                    Toast.makeText(LoginActivity.this, "Please input a account", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please input a password", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!PwdCheckUtil.isLetterDigit(password)) {
                    Toast.makeText(LoginActivity.this, "Include at least two of upper and lower case letters and numbers", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(LoginActivity.this, "Enter a password with at least six digits", Toast.LENGTH_LONG).show();
                    return;
                }
                BmobQuery<user_info> bmobQuery = new BmobQuery<user_info>();
                                  bmobQuery.addWhereEqualTo("account", account);
                bmobQuery.findObjects(new FindListener<user_info>() {
                    @Override
                    public void done(List<user_info> list, BmobException e) {
                        if (e == null) {
                            if (list == null || list.size() == 0) {
                                Toast.makeText(LoginActivity.this, "Account not registered", Toast.LENGTH_LONG).show();
                            } else if (!list.get(0).getPass_word().equals(password)) {
                                Toast.makeText(LoginActivity.this, "Password error", Toast.LENGTH_LONG).show();
                            } else {
                                LattePreference.setAppUserInfo(list.get(0));
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Query failed：" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        findViewById(R.id.tv_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }


          private long firstTime = 0;

       
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(LoginActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                finish();
                System.exit(0);
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}