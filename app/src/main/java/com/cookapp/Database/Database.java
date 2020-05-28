package com.cookapp.Database;

import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.cookapp.Model.Favorites;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME ="CookBook.db";
    private static final int DB_VER=1;

    public Database(Context context){ super(context,DB_NAME,null,DB_VER); }

//    //Dodawanie do ulubionych
//    public void addFavorite(String foodId, String UserNrTel)
//    {
//        SQLiteDatabase db = getReadableDatabase();
//        String query = String.format("INSERT INTO Favorites(FoodId,UserNrTel) VALUES('%s','%s');",foodId,UserNrTel);
//        db.execSQL(query);
//    }

    //Usuwanie z ulubionych
    public void removeFavorite(String foodId,String UserNrTel){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE FoodId='%s' and UserNrTel='%s';",foodId,UserNrTel);
        db.execSQL(query);
    }

    //Sprawdzanie ulubionych
    public boolean isFavorite(String foodId,String UserNrTel){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorites WHERE FoodId='%s' and UserNrTel='%s';",foodId,UserNrTel);
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.getCount()<=0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    public List<Favorites> getFavorites(String userNrTel){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"FoodId","UserNrTel","FoodName","FoodCalories","FoodTime"};
        String sqlTable = "Favorites";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,"UserNrTel=?",new String[]{userNrTel},null,null,null);

        final List<Favorites> result = new ArrayList<>();
        if (c.moveToFirst()){
            do {
                result.add(new Favorites(c.getString(c.getColumnIndex("FoodId")),
                        c.getString(c.getColumnIndex("UserNrTel")),
                        c.getString(c.getColumnIndex("FoodName")),
                        c.getString(c.getColumnIndex("FoodCalories")),
                        c.getString(c.getColumnIndex("FoodTime"))
                ));
            }while (c.moveToNext());
        }
        return result;
    }


    //Dodawanie do ulubionych
    public void addToFavorite(Favorites food)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favorites(FoodId,UserNrTel,FoodName,FoodCalories,FoodTime) VALUES('%s','%s','%s','%s','%s');",
                food.getFoodId(),
                food.getFoodNrTel(),
                food.getFoodName(),
                food.getFoodCalories(),
                food.getFoodTime());

        db.execSQL(query);
    }

//    //Usuwanie z ulubionych
//    public void removeFavorite()
//    {
//        SQLiteDatabase db = getReadableDatabase();
//        String query = String.format("DELETE FROM Favorites");
//        db.execSQL(query);
//    }
//






}
