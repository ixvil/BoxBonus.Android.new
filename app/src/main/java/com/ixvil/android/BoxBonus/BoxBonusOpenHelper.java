package com.ixvil.android.BoxBonus;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ixvil on 02.12.2016.
 */

public class BoxBonusOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String SHOPS_TABLE_NAME = "shops";
    private static final String SHOPS_TABLE_CREATE =
            "CREATE TABLE " + SHOPS_TABLE_NAME + " (" +
                    "NAME  TEXT, " +
                    "ADDRESS TEXT);";

    public BoxBonusOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SHOPS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
