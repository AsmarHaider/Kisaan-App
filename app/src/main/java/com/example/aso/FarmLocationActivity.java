package com.example.aso;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chootdev.csnackbar.Align;
import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Snackbar;
import com.chootdev.csnackbar.Type;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

public class FarmLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
        private CameraPosition mCameraPosition;
       LocationFinder locationFinder;
       mehdi.sakout.fancybuttons.FancyButton AddLocation;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    double lati=0.0,longi=0.0;

    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    FirebaseAuth firebaseAuth;


    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        AddLocation=findViewById(R.id.add_this_location_button);
firebaseAuth=FirebaseAuth.getInstance();
        AddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lati==0.0 || longi==0.0)
                {
                    // no location yet ..
                }
                else
                {
                    saveLocation(lati,longi);
                }

            }
        });

        getLocationPermission();

        Snackbar.with(this,null)
                .type(Type.SUCCESS)
                .message("Tap anywhere on map to select farm location.")
                .duration(Duration.LONG)
                .fillParent(true)
                .textAlign(Align.CENTER)
                .show();

    }


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
     //   updateLocationUI();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mLocationPermissionGranted)
        {
            locationFinder=new LocationFinder(this);
            if (locationFinder.canGetLocation())
            {
                 lati=locationFinder.getLatitude();
                longi=locationFinder.getLongitude();
              //  Toast.makeText(FarmLocationActivity.this,""+lati+"   "+longi,Toast.LENGTH_SHORT).show();
                LatLng CurrentLocation = new LatLng(lati,longi);
                mMap.addMarker(new MarkerOptions().position(CurrentLocation).title("Current Location"));
                // lat= 33.9946
                // long = 72.9106
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(CurrentLocation));
            }
            else {
                Toast.makeText(FarmLocationActivity.this, "Can't get location, Try Again.", Toast.LENGTH_SHORT).show();
            }
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                LatLng CurrentLocation = new LatLng(point.latitude,point.longitude);
                lati=point.latitude;
                longi=point.longitude;
                mMap.addMarker(new MarkerOptions().position(CurrentLocation).title("Current Location"));
                // lat= 33.9946
                // long = 72.9106
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(CurrentLocation));
                //saveLocation(point.latitude,point.longitude);
            }
        });
    }

    public void saveLocation(final double lati, final double longi) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
        alertDialog.setTitle("Farm Location.");
        alertDialog.setMessage("Do you want to save this location for Customers to locate your Farm?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               // save these coordinates ..

                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
                databaseReference=databaseReference.child("DSMUsers");
                HashMap<String,Object> hashMap=new HashMap<>();
                String latitude=""+lati;
                String longitude=""+longi;
                hashMap.put("latitude",latitude);
                hashMap.put("longitude",longitude);
                databaseReference.child(firebaseAuth
                        .getCurrentUser()
                        .getUid())
                        .updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(locationFinder, "Location Added", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FarmLocationActivity.this,FarmProductsActivity.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }



}
