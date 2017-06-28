package urmc.drinkingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.UUID;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import urmc.drinkingapp.model.PhoneNumbers;

/**
 * Created by litchiyang on 6/25/17.
 */

public class PhoneNumberCollection {


    private static PhoneNumberCollection sCollection;

    private final Context mContext;
    private final SQLiteDatabase mDatabase;
    private final List<PhoneNumbers> mPhoneNumbers;
    private final Map<UUID, PhoneNumbers> mPhoneNumbersMap;

    private PhoneNumberCollection(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new PhoneNumberDatabaseHelper(mContext).getWritableDatabase();
        mPhoneNumbers = new LinkedList<>();
        mPhoneNumbersMap = new HashMap<>();
    }

    public static synchronized PhoneNumberCollection get(Context context) {
        if(sCollection == null) {
            sCollection = new PhoneNumberCollection(context);
        }
        return sCollection;
    }

    public static PhoneNumberCollection get() {
        return sCollection;
    }

    public List<PhoneNumbers> getCollection() {
        mPhoneNumbers.clear();
        mPhoneNumbersMap.clear();
        PhoneNumberCursorWrapper wrapper = queryPhoneNumbers(null, null);

        try {
            wrapper.moveToFirst();
            while(wrapper.isAfterLast() == false) {
                PhoneNumbers numbers = wrapper.getPhoneNumber();
                mPhoneNumbers.add(numbers);
                mPhoneNumbersMap.put(numbers.getId(), numbers);
                wrapper.moveToNext();
            }
        }
        finally {
            wrapper.close();
        }

        return mPhoneNumbers;
    }

    public void addNumbers(PhoneNumbers number) {
        ContentValues values = getContentvalues(number);
        mDatabase.insert(PhoneNumberSchema.PhoneNumbersTable.TABLE_NAME, null, values);
    }


    private static ContentValues getContentvalues(PhoneNumbers number) {
        ContentValues values = new ContentValues();

        values.put(PhoneNumberSchema.PhoneNumbersTable.Cols.ID, number.getId().toString());
        values.put(PhoneNumberSchema.PhoneNumbersTable.Cols.NUMBER, number.getNumber().toString());

        return values;
    }

    private PhoneNumberCursorWrapper queryPhoneNumbers(String where, String[] args) {
        Cursor cursor = mDatabase.query(
                PhoneNumberSchema.PhoneNumbersTable.TABLE_NAME,
                null,
                where,
                args,
                null,
                null,
                null
        );

        return new PhoneNumberCursorWrapper(cursor);
    }


}

