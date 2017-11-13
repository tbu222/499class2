package com.example.vpvu2.androidpizza.Model;

import java.util.List;

/**
 * Created by thinh on 10/31/2017.
 */
//constructor, getter and setter
public class Request {
    private String phone;
    private String address;
    private String name;
    private String total;
    private List<Order> orders; //list of orders
    private String status_info;
    public Request() {
    }

    public Request(String phone, String address, String name, String total, List<Order> orders) {
        this.phone = phone;
        this.address = address;
        this.name = name;
        this.total = total;
        this.orders = orders;
        this.status_info = "0"; //0 = placed, 1 = shipping, 2 = shipped
    }

    public String getStatus_info() {
        return status_info;
    }

    public void setStatus_info(String status_info) {
        this.status_info = status_info;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
