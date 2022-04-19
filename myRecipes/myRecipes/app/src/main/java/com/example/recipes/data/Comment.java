package com.example.recipes.data;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

  public class Comment extends BmobObject {
    private user_info author;      private NewsFeedData newsFeed;      private RecipcData recipcData;      private String content;

    public user_info getAuthor() {
        return author;
    }

    public void setAuthor(user_info author) {
        this.author = author;
    }

    public NewsFeedData getNewsFeed() {
        return newsFeed;
    }

    public void setNewsFeed(NewsFeedData newsFeed) {
        this.newsFeed = newsFeed;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public RecipcData getRecipcData() {
        return recipcData;
    }

    public void setRecipcData(RecipcData recipcData) {
        this.recipcData = recipcData;
    }
}
