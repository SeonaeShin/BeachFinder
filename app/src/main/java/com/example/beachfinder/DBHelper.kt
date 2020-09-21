package com.example.beachfinder

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {


    override fun onCreate(db: SQLiteDatabase) {
        var sql : String =  "CREATE TABLE mytable (" +
                "_id integer primary key autoincrement," +
                "name text," +
                "addr text);";

        Log.d("hhhh","create try ");

        db.execSQL(sql)

        Log.d("hhhh","create success ");

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql : String = "DROP TABLE if exists mytable"

        db.execSQL(sql)
        onCreate(db)
    }

}