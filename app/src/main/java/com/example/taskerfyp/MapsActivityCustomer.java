package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.taskerfyp.Models.TaskerUser;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivityCustomer extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        LocationListener

{

    private GoogleMap mMap;

    //Play Services
    private static final int MY_PERMISSION_REQUEST_CODE = 7000;
    private static final int PLAY_SERVICE_RES_REQUEST = 7001;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private Location lastlocation;
    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;
   private DatabaseReference customer_lat_lng_ref;
    private GeoFire geoFire;

    Marker markerCurrent;
    SwitchCompat switchButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_customer);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        switchButton = findViewById(R.id.switch_button);

        SharedPreferences sharedPreferences = getSharedPreferences("save" , MODE_PRIVATE);
        switchButton.setChecked(sharedPreferences.getBoolean("value" , false));

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchButton.isChecked())
                {
                    SharedPreferences.Editor editor = getSharedPreferences("save" , MODE_PRIVATE).edit();
                    editor.putBoolean("value" , true);
                    editor.apply();
                    switchButton.setChecked(true);

                    startlocationUpdates();
                    displayLocation();

                    Toast.makeText(MapsActivityCustomer.this,
                            "You are Online & Your current location is shairing with taskers ", Toast.LENGTH_SHORT).show();
                }
                else {

                    SharedPreferences.Editor editor = getSharedPreferences("save" , MODE_PRIVATE).edit();
                    editor.putBoolean("value" , false);
                    editor.apply();
                    switchButton.setChecked(false);

                    stopLoationUpdates();

                    markerCurrent.remove();
                    customer_lat_lng_ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(false);
                    mMap.clear();

                    Toast.makeText(MapsActivityCustomer.this, "You are Offline ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setUpLocation();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //googleMap.setMyLocationEnabled(true); // Location btn is enabled by setMyLocationEnabled
        statusCheck();

    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        Toast.makeText(MapsActivityCustomer.this, "You are online", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(checkPlayServices())
                    {
                        buildGoogleApiClient();
                        creatLocationReaquest();
                        if(switchButton.isChecked())
                        {

                            displayLocation();
                        }
                    }

                }


        }

    }

    private void setUpLocation() {

        if(ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            //Request Runtime permission
            ActivityCompat.requestPermissions(this , new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            } , MY_PERMISSION_REQUEST_CODE);
        }
        else {
            if(checkPlayServices())
            {
                buildGoogleApiClient();
                creatLocationReaquest();
                if(switchButton.isChecked())
                {
                    displayLocation();
                }
            }
        }
    }

    private void creatLocationReaquest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICE_RES_REQUEST).show();
            else {
                Toast.makeText(this, "This is not supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return  true;
    }

    private void loadAllAvailableTasker() {
        //load all available driver in distance 3km
        DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference("Tasker_lat_lng");
        GeoFire geoFire = new GeoFire(driverLocation);

        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(lastlocation.getLatitude() , lastlocation.getLongitude()) , 8000 );
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, final GeoLocation location) {

                FirebaseDatabase.getInstance().getReference("Users").child("Tasker")
                        .child(key)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                TaskerUser taskerUser = dataSnapshot.getValue(TaskerUser.class);

                                //Add driver to map
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(location.latitude , location.longitude))
                                        .flat(true)
                                        .title(taskerUser.getTaskerUsername())
                                        .snippet("Phone : "+taskerUser.getTaskerPhonenumber())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }


    private void stopLoationUpdates() {

        if(ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient ,  this);
    }

    private void displayLocation() {
        if(ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        lastlocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(lastlocation != null)
        {
            if(switchButton.isChecked())
            {

                customer_lat_lng_ref = FirebaseDatabase.getInstance().getReference("Customer_lat_lng");
                geoFire = new GeoFire(customer_lat_lng_ref);
                final double latitude = lastlocation.getLatitude();
                final double longitude = lastlocation.getLongitude();

                geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {

                        //Add Marker
                        if (markerCurrent != null)
                            markerCurrent.remove(); //Remove already marker
                        markerCurrent = mMap.addMarker(new MarkerOptions()
                                //.title(String.valueOf(BitmapDescriptorFactory.fromResource(R.drawable.car)))
                                .position(new LatLng(latitude , longitude))
                                .title("Your Location"));

                        //Move marker to this position
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude , longitude) , 15.0f));

                            loadAllAvailableTasker();
                    }
                });

            }
        }
        else {
            Log.d("ERROR" , "Cannot get your location");
        }
    }


    private void startlocationUpdates() {
        if(ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient , locationRequest ,  this);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startlocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastlocation = location;
        displayLocation();
    }
}
