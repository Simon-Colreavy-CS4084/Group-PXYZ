package com.example.recipes.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.recipes.R;
import com.example.recipes.data.Msg;
import com.example.recipes.data.NewsFeedData;
import com.example.recipes.data.user_info;
import com.example.recipes.util.DataDispose;
import com.example.recipes.util.LattePreference;

import org.jetbrains.annotations.NotNull;

import java.util.List;


   

public class MsgAdapter extends BaseQuickAdapter<Msg, BaseViewHolder> {

       
    public MsgAdapter(List<Msg> list) {
        super(R.layout.item_msg_layout, list);
    }

       
    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull Msg msg) {
        LinearLayout  leftLayout = (LinearLayout) helper.getView(R.id.left_layout);
        LinearLayout  rightLayout = (LinearLayout) helper.getView(R.id.right_layout);
        TextView leftMsg = (TextView) helper.getView(R.id.left_msg);
        TextView rightMsg = (TextView) helper.getView(R.id.right_msg);
        TextView leftTime = (TextView) helper.getView(R.id.left_time);
        TextView  rightTime = (TextView) helper.getView(R.id.right_time);
        TextView  tv_time = (TextView) helper.getView(R.id.tv_time);

        user_info userInfo2 = LattePreference.getAppUserInfo();          leftTime.setText(msg.getShowTime());
        rightTime.setText(msg.getShowTime());

        tv_time.setVisibility(View.GONE);
        tv_time.setText(msg.getShowTimeMax());
        if (getData().size() > helper.getPosition()) {
            if (helper.getPosition() != 0 && !DataDispose.isSameDay(msg.getCreatedAt(), getData().get(helper.getPosition() - 1).getCreatedAt())) {
                tv_time.setVisibility(View.VISIBLE);
            }
        }
        if (helper.getPosition() == 0) {
            tv_time.setVisibility(View.VISIBLE);
        }

        if (!msg.getMeUser().getObjectId().equals(userInfo2.getObjectId())) {
                          leftLayout.setVisibility(View.VISIBLE);
            leftTime.setVisibility(View.VISIBLE);
            rightLayout.setVisibility(View.GONE);
            rightTime.setVisibility(View.GONE);
            leftMsg.setText(msg.getContent());
        } else {
                          rightLayout.setVisibility(View.VISIBLE);
            rightTime.setVisibility(View.VISIBLE);
            leftLayout.setVisibility(View.GONE);
            leftTime.setVisibility(View.GONE);
            rightMsg.setText(msg.getContent());
        }

    }




}