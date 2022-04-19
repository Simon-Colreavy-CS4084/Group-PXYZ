package com.example.recipes.data;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

  public class RecipcData extends BmobObject {
    private user_info author;      private String title;      private String content;      private String img;      private String cookingTime;      private String placementTime;      private String bakingTime;      private List<FoodsData> foodsDataList;      private List<StepData> stepDataList;      private List<String> dish_type;      private List<String> cuisine;      private List<String> occasion;      private String likess;      private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getPlacementTime() {
        return placementTime;
    }

    public void setPlacementTime(String placementTime) {
        this.placementTime = placementTime;
    }

    public String getBakingTime() {
        return bakingTime;
    }

    public void setBakingTime(String bakingTime) {
        this.bakingTime = bakingTime;
    }

    public List<FoodsData> getFoodsDataList() {
        return foodsDataList;
    }

    public void setFoodsDataList(List<FoodsData> foodsDataList) {
        this.foodsDataList = foodsDataList;
    }

    public List<StepData> getStepDataList() {
        return stepDataList;
    }

    public void setStepDataList(List<StepData> stepDataList) {
        this.stepDataList = stepDataList;
    }

    public List<String> getDish_type() {
        return dish_type;
    }

    public void setDish_type(List<String> dish_type) {
        this.dish_type = dish_type;
    }

    public List<String> getCuisine() {
        return cuisine;
    }

    public void setCuisine(List<String> cuisine) {
        this.cuisine = cuisine;
    }

    public List<String> getOccasion() {
        return occasion;
    }

    public void setOccasion(List<String> occasion) {
        this.occasion = occasion;
    }

    public String getLikess() {
        return likess;
    }

    public void setLikess(String likess) {
        this.likess = likess;
    }

    public String showLabel() {
        List<String> stringList = new ArrayList<>();
        if (getDish_type() != null && getDish_type().size() > 0) {
            stringList.addAll(getDish_type());
        }
        if (getCuisine() != null && getCuisine().size() > 0) {
            stringList.addAll(getCuisine());
        }
        if (getOccasion() != null && getOccasion().size() > 0) {
            stringList.addAll(getOccasion());
        }
        String showLabel = "";
        if (stringList != null && stringList.size() > 0) {
            for (int i = 0; i < stringList.size(); i++) {
                if (i == stringList.size() - 1) {
                    showLabel = showLabel + stringList.get(i);
                } else {
                    showLabel = showLabel + stringList.get(i) + "，";
                }
            }
        }
        return showLabel;
    }

    public List<String> getLabelList() {
        List<String> stringList = new ArrayList<>();
        if (getDish_type() != null && getDish_type().size() > 0) {
            stringList.addAll(getDish_type());
        }
        if (getCuisine() != null && getCuisine().size() > 0) {
            stringList.addAll(getCuisine());
        }
        if (getOccasion() != null && getOccasion().size() > 0) {
            stringList.addAll(getOccasion());
        }
        if (getAddress() != null) {
            stringList.add(address);
        }
        return stringList;
    }
}
