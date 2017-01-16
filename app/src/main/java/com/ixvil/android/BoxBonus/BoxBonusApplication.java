package com.ixvil.android.BoxBonus;

import android.app.Application;
import android.content.Context;

/**
 * Created by ixvil on 02.12.2016.
 */

public class BoxBonusApplication extends Application {

    Context getCurrentContext(){
        return getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();



       // Realm.init(this);
    }

}
