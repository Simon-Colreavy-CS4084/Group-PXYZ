package com.example.recipes;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraXConfig;
import androidx.camera.camera2.Camera2Config;

import com.luck.picture.lib.app.IApp;
import com.luck.picture.lib.app.PictureAppMaster;
import com.luck.picture.lib.engine.PictureSelectorEngine;

import cn.bmob.v3.Bmob;

public class MyApplication extends Application implements IApp, CameraXConfig.Provider {
    static Context context;
    static MyApplication myApplication;

    public static Context getContext() {
        return context;
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = MyApplication.this;
        myApplication = MyApplication.this;

                   Bmob.initialize(this, "adaa0a2af16ca9eb8632e24abd9c1c90");

        PictureAppMaster.getInstance().setApp(this);
    }

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }

    @Override
    public Context getAppContext() {
        return this;
    }


    @Override
    public PictureSelectorEngine getPictureSelectorEngine() {
        return new PictureSelectorEngineImp();
    }
}
