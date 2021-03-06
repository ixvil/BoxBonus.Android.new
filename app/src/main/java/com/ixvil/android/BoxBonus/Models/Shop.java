package com.ixvil.android.BoxBonus.Models;

import com.google.gson.JsonArray;

import io.realm.RealmObject;

/**
 * Created by ixvil on 02.12.2016.
 */

public class Shop extends RealmObject {

    private int id;
    private String name;

    private String address;

    public static JsonArray shops;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
