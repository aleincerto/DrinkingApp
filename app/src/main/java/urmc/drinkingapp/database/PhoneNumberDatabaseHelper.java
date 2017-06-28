package urmc.drinkingapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import urmc.drinkingapp.model.PhoneNumbers;

/**
 * Created by litchiyang on 6/25/17.
 */

public class PhoneNumberDatabaseHelper extends SQLiteOpenHelper {
    public PhoneNumberDatabaseHelper(Context context) {
        super(context, PhoneNumberSchema.DATABASE_NAME, null, PhoneNumberSchema.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + PhoneNumberSchema.PhoneNumbersTable.TABLE_NAME
                + "(_id integer primary key autoincrement, "
                + PhoneNumberSchema.PhoneNumbersTable.Cols.ID + ", "
                + PhoneNumberSchema.PhoneNumbersTable.Cols.NUMBER + ")");

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
