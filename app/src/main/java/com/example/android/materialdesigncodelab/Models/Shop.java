package com.example.android.materialdesigncodelab.Models;

import android.content.Context;
import android.support.test.espresso.core.deps.guava.hash.Hashing;
import android.widget.Toast;

import com.example.android.materialdesigncodelab.Fragments.HomeFragment;
import com.example.android.materialdesigncodelab.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import io.realm.RealmObject;

/**
 * Created by ixvil on 02.12.2016.
 */

public class Shop extends RealmObject {

    private int id;
    private String name;

    private String address;

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
