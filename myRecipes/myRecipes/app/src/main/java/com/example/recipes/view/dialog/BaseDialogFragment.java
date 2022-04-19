package com.example.recipes.view.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipes.R;
import com.example.recipes.util.Utils;
import com.gyf.immersionbar.ImmersionBar;

import org.jetbrains.annotations.NotNull;


   
public abstract class BaseDialogFragment extends DialogFragment {

    protected Activity mActivity;
    protected View mRootView;
    protected Window mWindow;
    public Integer[] mWidthAndHeight;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                  setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
                  dialog.setCanceledOnTouchOutside(true);
        mWindow = dialog.getWindow();
        mWidthAndHeight = Utils.getWidthAndHeight(mWindow);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayoutId(), container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
        initView();
        setListener();

        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWidthAndHeight = Utils.getWidthAndHeight(mWindow);
    }

       
    protected abstract int setLayoutId();

       
    protected boolean isImmersionBarEnabled() {
        return true;
    }

       
    protected void initImmersionBar() {
        ImmersionBar.with(this).init();
    }


       
    protected void initData() {

    }

       
    protected void initView() {
          
    }

       
    protected void setListener() {

    }
}