package com.example.restadmin;

import com.google.firebase.database.Exclude;

public class ModelClass {
   private String FoodName, FoodPrice, FoodType, FoodDescription, imageUrl;
    private String Key;

    @Exclude
    public String getKey() {
        return Key;
    }

    @Exclude
    public void setKey(String key) {
        Key = key;
    }

    public ModelClass() {
    }

    public ModelClass(String foodName, String foodPrice, String foodType, String foodDescription, String imageUrl) {
        FoodName = foodName;
        FoodPrice = foodPrice;
        FoodType = foodType;
        FoodDescription = foodDescription;
        this.imageUrl = imageUrl;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getFoodPrice() {
        return FoodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        FoodPrice = foodPrice;
    }

    public String getFoodType() {
        return FoodType;
    }

    public void setFoodType(String foodType) {
        FoodType = foodType;
    }

    public String getFoodDescription() {
        return FoodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        FoodDescription = foodDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
