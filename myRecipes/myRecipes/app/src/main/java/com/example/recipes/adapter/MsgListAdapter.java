package com.example.recipes.adapter;


import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipes.R;
import com.example.recipes.data.Msg;
import com.example.recipes.data.user_info;
import com.example.recipes.ui.chat.MyChatActivity;
import com.example.recipes.util.LattePreference;

import java.util.List;


   

public class MsgListAdapter extends RecyclerView.Adapter<MsgListAdapter.ViewHolder> {
    private List<Msg> mMsgList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivShow;
        TextView tvShow,tv_hint,tv_time;
        RelativeLayout llShow;


        public ViewHolder(View itemView) {
            super(itemView);
            llShow = (RelativeLayout) itemView.findViewById(R.id.ll_show);
            ivShow = (ImageView) itemView.findViewById(R.id.iv_show);
            tvShow = (TextView) itemView.findViewById(R.id.tv_show);
            tv_hint = (TextView) itemView.findViewById(R.id.tv_hint);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);

        }
    }

    public MsgListAdapter(List<Msg> mMsgList) {
        this.mMsgList = mMsgList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Msg msg = mMsgList.get(position);
        user_info userInfo2 = LattePreference.getAppUserInfo();          String id1 = msg.getMeUser().getObjectId();
        String id2 = userInfo2.getObjectId();

        holder.tv_hint.setText( msg.getContent());
        holder.tv_time.setText( msg.getShowTime());

        String image = "http://";
        if (msg.getMeUser().getObjectId().equals(userInfo2.getObjectId())) {

            if (msg.getOtherUser().getImage()!=null && !TextUtils.isEmpty(msg.getOtherUser().getImage().getUrl())){
                image = msg.getOtherUser().getImage().getUrl();
            }

            String name = msg.getOtherUser().showName();
            holder.tvShow.setText( name);
            String finalName = name;
            holder.llShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MyChatActivity.class);
                    intent.putExtra("userData", msg.getOtherUser());
                    intent.putExtra("otherId", msg.getOtherUser().getObjectId());
                    intent.putExtra("name", finalName);
                    v.getContext().startActivity(intent);
                }
            });
        } else {
            if (msg.getMeUser().getImage()!=null && !TextUtils.isEmpty(msg.getMeUser().getImage().getUrl())){
                image = msg.getMeUser().getImage().getUrl();
            }
            String name = msg.getMeUser().showName();
            holder.tvShow.setText(name);
            String finalName = name;
            holder.llShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MyChatActivity.class);
                    intent.putExtra("userData", msg.getMeUser());
                    intent.putExtra("otherId", msg.getMeUser().getObjectId());
                    intent.putExtra("name", finalName);
                    v.getContext().startActivity(intent);

                }
            });
        }



        Glide.with(holder.ivShow).load(image)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.ivShow);


    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }

}