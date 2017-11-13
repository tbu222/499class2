package com.example.vpvu2.androidpizza.Model;

/**
 * Created by thinh on 10/19/2017.
 */
//constructor, getter and setter
public class Pizza {
    private String Name, Image, Description, Price, MenuId;

    public Pizza() {
    }

    public Pizza(String name, String image, String description, String price, String menuId) {
        Name = name;
        Image = image;
        Description = description;
        Price = price;
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

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }
}
