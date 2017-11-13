package com.example.vpvu2.androidpizza.Model;

/**
 * Created by thinh on 10/22/2017.
 */
//constructor, getter and setter
public class Salad {
    private String Name;
    private String MenuId;
    private String Price;
    private String Image;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    private String Description;

    public Salad(String name, String menuId, String price, String image, String description) {
        Name = name;
        MenuId = menuId;
        Price = price;
        Image = image;
        Description = description;
    }

    public Salad() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
