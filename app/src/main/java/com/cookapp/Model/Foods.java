package com.cookapp.Model;

public class Foods {
    private String Name;
    private String Image;
    private String Description;
    private String Calories;
    private String Time;
    private String MenuId;

    public Foods() {
    }

    public Foods(String name, String image, String description, String calories, String time, String menuId) {
        Name = name;
        Image = image;
        Description = description;
        Calories = calories;
        Time = time;
        MenuId = menuId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCalories() {
        return Calories;
    }

    public void setCalories(String calories) {
        Calories = calories;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }
}
