package com.example.recipes.data;

import android.text.TextUtils;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

  public class FollowData extends BmobObject {
    private user_info meUser;      private user_info followUser;
    public user_info getMeUser() {
        return meUser;
    }

    public void setMeUser(user_info meUser) {
        this.meUser = meUser;
    }

    public user_info getFollowUser() {
        return followUser;
    }

    public void setFollowUser(user_info followUser) {
        this.followUser = followUser;
    }
}
