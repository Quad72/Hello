package com.example.ch17_database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBHelper(context: Context): SQLiteOpenHelper(context,"testdb2",null,1){
    //onCreate는 앱이 설치된 직후 딱 한번 호출됨
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table TODO_TB("+
        "_id integer primary key autoincrement,"+
        "todo not null,"+
        "colors)"
        )
    }
    //onUpgrade는 DB변경될때마다 호출됨
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}
