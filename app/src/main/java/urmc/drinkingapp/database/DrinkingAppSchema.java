package urmc.drinkingapp.database;

/**
 * Created by Alessandro on 2/25/17.
 */
/*
THIS CLASS IS NOT CURRENTLY BEING USED
This class is part of the original offline database. Not active in the most recent implementation
 */
//standard implementation of a database schema - contains all the desired attributes to be added to the database
public class DrinkingAppSchema {

    //database name and version number
    public static final String DATABASE_NAME = "drinking_app_database.db";
    public static final int VERSION = 1;

    //inner class with the table inside the database
    public static final class UserTable{
        public static final String NAME = "users";

        //inner class with the columns inside the table with all the desired attributes
        public static final class Columns {
            public static final String ID = "id";
            public static final String EMAIL = "email";
            public static final String PASSWORD = "password";
            public static final String FULLNAME = "fullname";
            public static final String PROFILEPIC = "profilepic";

        }
    }

}
