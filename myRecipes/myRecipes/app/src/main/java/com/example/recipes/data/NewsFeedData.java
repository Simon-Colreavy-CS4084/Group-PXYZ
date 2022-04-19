package com.example.recipes.data;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

  public class NewsFeedData extends BmobObject {
    private user_info author;      private String title;      private String content;      private String likess;      private List<String> imgs;        private BmobRelation likes;      private BmobRelation comments;

    public user_info getAuthor() {
        return author;
    }

    public void setAuthor(user_info author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }


    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }

    public BmobRelation getComments() {
        return comments;
    }

    public void setComments(BmobRelation comments) {
        this.comments = comments;
    }

    public String getLikess() {
        return likess;
    }

    public void setLikess(String likess) {
        this.likess = likess;
    }
}
