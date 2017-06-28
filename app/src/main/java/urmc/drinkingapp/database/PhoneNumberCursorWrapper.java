package urmc.drinkingapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import urmc.drinkingapp.database.*;
import urmc.drinkingapp.model.*;

import java.util.UUID;


/**
 * Created by litchiyang on 6/25/17.
 */

public class PhoneNumberCursorWrapper extends CursorWrapper {


    //Constructor for cursor
    public PhoneNumberCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    //Get the phone numbers
    public PhoneNumbers getPhoneNumber() {

        if(getCount() == 0){
            return null;
        }
        UUID id = UUID.fromString(getString(getColumnIndex(PhoneNumberSchema.PhoneNumbersTable.Cols.ID)));
        String number = getString(getColumnIndex(PhoneNumberSchema.PhoneNumbersTable.Cols.NUMBER));


        PhoneNumbers n = new PhoneNumbers(id);
        Log.d("YANG1", "getPhoneNumber: " + number);
        n.setNumber(number);

        return n;
    }





}

