package com.example.recipes.ui.recipc;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipes.R;
import com.example.recipes.adapter.GirdDropDownAdapter;
import com.example.recipes.adapter.NewsFeedAdapter;
import com.example.recipes.adapter.RecipcAdapter;
import com.example.recipes.data.NewsFeedData;
import com.example.recipes.data.RecipcData;
import com.example.recipes.data.RecipcType;
import com.example.recipes.util.Utils;
import com.example.recipes.view.FullyGridLayoutManager;
import com.gyf.immersionbar.ImmersionBar;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.tools.ScreenUtils;
import com.yyydjk.library.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

  public class RecipcSearchResultActivity extends AppCompatActivity {
    private RecipcAdapter recipcAdapter;
    private String headers[] = {"dish_type", "cuisine", "occasion"};
    private List<View> popupViews = new ArrayList<>();
    private List<String> dishTypes = new ArrayList<>();
    private List<String> cuisines = new ArrayList<>();
    private List<String> occasions = new ArrayList<>();
    private GirdDropDownAdapter dishTypeAdapter = null;
    private GirdDropDownAdapter cuisineAdapter = null;
    private GirdDropDownAdapter occasionAdapter = null;

    private int index = 0;
    private int indexType = 0;

    private String data = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recipc_search_result);

        ImmersionBar.with(this)
                .titleBarMarginTop(findViewById(R.id.title))
                .statusBarDarkFont(true, 0.2f)                   .keyboardEnable(true)                  .navigationBarWithKitkatEnable(false)                  .init();

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        data = getIntent().getStringExtra("data");
        if (TextUtils.isEmpty(data)) {
            data = "";
        }


          getRecipcTypeDataList();
        Utils.hideKeyboard(findViewById(R.id.iv_back));
    }

          private void getRecipcTypeDataList() {
        BmobQuery<RecipcType> query = new BmobQuery<RecipcType>();
        query.findObjects(new FindListener<RecipcType>() {
            @Override
            public void done(List<RecipcType> object, BmobException e) {
                initDropDownMenu(object);
                getData();
            }
        });
    }

    private void initDropDownMenu(List<RecipcType> recipcTypeList) {
        DropDownMenu mDropDownMenu = findViewById(R.id.dropDownMenu);
        if (recipcTypeList == null || recipcTypeList.size() == 0) {
            mDropDownMenu.setVisibility(View.INVISIBLE);
        } else {
            mDropDownMenu.setVisibility(View.VISIBLE);

            initRecipcTypeDatas(recipcTypeList);
                          final ListView dishTypeView = new ListView(this);
            dishTypeAdapter = new GirdDropDownAdapter(this, dishTypes);
            dishTypeView.setDividerHeight(0);
            dishTypeView.setAdapter(dishTypeAdapter);

                          final ListView cuisineView = new ListView(this);
            cuisineAdapter = new GirdDropDownAdapter(this, cuisines);
            cuisineView.setDividerHeight(0);
            cuisineView.setAdapter(cuisineAdapter);

                          final ListView occasionView = new ListView(this);
            occasionAdapter = new GirdDropDownAdapter(this, occasions);
            occasionView.setDividerHeight(0);
            occasionView.setAdapter(occasionAdapter);

            if (indexType == 1) {
                dishTypeAdapter.setCheckItem(index);
            }
            if (indexType == 2) {
                cuisineAdapter.setCheckItem(index);
            }
            if (indexType == 3) {
                occasionAdapter.setCheckItem(index);
            }

                          popupViews.add(dishTypeView);
            popupViews.add(cuisineView);
            popupViews.add(occasionView);

                          dishTypeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dishTypeAdapter.setCheckItem(position);
                    mDropDownMenu.setTabText(position == 0 ? headers[0] : dishTypes.get(position));
                    mDropDownMenu.closeMenu();
                    getData();
                }
            });

            cuisineView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cuisineAdapter.setCheckItem(position);
                    mDropDownMenu.setTabText(position == 0 ? headers[1] : cuisines.get(position));
                    mDropDownMenu.closeMenu();
                    getData();
                }
            });

            occasionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    occasionAdapter.setCheckItem(position);
                    mDropDownMenu.setTabText(position == 0 ? headers[2] : occasions.get(position));
                    mDropDownMenu.closeMenu();
                    getData();
                }
            });




            RecyclerView recyclerView = new RecyclerView(this);
              recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
              FullyGridLayoutManager manager = new FullyGridLayoutManager(this,
                    2, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(manager);

            recyclerView.addItemDecoration(new GridSpacingItemDecoration(4,
                    ScreenUtils.dip2px(this, 8), false));


            recipcAdapter = new RecipcAdapter(null);
            recyclerView.setAdapter(recipcAdapter);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(15, 0, 15, 0);
            recyclerView.setLayoutParams(layoutParams);

                          mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, recyclerView);
            String label = getIntent().getStringExtra("label");
            if (!TextUtils.isEmpty(label) && indexType != 0) {
                mDropDownMenu.setTabText((indexType - 1)*2, label);
            }
        }

    }

    private void initRecipcTypeDatas(List<RecipcType> list) {
        String label = getIntent().getStringExtra("label");
        if (TextUtils.isEmpty(label)) {
            label = "";
        }
        dishTypes.add("no limit");
        cuisines.add("no limit");
        occasions.add("no limit");
        for (int i = 0; i < list.size(); i++) {
            RecipcType recipcType = list.get(i);
            if (recipcType.getType().equals("1")) {
                dishTypes.add(recipcType.getContent());
            } else if (recipcType.getType().equals("2")) {
                cuisines.add(recipcType.getContent());
            } else if (recipcType.getType().equals("3")) {
                occasions.add(recipcType.getContent());
            }
        }

        for (int i = 0; i < dishTypes.size(); i++) {
            if (label.equals(dishTypes.get(i))) {
                indexType = 1;
                index = i;
            }
        }

        for (int i = 0; i < cuisines.size(); i++) {
            if (label.equals(cuisines.get(i))) {
                indexType = 2;
                index = i;
            }
        }
        for (int i = 0; i < occasions.size(); i++) {
            if (label.equals(occasions.get(i))) {
                indexType = 3;
                index = i;
            }
        }
    }

    private void getData() {
        BmobQuery<RecipcData> query = new BmobQuery<RecipcData>();
        query.include("author");
        query.order("-createdAt");
        query.findObjects(new FindListener<RecipcData>() {
            @Override
            public void done(List<RecipcData> object1, BmobException e) {
                List<RecipcData> object = new ArrayList<>();
                if (object1 != null) {
                    for (RecipcData recipcData : object1) {
                          
                        if ((recipcData.getContent().contains(data) || recipcData.getTitle().contains(data))
                                && recipcData.showLabel().contains(dishTypeAdapter.getCheckItem())
                                && recipcData.showLabel().contains(cuisineAdapter.getCheckItem())
                                && recipcData.showLabel().contains(occasionAdapter.getCheckItem())) {
                            object.add(recipcData);
                        }
                    }
                }
                recipcAdapter.setNewInstance(object);
                if (object == null || object.size() == 0) {
                    findViewById(R.id.ll_error).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.ll_error).setVisibility(View.GONE);
                }
            }
        });

    }
}