package com.example.android.chexample;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Address implements Parcelable {
     String room;
     String locality;
     String zipCode;
     String city;
     String state;
     String country;
     String longitude;
     String latitude;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(room);
        parcel.writeString(locality);
        parcel.writeString(zipCode);
        parcel.writeString(city);
        parcel.writeString(state);
        parcel.writeString(longitude);
        parcel.writeString(latitude);
        parcel.writeString(country);

    }

    public Address(){}

    public Address(String room, String locality,
                   String zipCode, String city, String state,
                   String country, String longitude, String latitude){
        this.room = room;
        this.locality = locality;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
        this.state = state;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public JSONObject toJSON() {

        JSONObject jo = new JSONObject();
        try {
            jo.put("room", room);
            jo.put("locality", locality);
            jo.put("city", city);
            jo.put("state", state);
            jo.put("zipCode", zipCode);
            jo.put("country", country);
            jo.put("longitude", longitude);
            jo.put("latitude", latitude);
        } catch(JSONException e){
            e.printStackTrace();
        }
        return jo;
    }

    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>() {
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    private Address(Parcel in) {
        room = in.readString();
        locality = in.readString();
        zipCode = in.readString();
        city = in.readString();
        state = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        country = in.readString();

    }

}
