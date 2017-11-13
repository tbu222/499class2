package com.example.vpvu2.androidpizza.Model;

/**
 * Created by thinh on 10/22/2017.
 */
//Constructor, getter and setter
public class Drink {
    private String Name,
            Image,
            Price,
            Description,
            MenuId;

    public Drink() {
    }

    public Drink(String name, String image, String price, String description, String menuId) {
        Name = name;
        Image = image;
        Price = price;
        Description = description;
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

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }
}
