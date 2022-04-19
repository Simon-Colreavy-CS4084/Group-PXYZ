package com.example.recipes.fragment.recipc;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.recipes.R;
import com.example.recipes.adapter.NewsFeedAdapter;
import com.example.recipes.adapter.RecipcFoodsAdapter;
import com.example.recipes.data.FoodsData;
import com.example.recipes.data.RecipcData;
import com.example.recipes.ui.recipc.RecipcEditActivity;
import com.example.recipes.ui.recipc.RecipcFoodsActivity;
import com.example.recipes.util.callback.CallbackManager;
import com.example.recipes.util.callback.CallbackType;
import com.example.recipes.util.callback.IGlobalCallback;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.tools.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

  public class FragmentRecipc3 extends Fragment {
    private View allView;
    private RecipcFoodsAdapter adapter;  

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        allView = inflater.inflate(R.layout.fragment_recipc3, container, false);

        RecyclerView recyclerView = allView.findViewById(R.id.rvShow);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecipcFoodsAdapter(null, this);

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4,
                ScreenUtils.dip2px(getContext(), 8), false));

        recyclerView.setAdapter(adapter);

        initView();
        return allView;
    }

    private void initView() {
        final IGlobalCallback<RecipcData> callback = new IGlobalCallback<RecipcData>() {
            @Override
            public void executeCallback(@Nullable RecipcData recipcData) {
                if (adapter.getData().size() == 0) {
                    Toast.makeText(getContext(), "Please add at least one ingredient", Toast.LENGTH_LONG).show();
                    return;
                }
                recipcData.setFoodsDataList(adapter.getData());
                final IGlobalCallback<RecipcData> callback = CallbackManager
                        .getInstance()
                        .getCallback(CallbackType.RECIPC_EDIT_RESULT);
                if (callback != null) {
                    callback.executeCallback(recipcData);
                }
            }
        };
        CallbackManager
                .getInstance()
                .addCallback(CallbackType.RECIPC_EDIT3, callback);

        RecipcData recipcData = ((RecipcEditActivity) getActivity()).getRecipcData();
        adapter.setNewInstance(recipcData.getFoodsDataList());

        allView.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(v.getContext(), RecipcFoodsActivity.class), 1);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                  if (requestCode == 1 && resultCode == 3) {
            int index = data.getIntExtra("index", -1);
            FoodsData foodsData = (FoodsData) data.getSerializableExtra("data");
            if (index == -1) {
                adapter.addData(foodsData);
            } else {
                adapter.setData(index, foodsData);
            }
        }
    }


}
