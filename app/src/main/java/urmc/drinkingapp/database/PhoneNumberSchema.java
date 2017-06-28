package urmc.drinkingapp.database;

/**
 * Created by litchiyang on 6/25/17.
 */

public class PhoneNumberSchema {

    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "test.db";

    public static final class PhoneNumbersTable {
        public static final String TABLE_NAME = "phoneNumbersTable";

        public static final class Cols {
            public static final String ID = "id";
            public static final String NUMBER = "number";
        }
    }


}
