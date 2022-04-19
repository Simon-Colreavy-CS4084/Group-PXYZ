package com.example.recipes.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipes.R;
import com.example.recipes.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

import cc.shinichi.library.ImagePreview;
import cn.jzvd.JzvdStd;


   
public class SelImagsAdapter extends
        RecyclerView.Adapter<SelImagsAdapter.ViewHolder> {
    public static final String TAG = "PictureSelector";
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;
    private final LayoutInflater mInflater;
    private List<String> list = new ArrayList<>();
    private int selectMax = 9;
       
    private final onAddPicClickListener mOnAddPicClickListener;

    public interface onAddPicClickListener {
        void onAddPicClick();
    }

       
    public void delete(int position) {
        try {

            if (position != RecyclerView.NO_POSITION && list.size() > position) {
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SelImagsAdapter(Context context, onAddPicClickListener mOnAddPicClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mOnAddPicClickListener = mOnAddPicClickListener;
    }

    public void setSelectMax(int selectMax) {
        this.selectMax = selectMax;
    }

    public void setList(List<String> list) {
        if (list==null){
            list = new ArrayList<>();
        }
        this.list = list;
        notifyDataSetChanged();
    }

    public void addLists(List<String> list) {
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public List<String> getData() {
        return list == null ? new ArrayList<>() : list;
    }

    public void remove(int position) {
        if (list != null && position < list.size()) {
            list.remove(position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImg;
        ImageView mIvDel;
        ImageView mivVoide;

        public ViewHolder(View view) {
            super(view);
            mImg = view.findViewById(R.id.fiv);
            mIvDel = view.findViewById(R.id.iv_del);
            mivVoide = view.findViewById(R.id.iv_voide);
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() < selectMax) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

       
    @Override
    public SelImagsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_sel_image, viewGroup, false);
        return new SelImagsAdapter.ViewHolder(view);
    }

    private boolean isShowAddItem(int position) {
        int size = list.size();
        return position == size;
    }

       
    @Override
    public void onBindViewHolder(final SelImagsAdapter.ViewHolder viewHolder, final int position) {

        if (getItemViewType(position) == TYPE_CAMERA) {
            viewHolder.mImg.setImageResource(R.drawable.ic_add_image);
            viewHolder.mImg.setOnClickListener(v -> mOnAddPicClickListener.onAddPicClick());
            viewHolder.mIvDel.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.mIvDel.setVisibility(View.VISIBLE);
            viewHolder.mIvDel.setOnClickListener(view -> {
                int index = viewHolder.getAbsoluteAdapterPosition();
                if (index != RecyclerView.NO_POSITION && list.size() > index) {
                    list.remove(index);
                    notifyItemRemoved(index);
                    notifyItemRangeChanged(index, list.size());
                }
            });
            String path = list.get(position);
            if (FileUtils.getFileType(path) == 1) {
                viewHolder.mivVoide.setVisibility(View.GONE);
            } else {
                viewHolder.mivVoide.setVisibility(View.VISIBLE);
            }

            Glide.with(viewHolder.itemView.getContext())
                    .load(path)
                    .centerCrop()
                    .placeholder(R.color.app_color_f6)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.mImg);

            viewHolder.itemView.setTag(R.id.view_tag, path);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String path = (String) view.getTag(R.id.view_tag);
                    if (FileUtils.getFileType(path) == 2) {
                        JzvdStd.startFullscreenDirectly(view.getContext(), JzvdStd.class, path, "");
                        return;
                    }
                    ImagePreview
                            .getInstance()
                            .setContext(view.getContext())
                            .setIndex(0)
                            .setEnableDragClose(true)
                            .setShowDownButton(false)
                            .setImage(path)
                            .start();
                }
            });
        }
    }

}
