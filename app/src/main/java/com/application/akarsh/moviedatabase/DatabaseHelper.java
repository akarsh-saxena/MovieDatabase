package com.application.akarsh.moviedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akarsh on 17-06-2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favorites.db";
    public static final String TABLE_NAME_MOVIES = "movies_table";
    public static final String TABLE_NAME_TV = "movies_tv";
    public static final String COLUMN_ITEM_ID = "item_id";
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_ITEM_VOTE_AVERAGE = "item_avg";
    public static final String COLUMN_ITEM_VOTE_COUNT = "item_count";
    public static final String COLUMN_ITEM_RELEASE_DATE = "item_release";
    public static final String COLUMN_ITEM_IMAGE = "item_image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //String movie_query = "CREATE TABLE"+TABLE_NAME_MOVIES+" ("+COLUMN_ITEM_ID+" INTEGER, "+COLUMN_ITEM_NAME+" VARCHAR(70), "+COLUMN_ITEM_VOTE_AVERAGE +" DECIMAL(3,2), "+COLUMN_ITEM_VOTE_COUNT+" INTEGER, "+COLUMN_ITEM_IMAGE+" BLOB)";
        String movie_query = "CREATE TABLE "+TABLE_NAME_MOVIES+" ("+COLUMN_ITEM_ID+" INTEGER, "+COLUMN_ITEM_NAME+" VARCHAR(70), "+COLUMN_ITEM_VOTE_AVERAGE +" DECIMAL(3,2), "+COLUMN_ITEM_VOTE_COUNT+" VARCHAR(10), "+COLUMN_ITEM_RELEASE_DATE+" VARCHAR(12), "+COLUMN_ITEM_IMAGE+" VARCHAR(50))";
        String tv_query = "CREATE TABLE "+TABLE_NAME_TV+" ("+COLUMN_ITEM_ID+" INTEGER, "+COLUMN_ITEM_NAME+" VARCHAR(70), "+COLUMN_ITEM_VOTE_AVERAGE +" DECIMAL(3,2), "+COLUMN_ITEM_VOTE_COUNT+" VARCHAR(10), "+COLUMN_ITEM_RELEASE_DATE+" VARCHAR(12), "+COLUMN_ITEM_IMAGE+" VARCHAR(50))";
        db.execSQL(movie_query);
        db.execSQL(tv_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_TV);
        onCreate(db);
    }

    public boolean insertFavorite(int id, String name, float avg, String count, String release, boolean movie, String image, Context context) throws IOException {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        /*if(!image.equals("null")) {
            URL url = new URL(image);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] myImage = new byte[1024];

            int len;
            while((len = inputStream.read())!=-1)
                byteArrayOutputStream.write(myImage, 0, len);
            contentValues.put(COLUMN_ITEM_IMAGE, myImage);
        }*/

        contentValues.put(COLUMN_ITEM_ID, id);
        contentValues.put(COLUMN_ITEM_NAME, name);
        contentValues.put(COLUMN_ITEM_VOTE_AVERAGE, avg);
        contentValues.put(COLUMN_ITEM_VOTE_COUNT, count);
        contentValues.put(COLUMN_ITEM_RELEASE_DATE, release);

        /*// Create a file in the Internal Storage
        String fileName = name;
        ContextWrapper cw = new ContextWrapper(context);

        File file = cw.getDir("images", Context.MODE_PRIVATE);
        File path = new File(file, fileName+".jpg");

        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(path);
//            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            contentValues.put(COLUMN_ITEM_IMAGE, path.getPath());
            Toast.makeText(context, "Path: "+path.getParent(), Toast.LENGTH_SHORT).show();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        if(movie)
            if(sqLiteDatabase.insert(TABLE_NAME_MOVIES, null, contentValues) == -1)
                return false;
            else
                return true;
        else
        if(sqLiteDatabase.insert(TABLE_NAME_TV, null, contentValues) == -1)
            return false;
        else
            return true;
    }

    public boolean isFavorite(int id, boolean movie){

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query;
        if(movie)
            query = "SELECT * FROM "+TABLE_NAME_MOVIES+" WHERE "+COLUMN_ITEM_ID+" = "+id;
        else
            query = "SELECT * FROM "+TABLE_NAME_TV+" WHERE "+COLUMN_ITEM_ID+" = "+id;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        else
            return true;
    }

    public boolean removeFavorite(int id, boolean movie){

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        if(movie)
            if((sqLiteDatabase.delete(TABLE_NAME_MOVIES, COLUMN_ITEM_ID+" = ?", new String[] {String.valueOf(id)})) > 0)
                return true;
            else
                return false;
        else
            if((sqLiteDatabase.delete(TABLE_NAME_TV, COLUMN_ITEM_ID+" = ?", new String[] {String.valueOf(id)})) > 0)
                return true;
            else
                return false;
    }

    public List<Integer> getId(boolean movie){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query;

        if(movie)
            query = "SELECT "+COLUMN_ITEM_ID+" FROM "+TABLE_NAME_MOVIES;
        else
            query = "SELECT "+COLUMN_ITEM_ID+" FROM "+TABLE_NAME_TV;

        List<Integer> Ids = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                Ids.add(cursor.getInt(cursor.getColumnIndex(COLUMN_ITEM_ID)));
            }while(cursor.moveToNext());
        }
        cursor.close();

        return Ids;
    }

    public List<String> getDetails(int id, boolean movie){

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query;

        if(movie)
            query = "SELECT * FROM "+TABLE_NAME_MOVIES+" WHERE "+COLUMN_ITEM_ID+" = "+id;
        else
            query = "SELECT * FROM "+TABLE_NAME_TV+" WHERE "+COLUMN_ITEM_ID+" = "+id;

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        String name = cursor.getString(1);
        String avg = cursor.getString(2);
        String count = cursor.getString(3);
        String release = cursor.getString(4);
        String image_path = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_IMAGE));
        cursor.close();
        List<String> myDetails = new ArrayList<>();
        myDetails.add(name);
        myDetails.add(avg);
        myDetails.add(count);
        myDetails.add(release);
        myDetails.add(String.valueOf(id));
        myDetails.add(image_path);

        return myDetails;
    }

    /*public Bitmap getImage(int id, boolean movie){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query;

        if(movie)
            query = "SELECT * FROM "+TABLE_NAME_MOVIES+" WHERE "+COLUMN_ITEM_ID+" = "+id;
        else
            query = "SELECT * FROM "+TABLE_NAME_TV+" WHERE "+COLUMN_ITEM_ID+" = "+id;

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        byte[] byteArray = cursor.getBlob(cursor.getColumnIndex(COLUMN_ITEM_IMAGE));
        Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length);

        return image;
    }*/
}
