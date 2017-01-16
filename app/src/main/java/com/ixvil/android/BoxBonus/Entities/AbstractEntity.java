package com.ixvil.android.BoxBonus.Entities;

import com.google.gson.JsonObject;

/**
 * Created by ixvil on 05.01.2017.
 */

abstract class AbstractEntity<E> {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    abstract public E factory(JsonObject resource);
}
