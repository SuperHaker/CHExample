package com.example.android.chexample;


import org.json.JSONException;
import org.json.JSONObject;

public class Address {
    private String room;
    private String locality;
    private String zipCode;
    private String city;
    private String state;
    private String country;
    private String longitude;
    private String latitude;

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

}
