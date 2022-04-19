package com.example.recipes.data;


import cn.bmob.v3.BmobObject;

  public class RecipcType extends BmobObject {
    private String type = "1";      private String content;  
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
