package com.example.recipes;

import android.util.Log;

import com.example.recipes.util.GlideEngine;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.engine.PictureSelectorEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.util.List;

   
public class PictureSelectorEngineImp implements PictureSelectorEngine {
    private static final String TAG = PictureSelectorEngineImp.class.getSimpleName();

    @Override
    public ImageEngine createEngine() {
                              return GlideEngine.createGlideEngine();
    }

    @Override
    public OnResultCallbackListener<LocalMedia> getResultCallbackListener() {
        return new OnResultCallbackListener<LocalMedia>() {
            @Override
            public void onResult(List<LocalMedia> result) {
                                                      Log.i(TAG, "onResult:" + result.size());
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "PictureSelector onCancel");
            }
        };
    }
}
