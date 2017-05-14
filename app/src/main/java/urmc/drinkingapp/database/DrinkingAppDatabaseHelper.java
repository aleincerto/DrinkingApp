package urmc.drinkingapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alessandro on 2/25/17.
 */
/*
THIS CLASS IS NOT CURRENTLY BEING USED
This class is part of the original offline database. Not active in the most recent implementation
 */
//standard database helper - creates different tables in the database based on the schema
public class DrinkingAppDatabaseHelper extends SQLiteOpenHelper {

    //constructor
    public DrinkingAppDatabaseHelper(Context context) {
        super(context, DrinkingAppSchema.DATABASE_NAME, null, DrinkingAppSchema.VERSION);
    }

    //generates a new tables for the database with all the desired attributes
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DrinkingAppSchema.UserTable.NAME
                + "(_id integer primary key autoincrement, "
                + DrinkingAppSchema.UserTable.Columns.ID + ", "
                + DrinkingAppSchema.UserTable.Columns.EMAIL + ", "
                + DrinkingAppSchema.UserTable.Columns.PASSWORD + ", "
                + DrinkingAppSchema.UserTable.Columns.FULLNAME + ", "
                + DrinkingAppSchema.UserTable.Columns.PROFILEPIC + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



}
