package com.example.filiu.mycontacts.Manifests;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.filiu.mycontacts.DataBase.ContactsDbHelper;
import com.example.filiu.mycontacts.R;
import com.example.filiu.mycontacts.Models.Contact;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by filiu on 02.05.2017.
 */

public class ContactPage extends AppCompatActivity{

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 111;
    private static int RESULT_LOAD_IMG = 1;
    private Contact contact;
    private ImageView profile_picture;
    private EditText first_name;
    private EditText last_name;
    private EditText phone_number;
    private EditText email;
    private Button save_button;
    private DatePicker datePicker;
    private ContactsDbHelper DbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_page);

        DbHelper = new ContactsDbHelper(this);

        Bundle extras = getIntent().getExtras();

        if(extras.getInt("contactId")!=0) {
            int contactId = extras.getInt("contactId");
            contact = new Contact(extras.getString("first_name"), extras.getString("last_name"),
                    extras.getString("profile_picture"), extras.getString("phone_number"),
                    new Date(), extras.getString("email"));
            contact.setId(contactId);
            contact.setLatitude(extras.getDouble("latitude"));
            contact.setLongitude(extras.getDouble("longitude"));
            Date date = new Date();
            date.setTime(extras.getLong("birthday"));
            contact.setBirthday(date);
        }
        else{
            contact = new Contact();
        }

        profile_picture = (ImageView) findViewById(R.id.contact_profile_picture);
        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });

        if(!contact.getProfile_picture().isEmpty()){
            profile_picture.setImageBitmap(BitmapFactory
                    .decodeFile(contact.getProfile_picture()));
        }

        first_name = (EditText) findViewById(R.id.contact_first_name);
        first_name.setText(contact.getFirst_name());

        last_name = (EditText) findViewById(R.id.contact_last_name);
        last_name.setText(contact.getLast_name());

        phone_number = (EditText) findViewById(R.id.contact_phone_number);
        phone_number.setText(contact.getPhone_number());

        email = (EditText) findViewById(R.id.contact_email);
        email.setText(contact.getEmail());

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        Calendar calendar = Calendar.getInstance();
        if(contact.getBirthday()!= null) {
            calendar.setTime(contact.getBirthday());
        }
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);

                contact.setBirthday(calendar.getTime());

                Log.d("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);
            }
        });


        save_button = (Button) findViewById(R.id.save_button);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContact();
            }
        });

        /*first_name.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (!event.isShiftPressed()) {
                                // the user is done typing.

                                return true; // consume.
                            }
                        }
                        return false; // pass on to other listeners.
                    }
                });*/
        first_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    updateContactStrings(v);
            }
        });
        last_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    updateContactStrings(v);
            }
        });
        phone_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    updateContactStrings(v);
            }
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    updateContactStrings(v);
            }
        });


    }

    //Save Contact Function
    public void saveContact(){
        updateContactStrings(first_name);
        updateContactStrings(last_name);
        updateContactStrings(phone_number);
        updateContactStrings(email);

        if(contact.getBirthday() == null){
            Context context = getApplicationContext();
            CharSequence text = "BirthDay not selected";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else {
            DbHelper.insertOrUpdate(contact);

            Intent intent = new Intent(getApplicationContext(), ContactsPage.class);
            startActivity(intent);
        }
    }

    //Function for textViews
    public void updateContactStrings(View view){
        switch (view.getId()){
            case R.id.contact_first_name:{
                contact.setFirst_name(first_name.getText().toString());
                break;
            }
            case R.id.contact_last_name:{
                contact.setLast_name(last_name.getText().toString());
                break;
            }
            case R.id.contact_phone_number:{
                contact.setPhone_number(phone_number.getText().toString());
                break;
            }
            case R.id.contact_email:{
                contact.setEmail(email.getText().toString());
                break;
            }
            default:{
                break;
            }
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                }else{
                loadImageFromGallery();
            }
            }else{
                loadImageFromGallery();
            }
    }

    // Function for profile_picture
    public void loadImageFromGallery() {

        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                contact.setProfile_picture(cursor.getString(columnIndex));
                cursor.close();
                // Set the Image in ImageView after decoding the String
                profile_picture.setImageBitmap(BitmapFactory
                        .decodeFile(contact.getProfile_picture()));

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }
}
