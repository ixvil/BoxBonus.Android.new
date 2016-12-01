package com.example.android.materialdesigncodelab;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by ixvil on 02.12.2016.
 */

public class BoxBonusApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

}
