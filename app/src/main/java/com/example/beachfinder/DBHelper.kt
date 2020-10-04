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
//                "_id integer primary key autoincrement," +
                "beach_id integer primary key," +
                "sido_nm text," +
                "gugun_nm text," +
                "sta_nm text," +
                "beach_wid text," +
                "beach_len text," +
                "beach_knd text," +
                "link_addr text," +
                "link_nm text," +
                "link_tel text," +
                "lat integer," +
                "lon integer);"

        Log.d("sql create >","create try ");

        db.execSQL(sql)

        Log.d("sql create >","create success ");

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql : String = "DROP TABLE if exists mytable"

        db.execSQL(sql)
        onCreate(db)
    }

}