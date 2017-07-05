package com.example.filiu.mycontacts.DataBase;

import android.provider.BaseColumns;

/**
 * Created by filiu on 03.05.2017.
 */

public class ContactsContract {
    public static final String DB_NAME = "mycontacts.db";
    public static final int DB_VERSION = 1;

    public class ContactsEntry implements BaseColumns {
        public static final String TABLE = "contacts";

        public static final String COL_first_name = "first_name";
        public static final String COL_last_name = "last_name";
        public static final String COL_profile_picture = "profile_picture";
        public static final String COL_birthday = "birthday";
        public static final String COL_phone_number = "phone_number";
        public static final String COL_email = "email";
        public static final String COL_latitude = "latitude";
        public static final String COL_longitude = "longitude";
    }
}
