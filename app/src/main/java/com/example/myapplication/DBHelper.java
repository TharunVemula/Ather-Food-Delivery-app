package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    String table_name="cart";
    String col1="id";
    String col2="name";
    String col3="price";

    public DBHelper(@Nullable Context context) {
        super(context, "mydb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String s="CREATE TABLE " + table_name + " ("
                + col1 + " TEXT,"
                + col2 + " TEXT,"
                + col3 +" TEXT)";
        db.execSQL(s);
    }

    public void addNewCourse(String id, String name, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(col1, id);
        values.put(col2, name);
        values.put(col3, price);
        db.insert(table_name, null, values);
        db.close();
    }

    public List<Item> getAllContacts() {
        List<Item> list = new ArrayList<Item>();
        String selectQuery = "SELECT  * FROM " + table_name;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Item contact = new Item();
                contact.setPrice(cursor.getString(2).toString());
                contact.setName(cursor.getString(1).toString());
                list.add(contact);
            } while (cursor.moveToNext());
        }
        return list;
    }

    void clearCart()
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(sqLiteDatabase);
    }
}
