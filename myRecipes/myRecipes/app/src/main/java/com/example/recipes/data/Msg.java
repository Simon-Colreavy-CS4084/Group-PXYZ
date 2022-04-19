package com.example.recipes.data;


import com.example.recipes.util.DataDispose;

import cn.bmob.v3.BmobObject;

   


public class Msg extends BmobObject {
    private String content;       private user_info otherUser ;      private user_info meUser;      private String msgListId;
    public String getShowTime() {
        return DataDispose.getDateToString(getCreatedAt(), "HH:mm");
    }

    public String getShowTimeMax() {
        return DataDispose.getDateToString(getCreatedAt(), "day:dd,month:MM");
    }



    public String getMsgListId() {
        return msgListId;
    }

    public void setMsgListId(String msgListId) {
        this.msgListId = msgListId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public user_info getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(user_info otherUser) {
        this.otherUser = otherUser;
    }

    public user_info getMeUser() {
        return meUser;
    }

    public void setMeUser(user_info meUser) {
        this.meUser = meUser;
    }
}