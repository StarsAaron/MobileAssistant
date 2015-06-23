package com.rujian.mobileassistant.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

/**
 * Created by lanlan on 2015/6/21.
 */
public class AppLcokDBOpenHelper extends SQLiteOpenHelper {

    public AppLcokDBOpenHelper(Context context){
        super(context,"applock.db",null,1);
    }
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table applock (_id integer primary key autoincrement , packname varchar(20))");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
