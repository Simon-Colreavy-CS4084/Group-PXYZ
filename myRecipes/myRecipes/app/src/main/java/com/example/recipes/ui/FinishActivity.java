package com.example.recipes.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.recipes.R;

  public class FinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        finish();
    }
}