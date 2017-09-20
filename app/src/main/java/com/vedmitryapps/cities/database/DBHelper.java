package com.vedmitryapps.cities.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "countriesToCities.db";
    private static final String TABLE_COUNTRIES = "countries";
    private static final int DATABASE_VERSION = 1;

    private static final String KEY_ID = "id";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_CITIES = "cities";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_COUNTRIES + "("
                + KEY_ID + " integer primary key autoincrement,"
                + KEY_COUNTRY + " text,"
                + KEY_CITIES + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void fillDB(Map<String, List<String>> map) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Gson gson = new Gson();

        for (String country : map.keySet()
                ) {
            if (country.equals("")) {
                continue;
            }
            values.put(KEY_COUNTRY, country);
            values.put(KEY_CITIES, gson.toJson(map.get(country)));
            db.insert(TABLE_COUNTRIES, null, values);
        }
        db.close();

    }

    public Map<String, List<String>> getDate() {
        Map<String, List<String>> map = new HashMap<>();

        String query = "SELECT * FROM " + TABLE_COUNTRIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        String country;
        List<String> cities;
        Gson gson = new Gson();
        Type itemsArrType = new TypeToken<List<String>>() {}.getType();

        if (cursor.moveToFirst()) {
            do {
                country = cursor.getString(1);
                cities = gson.fromJson(cursor.getString(2), itemsArrType);
                map.put(country, cities);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return map;
    }

    public void clearDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COUNTRIES, null, null);
    }
}
