package udenconstruction.yoobie.allTools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import udenconstruction.yoobie.allModels.Image;
import udenconstruction.yoobie.allModels.Image_stamp;

public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 5;
    // Database Name
    private static final String DATABASE_NAME = "ImageDB";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOK_TABLE = "CREATE TABLE images ( " +
                "image_id INTEGER, " +
                "image_name TEXT, "+
                "renewed_times INTEGER )";

        String CREATE_nig_TABLE = "CREATE TABLE image_stamps ( " +
                "image_id INTEGER, "+
                "user_id INTEGER, "+
                "stamp TEXT, "+
                "duration REAL )";

        db.execSQL(CREATE_BOOK_TABLE);
        db.execSQL(CREATE_nig_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS images");
        db.execSQL("DROP TABLE IF EXISTS image_stamps");
        this.onCreate(db);
    }

    public void addStamp(int img_id, int mem_id, String stp, double dur){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image_id", img_id);
        values.put("user_id", mem_id);
        values.put("stamp", stp);
        values.put("duration", dur);
        db.insert("image_stamps", null, values);
        db.close();
    }

    public List<Image_stamp> getStamps() {
        List<Image_stamp> list = new ArrayList<>();
        String query = "SELECT  * FROM image_stamps";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Image_stamp tempStamp = new Image_stamp(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getFloat(3));
                list.add(tempStamp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void deleteEvery(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("image_stamps", null, null);
        db.delete("images", null, null);
        db.close();
    }

    public void clearStamp(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("image_stamps", null, null);
        db.close();
    }

    public void addImage(Image img){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image_id", img.id);
        values.put("image_name", img.image_name);
        values.put("renewed_times", img.renewed_times);
        db.insert("images", null, values);
        db.close();
    }

    public int getImage() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT image_id FROM images", null);
        int res;
        if(c.getCount() == 0){
            res = -1;
        }
        else{
            Random rand = new Random();
            int rann = rand.nextInt(c.getCount());
            c.moveToPosition(rann);
            res = c.getInt(0);
        }
        c.close();
        db.close();
        return res;
    }

    public void clearImage(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("images", null, null);
        db.close();
    }
}
