package com.example.recipes.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.recipes.R;
import com.example.recipes.data.Comment;
import com.example.recipes.ui.UserInfoActivity;
import com.example.recipes.util.DataDispose;

import org.jetbrains.annotations.NotNull;

import java.util.List;

  public class CommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {

       
    public CommentAdapter(List<Comment> list) {
        super(R.layout.item_comment, list);
    }

       
    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull Comment item) {
        helper.setText(R.id.tv_show, item.getContent());
        helper.setText(R.id.tv_top, item.getAuthor().showName());
        ImageView iv_top = helper.getView(R.id.iv_top);


        String image = "http://";
        if (item.getAuthor().getImage() != null && !TextUtils.isEmpty(item.getAuthor().getImage().getUrl())) {
            image = item.getAuthor().getImage().getUrl();
        }
        Glide.with(iv_top).load(image)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(iv_top);

        String hint = "@" + item.getAuthor().getUser_no() + " · " + DataDispose.getIntervalTime(item.getCreatedAt());
        helper.setText(R.id.tv_top_hint, hint);


        helper.getView(R.id.rl_top).setTag(R.id.view_tag,item);
        helper.getView(R.id.rl_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment item = (Comment) v.getTag(R.id.view_tag);
                Intent intent = new Intent(v.getContext(), UserInfoActivity.class);
                intent.putExtra("user", item.getAuthor());
                v.getContext().startActivity(intent);
            }
        });

    }

}