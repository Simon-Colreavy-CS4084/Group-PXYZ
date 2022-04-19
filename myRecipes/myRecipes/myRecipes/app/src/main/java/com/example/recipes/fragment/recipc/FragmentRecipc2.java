package com.example.recipes.fragment.recipc;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.recipes.R;
import com.example.recipes.data.RecipcData;
import com.example.recipes.ui.recipc.RecipcEditActivity;
import com.example.recipes.util.callback.CallbackManager;
import com.example.recipes.util.callback.CallbackType;
import com.example.recipes.util.callback.IGlobalCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

  public class FragmentRecipc2 extends Fragment {
    private View allView;
    private String cookingTime = "0";      private String placementTime = "0";      private String bakingTime = "0";  

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        allView = inflater.inflate(R.layout.fragment_recipc2, container, false);
        initView();
        return allView;
    }

    private void initView() {
        final IGlobalCallback<RecipcData> callback = new IGlobalCallback<RecipcData>() {
            @Override
            public void executeCallback(@Nullable RecipcData recipcData) {
                cookingTime = ((EditText) allView.findViewById(R.id.tv_cookingTime)).getText().toString();
                placementTime = ((EditText) allView.findViewById(R.id.tv_placementTime)).getText().toString();
                bakingTime = ((EditText) allView.findViewById(R.id.tv_bakingTime)).getText().toString();
                if (TextUtils.isEmpty(cookingTime)) {
                    cookingTime = "0";
                }
                if (TextUtils.isEmpty(placementTime)) {
                    placementTime = "0";
                }
                if (TextUtils.isEmpty(bakingTime)) {
                    bakingTime = "0";
                }
                if (Integer.valueOf(cookingTime) <= 0 && Integer.valueOf(placementTime) <= 0
                        && Integer.valueOf(bakingTime) <= 0) {
                    Toast.makeText(getContext(), "At least one has time", Toast.LENGTH_LONG).show();
                    return;
                }
                recipcData.setCookingTime(cookingTime);
                recipcData.setPlacementTime(placementTime);
                recipcData.setBakingTime(bakingTime);
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
                .addCallback(CallbackType.RECIPC_EDIT2, callback);

        RecipcData recipcData = ((RecipcEditActivity) getActivity()).getRecipcData();
        if (!TextUtils.isEmpty(recipcData.getCookingTime())) {
            cookingTime = recipcData.getCookingTime();
        }
        if (!TextUtils.isEmpty(recipcData.getPlacementTime())) {
            placementTime = recipcData.getPlacementTime();
        }
        if (!TextUtils.isEmpty(recipcData.getBakingTime())) {
            bakingTime = recipcData.getBakingTime();
        }
        ((EditText) allView.findViewById(R.id.tv_cookingTime)).setText(cookingTime);
        ((EditText) allView.findViewById(R.id.tv_placementTime)).setText(placementTime);
        ((EditText) allView.findViewById(R.id.tv_bakingTime)).setText(bakingTime);
    }


}
