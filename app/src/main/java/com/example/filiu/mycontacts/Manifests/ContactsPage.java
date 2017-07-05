package com.example.filiu.mycontacts.Manifests;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.filiu.mycontacts.Adapters.ContactsAdapter;
import com.example.filiu.mycontacts.DataBase.ContactsDbHelper;
import com.example.filiu.mycontacts.Models.Contact;
import com.example.filiu.mycontacts.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ContactsPage extends AppCompatActivity {

    private Button add_contact_button;
    private ListView contacts_listView;
    //private android.widget.SearchView searchView;
    private ArrayList<Contact> contacts_list;
    private ContactsAdapter contactsAdapter;
    private ContactsDbHelper DbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);


        DbHelper = new ContactsDbHelper(this);

        add_contact_button = (Button) findViewById(R.id.add_contact_button_id);
        contacts_listView = (ListView) findViewById(R.id.contacts_list);
        //searchView = (android.widget.SearchView) findViewById(R.id.search_contacts_id);


        contacts_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), ContactPage.class);
                intent.putExtra("contactId", contacts_list.get(position).getId());
                intent.putExtra("first_name", contacts_list.get(position).getFirst_name());
                intent.putExtra("last_name", contacts_list.get(position).getLast_name());
                intent.putExtra("profile_picture", contacts_list.get(position).getProfile_picture());
                intent.putExtra("phone_number", contacts_list.get(position).getPhone_number());
                intent.putExtra("birthday", contacts_list.get(position).getBirthday().getTime());
                intent.putExtra("email", contacts_list.get(position).getEmail());
                intent.putExtra("latitude", contacts_list.get(position).getLatitude());
                intent.putExtra("longitude", contacts_list.get(position).getLongitude());

                startActivity(intent);
            }
        });


        add_contact_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContactPage.class);
                intent.putExtra("contactId", 0);

                startActivity(intent);
            }
        });

        setUpContacts();
    }

    private void setUpContacts(){

        contacts_list = DbHelper.read();

        Collections.sort(contacts_list, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getFull_name().compareTo(o2.getFull_name());
            }
        });

        contactsAdapter = new ContactsAdapter(this, R.layout.contacts_item, contacts_list);
        contacts_listView.setAdapter(contactsAdapter);
    }

}
