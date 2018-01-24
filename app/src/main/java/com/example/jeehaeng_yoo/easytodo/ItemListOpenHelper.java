package com.example.jeehaeng_yoo.easytodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by JeeHaeng_Yoo on 12/29/2017.
 */

public class ItemListOpenHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    // It's a good idea to always define a log tag like this.
    private static final String TAG = ItemListOpenHelper.class.getSimpleName();

    // has to be 1 first time or app will crash
    private static final int DATABASE_VERSION = 1;
    private static final String ITEM_LIST_TABLE = "item_entries";
    private static final String DATABASE_NAME = "itemlist";

    // Column names...
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_DETAIL = "detail";

    // ... and a string array of columns.
    private static final String[] COLUMNS = { KEY_ID, KEY_NAME, KEY_DATE, KEY_TIME, KEY_DETAIL};

    private static final String ITEM_LIST_TABLE_CREATE =
            "CREATE TABLE " + ITEM_LIST_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY, " +
                    // id will auto-increment if no value passed
                    KEY_NAME + " NAME, " +
                    KEY_DATE + " DATE, " +
                    KEY_TIME + " TIME, " +
                    KEY_DETAIL + " DETAIL );";

    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;

    public ItemListOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ITEM_LIST_TABLE_CREATE);
        //fillDatabaseWithData(db);
    }

    private void fillDatabaseWithData(SQLiteDatabase db) {
        String[] words = {"Android", "Adapter", "ListView"};

        // Create a container for the data.
        ContentValues values = new ContentValues();

        for (int i=0; i < words.length; i++) {
            // Put column/value pairs into the container.
            // put() overrides existing values.
            values.put(KEY_ID, i);
            values.put(KEY_NAME, words[i]);
            values.put(KEY_DATE, "00/00/00");
            values.put(KEY_TIME, "00:00:00");
            values.put(KEY_DETAIL, "abcdefg");
            db.insert(ITEM_LIST_TABLE, null, values);
        }
    }

    public ToDoItem query(int position) {
        String query = "SELECT  * FROM " + ITEM_LIST_TABLE +
                " ORDER BY " + KEY_DATE + " ASC " +
                "LIMIT " + position + ",1";

        Cursor cursor = null;
        ToDoItem entry = new ToDoItem();

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();
            entry.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            entry.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            entry.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            entry.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME)));
            entry.setDetail(cursor.getString(cursor.getColumnIndex(KEY_DETAIL)));
        } catch (Exception e) {
            Log.d(TAG, "QUERY EXCEPTION! " + e.getMessage());
        } finally {
            cursor.close();
            return entry;
        }
    }

    public int dbCount() {
        String query = "SELECT COUNT(*) FROM " + ITEM_LIST_TABLE;
        int count = 0;
        Cursor cursor = null;

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();
            count = cursor.getInt(0);
            System.out.println(count);
        } catch (Exception e) {
            Log.d(TAG, "QUERY EXCEPTION! " + e.getMessage());
        } finally {
            cursor.close();
            return count;
        }
    }

    public void closeDB(){
        db.close();
    }

    /**
     * Adds a single word row/entry to the database.
     *
     * @param  data New word.
     * @return The id of the inserted word.
     */
    public long insert(String [] data) {
        long newId = 0;
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, data[0]);
        values.put(KEY_DATE, data[1]);
        values.put(KEY_TIME, data[2]);
        values.put(KEY_DETAIL, data[3]);
        try {
            if (mWritableDB == null) {mWritableDB = getWritableDatabase();}
            newId = mWritableDB.insert(ITEM_LIST_TABLE, null, values);
        } catch (Exception e) {
            Log.d(TAG, "INSERT EXCEPTION! " + e.getMessage());
        }
        return newId;
    }

    /**
     * Updates the word with the supplied id to the supplied value.
     *
     * @param id Id of the word to update.
     * @param data The new value of the word.
     * @return The number of rows affected or -1 of nothing was updated.
     */
    public int update(int id, String [] data) {
        int mNumberOfRowsUpdated = -1;
        try {
            if (mWritableDB == null) {mWritableDB = getWritableDatabase();}
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, data[0]);
            values.put(KEY_DATE, data[1]);
            values.put(KEY_TIME, data[2]);
            values.put(KEY_DETAIL, data[3]);

            mNumberOfRowsUpdated = mWritableDB.update(ITEM_LIST_TABLE, //table to change
                    values, // new values to insert
                    KEY_ID + " = ?", // selection criteria for row (in this case, the _id column)
                    new String[]{String.valueOf(id)}); //selection args; the actual value of the id

        } catch (Exception e) {
            Log.d (TAG, "UPDATE EXCEPTION! " + e.getMessage());
        }
        return mNumberOfRowsUpdated;
    }

    /**
     * Deletes one entry identified by its id.
     *
     * @param id ID of the entry to delete.
     * @return The number of rows deleted. Since we are deleting by id, this should be 0 or 1.
     */
    public int delete(int id) {
        int deleted = 0;
        try {
            if (mWritableDB == null) {mWritableDB = getWritableDatabase();}
            deleted = mWritableDB.delete(ITEM_LIST_TABLE, //table name
                    KEY_ID + " = ? ", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.d (TAG, "DELETE EXCEPTION! " + e.getMessage());        }
        return deleted;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ItemListOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_LIST_TABLE);
        onCreate(db);
    }
}
