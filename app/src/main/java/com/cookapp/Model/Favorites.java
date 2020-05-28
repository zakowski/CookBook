package com.cookapp.Model;

public class Favorites {
    private String FoodId;
    private String FoodNrTel;
    private String FoodCalories;
    private String FoodName;
    private String FoodTime;

    public Favorites() {
    }

    public Favorites(String foodId, String foodNrTel,String foodName, String foodCalories,  String foodTime) {
        FoodId = foodId;
        FoodNrTel = foodNrTel;
        FoodName = foodName;
        FoodCalories = foodCalories;
        FoodTime = foodTime;
    }

    public String getFoodId() {
        return FoodId;
    }

    public void setFoodId(String foodId) {
        FoodId = foodId;
    }

    public String getFoodNrTel() {
        return FoodNrTel;
    }

    public void setFoodNrTel(String foodNrTel) {
        FoodNrTel = foodNrTel;
    }

    public String getFoodCalories() {
        return FoodCalories;
    }

    public void setFoodCalories(String foodCalories) {
        FoodCalories = foodCalories;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getFoodTime() {
        return FoodTime;
    }

    public void setFoodTime(String foodTime) {
        FoodTime = foodTime;
    }
}