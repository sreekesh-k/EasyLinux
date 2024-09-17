package com.example.easylinux;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.ContentValues;

public class DbHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "user_db";

    //database version, version: 1 indicates the database is new and this is the first version
    private static final int DB_VERSION=1;
    //tablename
    private static final String TABLE_NAME="user_details";
    //column names of the table: Strings
    //column 1: ID
    private static final String ID = "id";
    //column 2: USERNAME
    private static final String USERNAME = "username";
    //column 3: PASSWORD
    private static final String PASSWORD= "password";

    public DbHandler(Context context) {
        super(context, DB_NAME,null,DB_VERSION);
        //call the superclass method
        //arguments: Context object, Database name, list of columns that are allowed null values, version of database
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //query for creating a table, insert the syntax parts between " " , insert variable names between + +
        String query1 = "CREATE TABLE " +TABLE_NAME+ "("
                +ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
                +USERNAME+ " TEXT," +PASSWORD+ " TEXT)";
        //execute above query
        db.execSQL(query1);
        //execSQL executes query that doesnt return any results, eg: insert,drop table
    }

    //onUpgrade() method creates a database if it is not existing, upgrades to new version if already exisitng
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //A database is created with an initialization of atleast one table
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //function to add a new user by inserting values into the table
    public void addNewUser(String uname, String pwd)
    {
        //creating a variable for ur sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db=this.getWritableDatabase();
        //create a variable for content values
        ContentValues values=new ContentValues();

        //passing key-value pairs
        values.put(USERNAME,uname);
        values.put(PASSWORD,pwd);

        //pass content values to our table.
        db.insert(TABLE_NAME,null,values);
        //close db
        db.close();
    }

    //This method checks the username and password are matching during login
    public Boolean checkEmailPassword(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();

        //cursor object retrives results of a matching operation from SELECT query
        Cursor cursor;
        cursor= db.rawQuery("Select * from "+TABLE_NAME+" where "+USERNAME+" = ? and "+PASSWORD+" = ?", new String[]{username, password});
        //SYNTAX: rawQuery(sql_query,selection arguments);
        //rawQuery executes query that returns results, eg: select operation

        if (cursor.getCount() > 0) {
            //cursor.getCount returns number of matching rows from the results
            return true;
        } else {
            return false;
        }
    }
}
