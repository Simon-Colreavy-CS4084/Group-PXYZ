package com.example.recipes.fragment.recipc;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipes.R;
import com.example.recipes.adapter.SelImagsAdapter;
import com.example.recipes.data.RecipcData;
import com.example.recipes.map.MapsActivityCurrentPlace;
import com.example.recipes.ui.MainActivity;
import com.example.recipes.ui.recipc.RecipcEditActivity;
import com.example.recipes.util.NormalProgressDialog;
import com.example.recipes.util.PictureSelectorUtil;
import com.example.recipes.util.callback.CallbackManager;
import com.example.recipes.util.callback.CallbackType;
import com.example.recipes.util.callback.IGlobalCallback;
import com.example.recipes.view.FullyGridLayoutManager;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.ScreenUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

import static android.app.Activity.RESULT_OK;

  public class FragmentRecipc1 extends Fragment {
    private View allView;
    private SelImagsAdapter mAdapter;
    private TextView tv_address;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        allView = inflater.inflate(R.layout.fragment_recipc1, container, false);
        initRecyclerView();
        initView();

        return allView;
    }

    private void initView() {
        tv_address = allView.findViewById(R.id.tv_address);
        RecipcData recipcData = ((RecipcEditActivity) getActivity()).getRecipcData();
        ((EditText) allView.findViewById(R.id.et_name)).setText(recipcData.getTitle());
        ((EditText) allView.findViewById(R.id.et_content)).setText(recipcData.getContent());
        if (!TextUtils.isEmpty(recipcData.getImg())) {
            List<String> list = new ArrayList<>();
            list.add(recipcData.getImg());
            mAdapter.setList(list);
        }

        final IGlobalCallback<RecipcData> callback = new IGlobalCallback<RecipcData>() {
            @Override
            public void executeCallback(@Nullable RecipcData recipcData) {
                String name = ((EditText) allView.findViewById(R.id.et_name)).getText().toString();
                String content = ((EditText) allView.findViewById(R.id.et_content)).getText().toString();
                String address = tv_address.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getContext(), "Please name your recipe", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(getContext(), "Please select address", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(getContext(), "Please enter a recipe introduction", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mAdapter.getData() == null || mAdapter.getData().size() <= 0) {
                    Toast.makeText(getContext(), "Add recipe photos / videos", Toast.LENGTH_LONG).show();
                    return;
                }
                recipcData.setTitle(name);
                recipcData.setAddress(address);
                recipcData.setContent(content);
                uploadFiles(recipcData);
            }
        };
        CallbackManager
                .getInstance()
                .addCallback(CallbackType.RECIPC_EDIT1, callback);
        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch(true);
            }
        });

    }

    class ResultContract extends ActivityResultContract<Boolean, String> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Boolean input) {
            Intent intent = new Intent(getActivity(), MapsActivityCurrentPlace.class);
            return intent;
        }

        @Override
        public String parseResult(int resultCode, @Nullable Intent intent) {
            if (intent == null) {
                return "";
            }
            return intent.getStringExtra("info");
        }
    }

    ActivityResultLauncher launcher = registerForActivityResult(new ResultContract(), new ActivityResultCallback<String>() {
        @Override
        public void onActivityResult(String result) {
            if (!TextUtils.isEmpty(result)) {
                tv_address.setText(result);
            } else {
                Toast.makeText(getActivity(), "null select", Toast.LENGTH_SHORT).show();
            }
        }
    });

    private void initRecyclerView() {
        RecyclerView mRecyclerView = allView.findViewById(R.id.recycler);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(getContext(),
                1, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);

        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4,
                ScreenUtils.dip2px(getContext(), 8), false));
        mAdapter = new SelImagsAdapter(getContext(), new SelImagsAdapter.onAddPicClickListener() {
            @Override
            public void onAddPicClick() {
                PictureSelectorUtil.openImageVideo(FragmentRecipc1.this, 1, null);
            }
        });
        mAdapter.setSelectMax(1);
        mRecyclerView.setAdapter(mAdapter);


    }

    private void uploadFiles(RecipcData recipcData) {
        try {
            String path = mAdapter.getData().get(0);
            if (path.startsWith("http")) {
                recipcData.setImg(mAdapter.getData().get(0));
                final IGlobalCallback<RecipcData> callback = CallbackManager
                        .getInstance()
                        .getCallback(CallbackType.RECIPC_EDIT_RESULT);
                if (callback != null) {
                    callback.executeCallback(recipcData);
                }
                return;
            }
            NormalProgressDialog.showLoading(getContext(), "Uploading...");
            BmobFile bmobFile = new BmobFile(new File(path));
            bmobFile.uploadblock(new UploadFileListener() {

                @Override
                public void done(BmobException e) {
                    NormalProgressDialog.stopLoading();
                    if (e == null) {
                        recipcData.setImg(bmobFile.getUrl());
                        final IGlobalCallback<RecipcData> callback = CallbackManager
                                .getInstance()
                                .getCallback(CallbackType.RECIPC_EDIT_RESULT);
                        if (callback != null) {
                            callback.executeCallback(recipcData);
                        }
                        initView();
                    } else {
                        Toast.makeText(getContext(), "Failed to upload file", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onProgress(Integer value) {
                                      }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to upload file", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                                          List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList != null && selectList.size() > 0) {
                        List<String> path = new ArrayList<>();
                        for (LocalMedia media : selectList) {
                            if (media.isCut()) {
                                path.add(media.getCutPath());
                            } else {
                                path.add(media.getRealPath());
                            }
                        }
                        mAdapter.setList(path);
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
