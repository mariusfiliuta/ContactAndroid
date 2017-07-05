package com.example.filiu.mycontacts.Manifests;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;

import com.example.filiu.mycontacts.DataBase.ContactsDbHelper;
import com.example.filiu.mycontacts.Models.Contact;
import com.example.filiu.mycontacts.R;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;

public class ContactMaps extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 112;
    private GoogleMap mMap;
    private Contact contact;
    private Marker marker = null;
    private ContactsDbHelper DbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Bundle extras = getIntent().getExtras();
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

        DbHelper = new ContactsDbHelper(this);
    }

    @Override
    public void onBackPressed() {
        DbHelper.insertOrUpdate(contact);
        startActivity(new Intent(this, ContactsPage.class));
        finish();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
           mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }

        if(!contact.getLatitude().isNaN() && !contact.getLongitude().isNaN()) {
            LatLng contactLoc = new LatLng(contact.getLatitude(), contact.getLongitude());
            marker = mMap.addMarker(new MarkerOptions().position(contactLoc).title(contact.getFull_name()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(contactLoc));
        }
    }


    @Override
    public void onMapLongClick(LatLng point) {
        if(marker == null) {
            marker = mMap.addMarker(new MarkerOptions()
                    .position(point)
                    .title(contact.getFull_name()));
        }else{
            marker.setPosition(point);
        }

        contact.setLatitude(point.latitude);
        contact.setLongitude(point.longitude);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_FINE_LOCATION) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);

            }
        }
    }

}
