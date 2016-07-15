package com.example.hp.pumpkin2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by hp on 2016/2/6.
 */
public class MySqliteHelper  extends SQLiteOpenHelper {

    public MySqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists my_table(_id integer primary key autoincrement,task text,date text,task_type text,cycle_time text,remark text,state text,done_time text)");

    }


   /* state:"todo" --未做
          "done" --已完成*/


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}
