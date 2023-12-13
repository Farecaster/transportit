package com.example.transportit

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyHelper(context: Context) : SQLiteOpenHelper(context,"TRUCK",null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE USER(_id integer not null primary key autoincrement, USERNAME TEXT,EMAIL TEXT, PASSWORD TEXT,imageData BLOB)")
        db?.execSQL("CREATE TABLE TRUCKS(_id integer primary key autoincrement, ownerId TEXT,truckName TEXT, dimension TEXT,payload TEXT, type TEXT,price TEXT, imageData BLOB)")
        db?.execSQL("CREATE TABLE BOOKING(_id integer primary key autoincrement, requesterId TEXT,requesterUsername TEXT, ownerId TEXT, status TEXT, dateRequested TEXT, date TEXT, time TEXT, fromLocation TEXT, toLocation TEXT,goodsType TEXT,goodsWeight TEXT,message TEXT, vehicleName TEXT, vehicleId TEXT)")


    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {


    }

}