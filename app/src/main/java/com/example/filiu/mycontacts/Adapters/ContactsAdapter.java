package com.example.filiu.mycontacts.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.filiu.mycontacts.DataBase.ContactsDbHelper;
import com.example.filiu.mycontacts.Manifests.ContactMaps;
import com.example.filiu.mycontacts.Manifests.ContactPage;
import com.example.filiu.mycontacts.Models.Contact;
import com.example.filiu.mycontacts.R;

import java.util.ArrayList;

/**
 * Created by filiu on 02.05.2017.
 */

public class ContactsAdapter extends ArrayAdapter<Contact> {

    private int resource;
    private Context context;
    private ArrayList<Contact> contacts;
    private ContactsDbHelper DbHelper;


    public static class ViewHolder{

        ImageView profile_picture_image;
        TextView full_name_textView;
        TextView phone_number_textView;
        Button delete_button;
        ImageButton location_button;
    }

    public ContactsAdapter(Context context, int resource, ArrayList<Contact> objects) {
        super(context, resource, objects);

        this.resource = resource;
        this.context = context;
        this.DbHelper = new ContactsDbHelper(context);
        this.contacts = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(resource, null);

            ViewHolder holder = new ViewHolder();
            holder.full_name_textView = (TextView) view.findViewById(R.id.full_name_textView);
            holder.phone_number_textView = (TextView) view.findViewById(R.id.phone_number_textView);
            holder.profile_picture_image = (ImageView) view.findViewById(R.id.contact_image);
            holder.delete_button = (Button) view.findViewById(R.id.delete_button);
            holder.location_button = (ImageButton) view.findViewById(R.id.location_button);

            view.setTag(holder);
        }

        ViewHolder holderView = (ViewHolder) view.getTag();

        final Contact contact = getItem(position);

        holderView.full_name_textView.setText(contact.getFull_name());
        holderView.phone_number_textView.setText(contact.getPhone_number());
        if(!contact.getProfile_picture().isEmpty() && contact.getProfile_picture() != null)
            holderView.profile_picture_image.setImageBitmap(BitmapFactory
                    .decodeFile(contact.getProfile_picture()));
        else
            holderView.profile_picture_image.setImageResource(R.mipmap.default_contact);

        // Delete Button Action
        holderView.delete_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                DbHelper.delete(contact);
                contacts.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });

        // Location Button Action
        holderView.location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ContactMaps.class);
                intent.putExtra("contactId", contact.getId());
                intent.putExtra("first_name", contact.getFirst_name());
                intent.putExtra("last_name", contact.getLast_name());
                intent.putExtra("profile_picture", contact.getProfile_picture());
                intent.putExtra("phone_number", contact.getPhone_number());
                intent.putExtra("birthday", contact.getBirthday().getTime());
                intent.putExtra("email", contact.getEmail());
                intent.putExtra("latitude", contact.getLatitude());
                intent.putExtra("longitude", contact.getLongitude());

                context.startActivity(intent);
            }
        });

        return view;
    }
}