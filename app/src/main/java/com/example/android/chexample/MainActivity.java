package com.example.android.chexample;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = "Volley";
    LatLng point = new LatLng(0,0);
    RequestQueue queue;
    int cityOption = 0, stateOption = 0, countryOption = 0;
    Button submit;
    Intent i, i2;
    EditText roomNo, locality, zipCode;
    Spinner citySpinner, stateSpinner, countrySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "Turn GPS on and try again to get coordinates", Toast.LENGTH_SHORT).show();
        }
        i = new Intent(MainActivity.this, ResultActivity.class);

        submit = (Button) findViewById(R.id.button);
        roomNo = (TextInputEditText) findViewById(R.id.room_no);
        locality = (TextInputEditText) findViewById(R.id.locality);
        zipCode = (TextInputEditText) findViewById(R.id.zip_code);

        i2 = getIntent();
            Address a = i2.getParcelableExtra("fieldValues");
            if(a != null) {
                roomNo.setText(a.room);
                locality.setText(a.locality);
                zipCode.setText(a.zipCode);
            }


        queue = Volley.newRequestQueue(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (roomNo.getText().toString().isEmpty()){
                    roomNo.setError("Enter room no.");
                    return;
                }

                if (locality.getText().toString().isEmpty()){
                    locality.setError("Enter Locality");
                    return;
                }

                if (zipCode.getText().toString().isEmpty()){
                    zipCode.setError("Enter zipCode");
                    return;
                }


                Address address = new Address(roomNo.getText().toString(), locality.getText().toString(), zipCode.getText().toString(),
                        Integer.toString(cityOption), Integer.toString(stateOption),Integer.toString(countryOption),
                        Double.toString(point.longitude), Double.toString(point.latitude));

                updateAddress(address);

            }
        });


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        citySpinner = (Spinner) findViewById(R.id.city_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.city_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityOption = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                cityOption = 1;
            }
        });

        stateSpinner = (Spinner) findViewById(R.id.state_spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.state_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(adapter2);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stateOption = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                stateOption = 1;
            }
        });

        countrySpinner = (Spinner) findViewById(R.id.country_spinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.country_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter3);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                countryOption = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                countryOption = 1;
            }
        });

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        queue.cancelAll(TAG);
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION )
                != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                   1);}
        else{
            getDigits();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getDigits();

                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    public void getDigits(){
        Location mLastLocation;
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                Double lat = mLastLocation.getLatitude();
                Double lon = mLastLocation.getLongitude();
                Toast.makeText(this, Double.toString(lat) + ", " + Double.toString(lon), Toast.LENGTH_SHORT).show();
                point = new LatLng(lat,lon);

            }
        } catch(SecurityException e){
                e.printStackTrace();
        }
    }

    public void updateAddress(final Address address){
        String url = "http://ec2-35-154-15-217.ap-south-1.compute.amazonaws.com:8080" +
                "/campushaatTestAPI/webapi/users/createAddress";

        JSONObject json = address.toJSON();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                i.putExtra("address", address);
                startActivity(i);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(request);
        request.setTag(TAG);
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
