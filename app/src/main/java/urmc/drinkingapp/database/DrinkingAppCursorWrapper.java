package urmc.drinkingapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import urmc.drinkingapp.model.User;

/**
 * Created by Alessandro on 2/25/17.
 */

/*
THIS CLASS IS NOT CURRENTLY BEING USED
This class is part of the original offline database. Not active in the most recent implementation
 */
//Cursor Wrapper to facilitate the process of obtaining objects and their info from the database
public class DrinkingAppCursorWrapper extends CursorWrapper {

    //constructor
    public DrinkingAppCursorWrapper(Cursor cursor){
        super(cursor);
    }

    //method to get user from the database
    public User getUserFromDB(){
        //obtains all the information from the database
        UUID id = UUID.fromString(getString(getColumnIndex(DrinkingAppSchema.UserTable.Columns.ID)));
        String email = getString(getColumnIndex(DrinkingAppSchema.UserTable.Columns.EMAIL));
        String password = getString(getColumnIndex(DrinkingAppSchema.UserTable.Columns.PASSWORD));
        String fullname = getString(getColumnIndex(DrinkingAppSchema.UserTable.Columns.FULLNAME));
        String profilepic = getString(getColumnIndex(DrinkingAppSchema.UserTable.Columns.PROFILEPIC));

        //sets all the information to a new user object to be returned
        User theUser = new User(id);
        theUser.setEmail(email);
        theUser.setPassword(password);
        theUser.setFullname(fullname);
        theUser.setProfilePic(profilepic);
        return theUser;
    }
}
