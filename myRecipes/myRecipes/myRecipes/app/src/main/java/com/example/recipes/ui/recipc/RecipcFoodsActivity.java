package com.example.recipes.ui.recipc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.recipes.R;
import com.example.recipes.data.FoodsData;
import com.example.recipes.ui.newsfeed.NewsFeedEditActivity;
import com.example.recipes.util.Utils;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

  public class RecipcFoodsActivity extends AppCompatActivity {

          private List<String> unitList = Arrays.asList("liter", "g", "kg", "ml", "drop", "TBSP", "TSP", "piece",
            "bottle", "box", "piece", "double", "mr");
    private FoodsData foodsData;
    private int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recipc_foods);

        ImmersionBar.with(this)
                .titleBarMarginTop(findViewById(R.id.title))
                .statusBarDarkFont(true, 0.2f)                   .keyboardEnable(true)                  .navigationBarWithKitkatEnable(false)                  .init();

        foodsData = (FoodsData) getIntent().getSerializableExtra("data");
        if (foodsData == null) {
            foodsData = new FoodsData();
        } else {
            ((EditText) findViewById(R.id.et_1)).setText(foodsData.getName());
            ((EditText) findViewById(R.id.et_2)).setText(foodsData.getNum());
            ((TextView) findViewById(R.id.et_3)).setText(foodsData.getUnit());
        }
        index = getIntent().getIntExtra("index", -1);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string1 = ((EditText) findViewById(R.id.et_1)).getText().toString();
                String string2 = ((EditText) findViewById(R.id.et_2)).getText().toString();
                String string3 = ((TextView) findViewById(R.id.et_3)).getText().toString();
                if (TextUtils.isEmpty(string1)) {
                    Toast.makeText(RecipcFoodsActivity.this, "Please enter the name of the food", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(string2)) {
                    Toast.makeText(RecipcFoodsActivity.this, "Please enter the amount of ingredients", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(string3)) {
                    Toast.makeText(RecipcFoodsActivity.this, "Please enter the consumption unit of ingredients", Toast.LENGTH_LONG).show();
                    return;
                }
                foodsData.setName(string1);
                foodsData.setNum(string2);
                foodsData.setUnit(string3);
                Intent intent = new Intent();
                intent.putExtra("index", index);
                intent.putExtra("data", foodsData);
                setResult(3, intent);
                finish();
            }
        });

        findViewById(R.id.et_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(view);
                                  OptionsPickerView pvOptions = new OptionsPickerBuilder(view.getContext(), new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        ((TextView) findViewById(R.id.et_3)).setText(unitList.get(options1));
                    }
                }).build();
                pvOptions.setPicker(unitList, null, null);
                pvOptions.show();
            }
        });

    }
}