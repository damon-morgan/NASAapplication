package com.example.nasaapplication;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLDatabase extends SQLiteOpenHelper {
    protected final static String DATABASE_NAME = "nasadb";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "Nasa";
    public final static String COL_EXPLAIN = "Explanation";
    public final static String COL_TITLE = "Title";
    public final static String COL_DATE = "Date";

    public SQLDatabase(Context contxt) {
        super(contxt, DATABASE_NAME, null, VERSION_NUM);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DATE + " text,"
                + COL_TITLE + " text,"
                + COL_EXPLAIN + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }
}