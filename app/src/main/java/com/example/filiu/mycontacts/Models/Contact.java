package com.example.filiu.mycontacts.Models;

import android.support.annotation.Nullable;

import java.util.Date;

/**
 * Created by filiu on 02.05.2017.
 */

public class Contact {
    private int id = 0;
    private String first_name;
    private String last_name;
    private String profile_picture;
    private String phone_number;
    private Date birthday;
    private String email;
    private Double latitude;
    private Double longitude;


    public String getFull_name(){
        return first_name + " " + last_name;
    }

    public Contact(){
        this.first_name = "";
        this.last_name = "";
        this.profile_picture = "";
        this.phone_number = "";
        this.birthday = null;
        this.email = "";
        this.latitude = Double.NaN;
        this.longitude = Double.NaN;
    }
    public Contact(String first_name, String last_name, String profile_picture, String phone_number, Date birthday, String email) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile_picture = profile_picture;
        this.phone_number = phone_number;
        this.birthday = birthday;
        this.email = email;
        this.latitude = Double.NaN;
        this.longitude = Double.NaN;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
