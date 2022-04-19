package com.example.recipes.fragment.recipc;


import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.recipes.R;
import com.example.recipes.adapter.RecipcFoodsAdapter;
import com.example.recipes.adapter.RecipcStepAdapter;
import com.example.recipes.data.FoodsData;
import com.example.recipes.data.RecipcData;
import com.example.recipes.data.StepData;
import com.example.recipes.ui.recipc.RecipcEditActivity;
import com.example.recipes.ui.recipc.RecipcStepActivity;
import com.example.recipes.util.callback.CallbackManager;
import com.example.recipes.util.callback.CallbackType;
import com.example.recipes.util.callback.IGlobalCallback;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.tools.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

  public class FragmentRecipc4 extends Fragment {
    private View allView;
    private RecipcStepAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        allView = inflater.inflate(R.layout.fragment_recipc4, container, false);

        initRecyclerView();

        initView();
        return allView;
    }

    private void initRecyclerView(){
                  OnItemDragListener listener = new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                final BaseViewHolder holder = ((BaseViewHolder) viewHolder);

                                  int startColor = Color.WHITE;
                int endColor = Color.rgb(245, 245, 245);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ValueAnimator v = ValueAnimator.ofArgb(startColor, endColor);
                    v.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            holder.itemView.setBackgroundColor((int)animation.getAnimatedValue());
                        }
                    });
                    v.setDuration(300);
                    v.start();
                }
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                final BaseViewHolder holder = ((BaseViewHolder) viewHolder);
                                  int startColor = Color.rgb(245, 245, 245);
                int endColor = Color.WHITE;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ValueAnimator v = ValueAnimator.ofArgb(startColor, endColor);
                    v.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            holder.itemView.setBackgroundColor((int)animation.getAnimatedValue());
                        }
                    });
                    v.setDuration(300);
                    v.start();
                }
                adapter.notifyDataSetChanged();
            }
        };


        RecyclerView recyclerView = allView.findViewById(R.id.rvShow);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecipcStepAdapter(null, this);
        adapter.getDraggableModule().setDragEnabled(true);
        adapter.getDraggableModule().setOnItemDragListener(listener);
        adapter.getDraggableModule().setDragOnLongPressEnabled(false);
        adapter.getDraggableModule().setToggleViewId(R.id.iv_draggable);

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4,
                ScreenUtils.dip2px(getContext(), 8), false));

        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        final IGlobalCallback<RecipcData> callback = new IGlobalCallback<RecipcData>() {
            @Override
            public void executeCallback(@Nullable RecipcData recipcData) {
                if (adapter.getData().size() == 0) {
                    Toast.makeText(getContext(), "Please add at least one step", Toast.LENGTH_LONG).show();
                    return;
                }
                recipcData.setStepDataList(adapter.getData());
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
                .addCallback(CallbackType.RECIPC_EDIT4, callback);

        RecipcData recipcData = ((RecipcEditActivity) getActivity()).getRecipcData();
          

        adapter.setNewInstance(recipcData.getStepDataList());

        allView.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(v.getContext(), RecipcStepActivity.class), 1);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                  if (requestCode == 1 && resultCode == 3) {
            int index = data.getIntExtra("index", -1);
            StepData stepData = (StepData) data.getSerializableExtra("data");
            if (index == -1) {
                adapter.addData(stepData);
            } else {
                adapter.setData(index, stepData);
            }
        }
    }


}
