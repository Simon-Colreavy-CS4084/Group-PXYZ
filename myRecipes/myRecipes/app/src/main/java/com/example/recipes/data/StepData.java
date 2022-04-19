package com.example.recipes.data;

import java.io.Serializable;
import java.util.List;

  public class StepData  implements Serializable {
    private String desc;      private String image;
    public StepData() {
    }

    public StepData(String desc, String image) {
        this.desc = desc;
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
