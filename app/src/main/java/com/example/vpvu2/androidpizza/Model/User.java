package com.example.vpvu2.androidpizza.Model;

/**
 * Created by thinh on 10/8/2017.
 */
//constructor, getter and setter
public class User {
    private String Name;
    private String Password;
    private String Phone_num;

    public User(){

    }

    public User(String name, String password) {
        Name = name;
        Password = password;
    }

    public String getPhone_num() {
        return Phone_num;
    }

    public void setPhone_num(String phone_num) {
        Phone_num = phone_num;
    }

    public String getName(){
        return Name;
    }

    public void setName(String name){
        Name =  name;
    }

    public void setPassword(String password){
        Password = password;
    }

    public String getPassword(){
        return Password;
    }

}
