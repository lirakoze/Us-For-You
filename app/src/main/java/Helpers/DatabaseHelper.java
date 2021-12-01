package Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context)
    {
        super(context, "UserCartDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("Create Table UserItems(id TEXT  primary key, name TEXT , url TEXT, quantity TEXT,amount TEXT, price TEXT)");
        DB.execSQL("Create Table UserFavorites(id TEXT  primary key, name TEXT , url TEXT, quantity TEXT,amount TEXT, price TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop Table if exists UserItems");
        DB.execSQL("drop Table if exists UserFavorites");
    }

    //CRUD OPERATIONS

    //Inserting in user cart
    public Boolean insertCartItem(String id, String name, String url, String quantity, String amount, String price){
        SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("id",id);
        contentValues.put("name",name);
        contentValues.put("url",url);
        contentValues.put("quantity",quantity);
        contentValues.put("amount",amount);
        contentValues.put("price",price);

        long result=DB.insert("UserItems",null,contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }

    }
    //Inserting in user favorites
    public Boolean insertFavItem(String id, String name, String url, String quantity, String amount, String price){
        SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("id",id);
        contentValues.put("name",name);
        contentValues.put("url",url);
        contentValues.put("quantity",quantity);
        contentValues.put("amount",amount);
        contentValues.put("price",price);

        long result=DB.insert("UserItems",null,contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }

    }

    public Boolean updateCartItem(String id, String name, String quantity, String amount, String price){
        SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("name",name);
        contentValues.put("quantity",quantity);
        contentValues.put("amount",amount);
        contentValues.put("price",price);

        Cursor cursor=DB.rawQuery("Select * from UserItems where id=?", new String[]{id});
        if(cursor.getCount()>0) {
            long result = DB.update("UserItems", contentValues, "id=?", new String[]{id});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else{
            return false;
        }
    }


//Deleting cart Item
    public Boolean deleteCartItem(String id){
        SQLiteDatabase DB=this.getWritableDatabase();

        Cursor cursor=DB.rawQuery("Select * from UserItems where id=?", new String[]{id});
        if(cursor.getCount()>0) {
            long result = DB.delete("UserItems", "id=?", new String[]{id});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else{
            return false;
        }
    }

   //Removing From Favorites
   public Boolean deleteFavItem(String id){
       SQLiteDatabase DB=this.getWritableDatabase();

       Cursor cursor=DB.rawQuery("Select * from UserFavorites where id=?", new String[]{id});
       if(cursor.getCount()>0) {
           long result = DB.delete("UserFavorites", "id=?", new String[]{id});
           if (result == -1) {
               return false;
           } else {
               return true;
           }
       }else{
           return false;
       }
   }

//Getting Cart Items
    public Cursor getCartItems(){
        SQLiteDatabase DB=this.getWritableDatabase();

        Cursor cursor=DB.rawQuery("Select * from UserItems", null);
        return cursor;
    }

    public  Cursor getCartItem(String id){
        SQLiteDatabase DB=this.getWritableDatabase();

        Cursor cursor=DB.rawQuery("Select * from UserItems where id=?", new String[]{id});
        return cursor;
    }

    //Getting Favorite Items
    public Cursor getFavItems(){
        SQLiteDatabase DB=this.getWritableDatabase();

        Cursor cursor=DB.rawQuery("Select * from UserFavorites", null);
        return cursor;
    }

    public  Cursor getFavItem(String id){
        SQLiteDatabase DB=this.getWritableDatabase();

        Cursor cursor=DB.rawQuery("Select * from UserFavorites where id=?", new String[]{id});
        return cursor;
    }

    public  void clearCart(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from UserItems");
        db.close();
    }
}
