package com.example.recipes.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipes.R;
import com.example.recipes.data.user_info;
import com.example.recipes.util.PwdCheckUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        final EditText et_name = findViewById(R.id.et_name);
        final EditText et_account = findViewById(R.id.et_account);
        final EditText et_password = findViewById(R.id.et_password);
        final EditText et_confirm_password = findViewById(R.id.et_confirm_password);

        findViewById(R.id.btn_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = et_name.getText().toString();
                final String account = et_account.getText().toString();
                final String password = et_password.getText().toString();
                final String confirmPassword = et_confirm_password.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(RegisterActivity.this, "Please input a name", Toast.LENGTH_LONG).show();
                    return;
                }
                if (name.length()<3) {
                    Toast.makeText(RegisterActivity.this, "Please enter a name with at least 3 digits", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(account)) {
                    Toast.makeText(RegisterActivity.this, "Please input a account", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Please input a password", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!PwdCheckUtil.isLetterDigit(password)) {
                    Toast.makeText(RegisterActivity.this, "Include at least two of upper and lower case letters and numbers", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.length()<6) {
                    Toast.makeText(RegisterActivity.this, "Enter a password with at least six digits", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "The passwords entered are inconsistent", Toast.LENGTH_LONG).show();
                    return;
                }
                BmobQuery<user_info> bmobQuery = new BmobQuery<user_info>();
                                    bmobQuery.findObjects(new FindListener<user_info>() {
                    @Override
                    public void done(List<user_info> list, BmobException e) {
                        if (e == null) {
                            for (user_info userInfo:list) {
                                if (userInfo.getAccount().equals(account)){
                                    Toast.makeText(RegisterActivity.this, "Account already registered", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                            register(name, account, password,"user_"+list.size());
                        } else {
                            Toast.makeText(RegisterActivity.this, "Query failed：" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        findViewById(R.id.tv_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

          private void register(String name, String account, String password,String userNo) {
        user_info userInfo = new user_info();
        userInfo.setUser_name(name);
        userInfo.setAccount(account);
        userInfo.setPass_word(password);
        userInfo.setUser_no(userNo);

        userInfo.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                                          Toast.makeText(RegisterActivity.this, "register successful", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                                          Log.e("asd", e.getMessage());
                    Toast.makeText(RegisterActivity.this, "register failed：" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

          private long firstTime = 0;


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(RegisterActivity.this, "Press again to exit the program", Toast.LENGTH_SHORT).show();
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