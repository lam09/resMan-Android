package lam.fooapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import lam.fooapp.Food;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "mango";
    public final static int DATABASE_VERSION = 1;
    String TABLE_FOOD = "food";
    String COLUMN_FOOD_ID = "food_id";
    String COLUMN_FOOD_DESCRIPTION = "food_description";
    String COLUMN_FOOD_TITLE="food_title";
    String COLUMN_FOOD_TYPE="food_type";


    public MyDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
// Script to create table.
        String script = "CREATE TABLE " + TABLE_FOOD + "("
                + COLUMN_FOOD_ID + " INTEGER PRIMARY KEY," + COLUMN_FOOD_TITLE + " TEXT,"
                + COLUMN_FOOD_TYPE + " TEXT,"
                + COLUMN_FOOD_DESCRIPTION + " TEXT" + ")";
        // Execute script.
        sqLiteDatabase.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        // Recreate
        onCreate(sqLiteDatabase);
    }

    public void createDefaultFoodsIfNeed(){
        int count=this.getFoodsCount();
    }

    public Food getFood(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FOOD,new String[]{COLUMN_FOOD_ID,COLUMN_FOOD_TITLE,COLUMN_FOOD_TYPE,COLUMN_FOOD_DESCRIPTION},COLUMN_FOOD_ID+"=?",
                        new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor!=null)
            cursor.moveToFirst();
        Food food = new Food();
        food.setId(Integer.parseInt(cursor.getString(0)));
        food.setTitle(cursor.getString(1));
        food.setType(cursor.getString(2));
        food.setDescription(cursor.getString(3));
        return food;
    }

    public List<Food> getAllFoods()
    {
        List<Food> foods= new ArrayList<Food>();
        String selectQuery = "SELECT * FROM "+TABLE_FOOD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                Food food = new Food();
                food.setId(Integer.parseInt(cursor.getColumnName(0)));
                food.setTitle(cursor.getString(1));
                food.setType(cursor.getString(2));
                food.setDescription(cursor.getString(3));
                foods.add(food);
            } while (cursor.moveToNext());
        }
        return foods;
    }

    public int getFoodsCount() {
        String countQuery = "SELECT * FROM "+TABLE_FOOD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(countQuery,null);
        int count=cursor.getCount();
        cursor.close();
        return count;
    }

    public int updateFood(Food food){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COLUMN_FOOD_TITLE,food.getTitle());
        values.put(COLUMN_FOOD_DESCRIPTION,food.getDescription());
        values.put(COLUMN_FOOD_TYPE,food.getType());
        return db.update(TABLE_FOOD,values,COLUMN_FOOD_ID + "=?",
                new String[]{String.valueOf(food.getId())});
    }

    public void deleteFood(Food food)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOOD,COLUMN_FOOD_ID+"=?",
                new String[]{String.valueOf(food.getId())});
        db.close();
    }



}
