package urmc.drinkingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import urmc.drinkingapp.model.User;

/**
 * Created by Alessandro on 2/25/17.
 */

/*
THIS CLASS IS NOT CURRENTLY BEING USED
This class is part of the original offline database. Not active in the most recent implementation
It was made to communicate with the  Offline SQLite Database.
 */
//Class to communicate with the database
public class DrinkingAppCollection {

    //contains list of different model objects
    private final Context mContext;
    private final Map<UUID,User> mUserMap;
    private List<User> mUserList;
    private final SQLiteDatabase mDatabase;

    public static User mMainUser; //main user is the one who logs in

    private static DrinkingAppCollection SING_DrinkingAppCollection; //SINGLETON collection

    //constructor
    private DrinkingAppCollection(Context c){
        mContext= c.getApplicationContext();
        mDatabase = new DrinkingAppDatabaseHelper(mContext).getWritableDatabase();
        mUserList = new LinkedList<>();
        mUserMap = new HashMap<>();
    }

    //initializes an intance of the collection - it's unique
    public static synchronized DrinkingAppCollection get(Context context){
        if (SING_DrinkingAppCollection == null){
            SING_DrinkingAppCollection = new DrinkingAppCollection(context);
        }
        return SING_DrinkingAppCollection;
    }

    //returns a specific user from the collection
    public User getUser(UUID id){
        return mUserMap.get(id);
    }

    //returns a list with all the users in the collection
    public List<User> getUserList() {
        return mUserList;
    }


    //helper method to get all the values from a specific user and add them to the database
    public static ContentValues getContentValues(User theUser){

        ContentValues theValues = new ContentValues();

        theValues.put(DrinkingAppSchema.UserTable.Columns.ID, theUser.getID().toString());
        theValues.put(DrinkingAppSchema.UserTable.Columns.EMAIL, theUser.getEmail());
        theValues.put(DrinkingAppSchema.UserTable.Columns.PASSWORD, theUser.getPassword());
        theValues.put(DrinkingAppSchema.UserTable.Columns.FULLNAME, theUser.getFullname());
        theValues.put(DrinkingAppSchema.UserTable.Columns.PROFILEPIC, theUser.getProfilePic());

        return theValues;
    }

    //adds a User to the database using the previous helper method and using the insert method of the database
    public void addUser(User user){
        ContentValues theValues = getContentValues(user);
        mDatabase.insert(DrinkingAppSchema.UserTable.NAME, null, theValues);
    }

    //Updates information of a specific user in the database
    public void updateUser(User user){
        String id = user.getID().toString();
        ContentValues values = getContentValues(user);
        mDatabase.update(DrinkingAppSchema.UserTable.NAME,
                values,
                DrinkingAppSchema.UserTable.Columns.ID + "=?",
                new String[]{id});
    }

    //get a user from the database using email and password
    public User getUser(String email, String password){

        Cursor cursor = mDatabase.query(
                DrinkingAppSchema.UserTable.NAME,    // table name
                null,                                   // which columns; null for all
                "email = ? AND password = ?",           // where clause, e.g. id=?
                new String[]{
                        email, password
                },                              // where arguments
                null,                                   // group by
                null,                                   // having
                null                                    // order by
        );
        DrinkingAppCursorWrapper wrapper = new DrinkingAppCursorWrapper(cursor);
        //cursor.close();

        User user;

        if(wrapper.getCount() > 0) {
            wrapper.moveToFirst();
            user = wrapper.getUserFromDB();
        }else{
            user = null;
        }
        wrapper.close();

        return user;
    }

    //get user from database using only email
    public User getUser(String email){

        Cursor cursor = mDatabase.query(
                DrinkingAppSchema.UserTable.NAME,    // table name
                null,                                   // which columns; null for all
                "email = ?",           // where clause, e.g. id=?
                new String[]{
                        email
                },                              // where arguments
                null,                                   // group by
                null,                                   // having
                null                                    // order by
        );
        DrinkingAppCursorWrapper wrapper = new DrinkingAppCursorWrapper(cursor);
        //cursor.close();

        User user;

        if(wrapper.getCount() > 0) {
            wrapper.moveToFirst();
            user = wrapper.getUserFromDB();
        }else{
            user = null;
        }
        wrapper.close();

        return user;
    }

    //method to query all of the users
    private DrinkingAppCursorWrapper queryUsers(String where, String[] arguments){

        Cursor cursor = mDatabase.query(
                DrinkingAppSchema.UserTable.NAME,    // table name
                null,                                   // which columns; null for all
                where,                                  // where clause, e.g. id=?
                arguments,                              // where arguments
                null,                                   // group by
                null,                                   // having
                null                                    // order by
        );
        DrinkingAppCursorWrapper wrapper = new DrinkingAppCursorWrapper(cursor);
        //cursor.close();
        return wrapper;



    }

    //method to query all of the users
    private DrinkingAppCursorWrapper queryUsersButMyself(String email){

        Cursor cursor = mDatabase.query(
                DrinkingAppSchema.UserTable.NAME,    // table name
                null,                                   // which columns; null for all
                "email != ?",           // where clause, e.g. id=?
                new String[]{
                        email
                },                              // where arguments
                null,                                   // group by
                null,                                   // having
                null                                    // order by
        );
        DrinkingAppCursorWrapper wrapper = new DrinkingAppCursorWrapper(cursor);
        //cursor.close();
        return wrapper;



    }

    //method to query all of the users
    private DrinkingAppCursorWrapper queryUsersByQuery(String query){

        Cursor cursor = mDatabase.query(
                DrinkingAppSchema.UserTable.NAME,    // table name
                null,                                   // which columns; null for all
                "fullname LIKE ?",           // where clause, e.g. id=?
                new String[]{
                        "%"+query+"%"
                },                              // where arguments
                null,                                   // group by
                null,                                   // having
                null                                    // order by
        );
        DrinkingAppCursorWrapper wrapper = new DrinkingAppCursorWrapper(cursor);
        //cursor.close();
        return wrapper;

    }

    //method to get all users from the database and return a list
    public List<User> getAllUsersBasedOnQuery(String query){

        DrinkingAppCursorWrapper wrapper = queryUsersByQuery(query);
        mUserList = new ArrayList<>();

        try{
            wrapper.moveToFirst();
            while (!wrapper.isAfterLast()){
                User aUser = wrapper.getUserFromDB();
                mUserList.add(aUser);
                wrapper.moveToNext();
            }
        }
        finally {
            wrapper.close();
        }
        return mUserList;
    }


    //method to get all users from the database and return a list
    public List<User> getAllUsersButMyself(String email){

        DrinkingAppCursorWrapper wrapper = queryUsersButMyself(email);
        mUserList = new ArrayList<>();

        try{
            wrapper.moveToFirst();
            while (!wrapper.isAfterLast()){
                User aUser = wrapper.getUserFromDB();
                mUserList.add(aUser);
                wrapper.moveToNext();
            }
        }
        finally {
            wrapper.close();
        }
        return mUserList;
    }

    //method to get all users from the database and return a list
    public List<User> getAllUsers(){

        DrinkingAppCursorWrapper wrapper = queryUsers(null, null);
        mUserList = new ArrayList<>();

        try{
            wrapper.moveToFirst();
            while (!wrapper.isAfterLast()){
                User aUser = wrapper.getUserFromDB();
                mUserList.add(aUser);
                wrapper.moveToNext();
            }
        }
        finally {
            wrapper.close();
        }
        return mUserList;
    }

}
