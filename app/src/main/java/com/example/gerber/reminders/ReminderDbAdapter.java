package com.example.gerber.reminders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Sel on 2016/8/22.
 */
public class ReminderDbAdapter {
    //these are the corresponding indices
    public static final int INDEX_ID = 0;
    public static final int INDEX_CONTENT = INDEX_ID + 1;
    public static final int INDEX_IMPORTANT = INDEX_ID + 2;
    //these are the column names
    public static final String COL_ID = "_id";
    public static final String COL_CONTENT = "content";
    public static final String COL_IMPORTANT = "important";
    //used for logging
    private static final String TAG = "ReminderDbAdpater";
    private static final String DATABASE_NAME = "dba_remdrs";
    private static final String TABLE_NAME = "tbl_remdrs";
    private static final int DATABASE_VERSION = 1;
    // SQL statement used to create the database
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_CONTENT + " TEXT, " +
                    COL_IMPORTANT + " INTEGER );";
    //SQL statement used to drop the database
    public static final String DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    private final Context mCtx;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDB;


    public ReminderDbAdapter(Context ctx) {
        mCtx = ctx;
    }

    public void open() throws SQLException {

        if (mDbHelper == null) {
            mDbHelper = new DatabaseHelper(mCtx);
            mDB = mDbHelper.getWritableDatabase();
        }
    }

    public void close() throws SQLException {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    //CREATE
    //note that the id will be created for you automatically
    public void createReminder(String name, boolean important) {
        open();
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT,name);
        values.put(COL_IMPORTANT, important ? 1 : 0);
        mDB.insert(TABLE_NAME, null, values);
    }

    //overloaded to take a reminder
    public void createReminder(Reminder reminder) {
        open();
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, reminder.getContent());
        values.put(COL_IMPORTANT, reminder.getiImportant());
        mDB.insert(TABLE_NAME, null, values);
    }

    //READ
    public Reminder fetchReminderById(int id) {
        open();
        Cursor cursor = mDB.query(TABLE_NAME, new String[]{COL_ID, COL_CONTENT, COL_IMPORTANT}, COL_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor!= null) {
            cursor.moveToFirst();
            return new Reminder(
                    cursor.getInt(INDEX_ID),
                    cursor.getString(INDEX_CONTENT),
                    cursor.getInt(INDEX_IMPORTANT)
            );
        }
        return null;
    }

    public Cursor fetchAllReminders() {
        open();
        Cursor cursor = mDB.query(TABLE_NAME,new String[]{COL_ID,COL_CONTENT,COL_IMPORTANT},null,null,null,null,null);
        if (cursor!= null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void updateReminder(Reminder reminder) {
        open();
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, reminder.getContent());
        values.put(COL_IMPORTANT, reminder.getiImportant());
        mDB.update(TABLE_NAME, values, COL_ID + "=?", new String[]{String.valueOf(reminder.getId())});
    }

    public void deleteReminder(int id) {
        open();
        mDB.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteAllReminder() {
        open();
        mDB.delete(TABLE_NAME, null, null);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            Log.w(TAG, DATABASE_CREATE);
            sqLiteDatabase.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            Log.w(TAG, "Upgrade database from version " + i + " to " + i1 + ",which will destory all old data");
            sqLiteDatabase.execSQL(DROP_TABLE);
            onCreate(sqLiteDatabase);
        }
    }
}
