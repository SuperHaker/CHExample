package com.example.android.chexample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.android.chexample.R.id.map;

public class ResultActivity extends AppCompatActivity implements OnMapReadyCallback {
    Address address;
    TextView addressView, gpsOn;
    String cityName, stateName, countryName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        addressView = (TextView) findViewById(R.id.address_view);
        gpsOn = (TextView) findViewById(R.id.gps_on);
        address =  getIntent().getParcelableExtra("address");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

            cityName = getResources().getStringArray(R.array.city_array)[Integer.parseInt(address.city)-1];
            stateName = getResources().getStringArray(R.array.state_array)[Integer.parseInt(address.state)-1];
            countryName = getResources().getStringArray(R.array.country_array)[Integer.parseInt(address.country)-1];

        String add = address.room + ", " + address.locality + "\n" + cityName + "\n" + stateName + "\n"
                + countryName + " - " + address.zipCode;
        addressView.setText(add);
        addressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResultActivity.this, MainActivity.class);
                i.putExtra("fieldValues", address);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mMap;
        mMap = googleMap;
        LatLng point = new LatLng(Double.parseDouble(address.latitude), Double.parseDouble(address.longitude));
        if (point == new LatLng(0,0)){
            gpsOn.setVisibility(View.VISIBLE);
        }

        mMap.addMarker(new MarkerOptions().position(point)).setTitle("Your Location");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Uri gmmIntentUri = Uri.parse("geo:" + address.latitude + "," + address.longitude + "?q=" + address.latitude +
                        "," + address.longitude + "(Your Location)");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }
}
