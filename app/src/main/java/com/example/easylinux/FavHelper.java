package com.example.easylinux;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class FavHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_FAVORITES = "favorites";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_COMMAND_ID = "command_id";

    public FavHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the favorites table
        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_COMMAND_ID + " INTEGER"
                + ")";
        db.execSQL(CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    // Add command to favorites
    public void addFavorite(String username, int commandId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_COMMAND_ID, commandId);

        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

    // Get all favorite commands for a specific user
    public Cursor getFavoritesForUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_FAVORITES, new String[]{COLUMN_COMMAND_ID},
                COLUMN_USERNAME + "=?",
                new String[]{username}, null, null, null);
    }


    // Method to remove a command from favorites
    public void removeFavorite(String username, int commandId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, COLUMN_USERNAME + "=? AND " + COLUMN_COMMAND_ID + "=?",
                new String[]{username, String.valueOf(commandId)});
        db.close();
    }

    // Method to get favorite command IDs for a specific user
    public List<Integer> getFavoriteCommandIds(String username) {
        List<Integer> favoriteCommandIds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database to get the command IDs for the given username
        Cursor cursor = db.query(TABLE_FAVORITES, new String[]{COLUMN_COMMAND_ID},
                COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);

        // Iterate through the results and add the command IDs to the list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int commandId = cursor.getInt(cursor.getColumnIndex(COLUMN_COMMAND_ID));
                favoriteCommandIds.add(commandId);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return favoriteCommandIds;
    }
}
