package com.example.android.materialdesigncodelab;

import android.app.Application;
import android.content.Intent;

import com.example.android.materialdesigncodelab.Activities.LoginActivity;
import com.example.android.materialdesigncodelab.Activities.MainActivity;
import com.example.android.materialdesigncodelab.Models.User;

import io.realm.Realm;

/**
 * Created by ixvil on 02.12.2016.
 */

public class BoxBonusApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();



       // Realm.init(this);
    }

}
