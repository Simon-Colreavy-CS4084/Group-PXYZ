package com.example.recipes.view.dialog;


import android.content.res.Configuration;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;

import com.example.recipes.R;
import com.gyf.immersionbar.ImmersionBar;


   
public class RightDialogFragment extends BaseDialogFragment {

    private Toolbar toolbar;

    @Override
    public void onStart() {
        super.onStart();
        mWindow.setGravity(Gravity.TOP | Gravity.END);
        mWindow.setWindowAnimations(R.style.RightAnimation);
        mWindow.setLayout(mWidthAndHeight[0] / 2, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void initView() {
        super.initView();
        toolbar = mRootView.findViewById(R.id.toolbar);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).titleBar(toolbar)
                  .keyboardEnable(true)
                .init();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWindow.setLayout(mWidthAndHeight[0] / 2, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}