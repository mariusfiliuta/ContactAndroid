package com.example.filiu.mycontacts.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.transition.CircularPropagation;

import com.example.filiu.mycontacts.Models.Contact;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by filiu on 03.05.2017.
 */

public class ContactsDbHelper extends SQLiteOpenHelper{

    public ContactsDbHelper(Context context) {
        super(context, ContactsContract.DB_NAME, null, ContactsContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE IF NOT EXISTS " + ContactsContract.ContactsEntry.TABLE +
                " (" + "id INTEGER PRIMARY KEY, "
                + ContactsContract.ContactsEntry.COL_first_name + " VARCHAR, "
                + ContactsContract.ContactsEntry.COL_last_name + " VARCHAR, "
                + ContactsContract.ContactsEntry.COL_profile_picture + " VARCHAR, "
                + ContactsContract.ContactsEntry.COL_phone_number + " VARCHAR, "
                + ContactsContract.ContactsEntry.COL_email + " VARCHAR, "
                + ContactsContract.ContactsEntry.COL_latitude + " REAL, "
                + ContactsContract.ContactsEntry.COL_longitude + " REAL, "
                + ContactsContract.ContactsEntry.COL_birthday + " INTEGER)";
        System.out.println("Create table " + createTable);
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ContactsContract.ContactsEntry.TABLE);
        onCreate(db);
    }

    public void deleteDatabase(){
        this.deleteContactsDB();
    }


    public void deleteContactsDB() {
        String deleteScript = "delete from " + ContactsContract.ContactsEntry.TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteScript);
    }

    public void insertOrUpdate(Contact contact){


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if(contact.getId()!=0){
            values.put("id", contact.getId());
        }
        values.put(ContactsContract.ContactsEntry.COL_first_name, contact.getFirst_name());
        values.put(ContactsContract.ContactsEntry.COL_last_name, contact.getLast_name());
        values.put(ContactsContract.ContactsEntry.COL_email, contact.getEmail());
        values.put(ContactsContract.ContactsEntry.COL_birthday, contact.getBirthday().getTime());
        values.put(ContactsContract.ContactsEntry.COL_profile_picture, contact.getProfile_picture());
        values.put(ContactsContract.ContactsEntry.COL_latitude, contact.getLatitude());
        values.put(ContactsContract.ContactsEntry.COL_longitude, contact.getLongitude());
        values.put(ContactsContract.ContactsEntry.COL_phone_number, contact.getPhone_number());

        db.insertWithOnConflict(ContactsContract.ContactsEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        db.close();

    }


    public ArrayList<Contact> read() {
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        try {

            SQLiteDatabase db = this.getWritableDatabase();

            String queryString = "SELECT * FROM " + ContactsContract.ContactsEntry.TABLE;

            Cursor c = db.rawQuery(queryString, null);

            int idIndex = c.getColumnIndex("id");
            int first_nameIndex = c.getColumnIndex(ContactsContract.ContactsEntry.COL_first_name);
            int last_nameIndex = c.getColumnIndex(ContactsContract.ContactsEntry.COL_last_name);
            int birthdayIndex = c.getColumnIndex(ContactsContract.ContactsEntry.COL_birthday);
            int phone_numberIndex = c.getColumnIndex(ContactsContract.ContactsEntry.COL_phone_number);
            int latitudeIndex = c.getColumnIndex(ContactsContract.ContactsEntry.COL_latitude);
            int longitudeIndex = c.getColumnIndex(ContactsContract.ContactsEntry.COL_longitude);
            int profile_pictureIndex = c.getColumnIndex(ContactsContract.ContactsEntry.COL_profile_picture);
            int emailIndex = c.getColumnIndex(ContactsContract.ContactsEntry.COL_email);

            if (c != null && c.moveToFirst()) {
                do {
                    Contact contact = new Contact(c.getString(first_nameIndex), c.getString(last_nameIndex),
                            c.getString(profile_pictureIndex), c.getString(phone_numberIndex),
                            new Date(), c.getString(emailIndex));

                    Date date = new Date();
                    date.setTime(c.getLong(birthdayIndex));
                    contact.setBirthday(date);
                    contact.setId(c.getInt(idIndex));
                    contact.setLatitude(c.getDouble(latitudeIndex));
                    contact.setLongitude(c.getDouble(longitudeIndex));

                    contacts.add(contact);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contacts;
    }

    public boolean delete(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();

        boolean response = db.delete(ContactsContract.ContactsEntry.TABLE, "id = " + contact.getId(), null) > 0;

        db.close();

        return response;
    }
}
