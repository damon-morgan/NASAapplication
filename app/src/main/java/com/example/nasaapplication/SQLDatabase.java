package com.example.nasaapplication;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 *  The NASA Application SQLDatabase class provides the functionality to the application for storing NASA images.
 *  The database utilizes one table that interacts with the user to add, and delete items.
 *
 *  Authors: Damon & Dylan
 *
 */
public class SQLDatabase extends SQLiteOpenHelper {
    protected final static String DATABASE_NAME = "nasadb";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "Nasa";
    public final static String COL_EXPLAIN = "Explanation";
    public final static String COL_TITLE = "Title";
    public final static String COL_DATE = "Date";
    public final static String COL_URL = "Url";

    public SQLDatabase(Context contxt) {
        super(contxt, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * onCreate is used with our database details in the arguments to create our Nasa table to store images.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DATE + " text UNIQUE,"
                + COL_TITLE + " text,"
                + COL_EXPLAIN + " text,"
                + COL_URL + " text)");
    }

    /**
     * onUpgrade is used with our database details in the arguments to drop the old database version and replace with a newly created one and upgraded version.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    /**
     * onDowngrade is used with our database details in the arguments to drop the old database version and replace with a newly created one and older version.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }
}