package com.example.recipes.data;

import java.io.Serializable;

  public class FoodsData  implements Serializable {
    private String name;      private String num;      private String unit;

    public FoodsData(String name, String num, String unit) {
        this.name = name;
        this.num = num;
        this.unit = unit;
    }

    public FoodsData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
