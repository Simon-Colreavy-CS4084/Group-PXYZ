package com.example.recipes.fragment.recipc;


import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.recipes.R;
import com.example.recipes.data.RecipcData;
import com.example.recipes.data.RecipcType;
import com.example.recipes.ui.recipc.RecipcEditActivity;
import com.example.recipes.util.callback.CallbackManager;
import com.example.recipes.util.callback.CallbackType;
import com.example.recipes.util.callback.IGlobalCallback;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

  public class FragmentRecipc5 extends Fragment {
    private View allView;

    private List<RecipcType> recipcTypeList = null;  
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        allView = inflater.inflate(R.layout.fragment_recipc5, container, false);
        initView();
        getRecipcTypeDataList();
        return allView;
    }

    private void initView() {
        final IGlobalCallback<RecipcData> callback = new IGlobalCallback<RecipcData>() {
            @Override
            public void executeCallback(@Nullable RecipcData recipcData) {
                String string1 = ((TextView) allView.findViewById(R.id.tv_1)).getText().toString();
                String string2 = ((TextView) allView.findViewById(R.id.tv_2)).getText().toString();
                String string3 = ((TextView) allView.findViewById(R.id.tv_3)).getText().toString();
                if (TextUtils.isEmpty(string1) && TextUtils.isEmpty(string2)  && TextUtils.isEmpty(string3)) {
                    Toast.makeText(getContext(), "Please select at least one category", Toast.LENGTH_LONG).show();
                    return;
                }
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
                .addCallback(CallbackType.RECIPC_EDIT5, callback);

        RecipcData recipcData = ((RecipcEditActivity) getActivity()).getRecipcData();
        String dish_type = "";
        String cuisine = "";
        String occasion = "";
        if (recipcData.getDish_type() != null && recipcData.getDish_type().size() > 0) {
            for (int i = 0; i < recipcData.getDish_type().size(); i++) {
                if (i == recipcData.getDish_type().size() - 1) {
                    dish_type = dish_type + recipcData.getDish_type().get(i);
                } else {
                    dish_type = dish_type + recipcData.getDish_type().get(i) + "，";
                }
            }
        }
        if (recipcData.getCuisine() != null && recipcData.getCuisine().size() > 0) {
            for (int i = 0; i < recipcData.getCuisine().size(); i++) {
                if (i == recipcData.getCuisine().size() - 1) {
                    cuisine = cuisine + recipcData.getCuisine().get(i);
                } else {
                    cuisine = cuisine + recipcData.getCuisine().get(i) + "，";
                }
            }
        }
        if (recipcData.getOccasion() != null && recipcData.getOccasion().size() > 0) {
            for (int i = 0; i < recipcData.getOccasion().size(); i++) {
                if (i == recipcData.getOccasion().size() - 1) {
                    occasion = occasion + recipcData.getOccasion().get(i);
                } else {
                    occasion = occasion + recipcData.getOccasion().get(i) + "，";
                }
            }
        }
        ((TextView) allView.findViewById(R.id.tv_1)).setText(dish_type);
        ((TextView) allView.findViewById(R.id.tv_2)).setText(cuisine);
        ((TextView) allView.findViewById(R.id.tv_3)).setText(occasion);
        allView.findViewById(R.id.tv_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMultiChoiceDialog("1");
            }
        });

        allView.findViewById(R.id.tv_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMultiChoiceDialog("2");
            }
        });

        allView.findViewById(R.id.tv_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMultiChoiceDialog("3");
            }
        });
    }


          private void getRecipcTypeDataList() {
        BmobQuery<RecipcType> query = new BmobQuery<RecipcType>();
        query.findObjects(new FindListener<RecipcType>() {
            @Override
            public void done(List<RecipcType> object, BmobException e) {
                recipcTypeList = object;
            }
        });
    }


    ArrayList<String> yourChoices = new ArrayList<>();

          private void showMultiChoiceDialog(String type) {
        if (recipcTypeList == null || recipcTypeList.size() == 0) {
            Toast.makeText(getContext(), " Failed to obtain classification data, please check the network!", Toast.LENGTH_LONG).show();
            getRecipcTypeDataList();
            return;
        }
        List<String> list = new ArrayList<>();
        for (int i = 0; i < recipcTypeList.size(); i++) {
            if (recipcTypeList.get(i).getType().equals(type)) {
                list.add(recipcTypeList.get(i).getContent());
            }
        }
        yourChoices = new ArrayList<>();
        final String[] items = new String[list.size()];
                  final boolean[] initChoiceSets = new boolean[list.size()];
        RecipcData recipcData = ((RecipcEditActivity) getActivity()).getRecipcData();
        for (int i = 0; i < list.size(); i++) {
            items[i] = list.get(i);
            initChoiceSets[i] = false;
            if (type.equals("1") && recipcData.getDish_type() != null) {
                for (String string : recipcData.getDish_type()) {
                    if (list.get(i).equals(string)) {
                        initChoiceSets[i] = true;
                        yourChoices.add(string);
                        break;
                    }
                }
            } else if (type.equals("2") && recipcData.getCuisine() != null) {
                for (String string : recipcData.getCuisine()) {
                    if (list.get(i).equals(string)) {
                        initChoiceSets[i] = true;
                        yourChoices.add(string);
                        break;
                    }
                }
            } else if (type.equals("3") && recipcData.getOccasion() != null) {
                for (String string : recipcData.getOccasion()) {
                    if (list.get(i).equals(string)) {
                        initChoiceSets[i] = true;
                        yourChoices.add(string);
                        break;
                    }
                }
            }

        }

        AlertDialog.Builder multiChoiceDialog =
                new AlertDialog.Builder(getContext());
        multiChoiceDialog.setTitle("Please select");
        multiChoiceDialog.setMultiChoiceItems(items, initChoiceSets,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (isChecked) {
                            yourChoices.add(list.get(which));
                        } else {
                            yourChoices.remove(list.get(which));
                        }
                    }
                });
        multiChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RecipcData recipcData = ((RecipcEditActivity) getActivity()).getRecipcData();
                        if (type.equals("1")) {
                            recipcData.setDish_type(yourChoices);
                        } else if (type.equals("2")) {
                            recipcData.setCuisine(yourChoices);
                        } else if (type.equals("3")) {
                            recipcData.setOccasion(yourChoices);
                        }
                        initView();
                    }
                });
        multiChoiceDialog.show();
    }
}
