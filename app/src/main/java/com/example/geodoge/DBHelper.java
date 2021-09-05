/**
 * Name: Nathan Almeida,
 * Date Created: 05/05/2021,
 * Purpose: Insert and Check Login/Registration Data using SQLite (Create Users Table)
 */

package com.example.geodoge;

import android.content.ContentValues;import android.content.Context;import android.database.Cursor;import android.database.sqlite.SQLiteDatabase;import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    //Database file name
    public static final String DBNAME="Login.db";

    //Constructor of DBHelper Class
    public DBHelper(Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    //Create table on initial Sign Up
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(username TEXT primary key, password TEXT)");
    }

    @Override
    //If User exists when Signing Up, remove the Table Created (Do not add)
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop Table if exists users");
    }

    //Insert User information into table if contentValues are NULL (Signing Up), return true or false
    public Boolean insertData(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = MyDB.insert("users", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //Check if Username already exists (Signing Up), return true or false
    public Boolean checkUserName(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[] {username});
        if(cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    //Check if Username with the same Password already exists (Logging In), return true or false
    public Boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * from users where username = ? and password = ?", new String[] {username, password});
        if(cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
