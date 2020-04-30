package com.example.aso;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class NearbyFarmActivity extends FragmentActivity implements OnMapReadyCallback,
                GoogleMap.OnPolylineClickListener {

    private GoogleMap mMap;
    int COLOR_POLYLINE = 0xff8ed141;
    private static final int PATTERN_GAP_LENGTH_PX = 12;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    List<Polyline> polylines;
    // Create a stroke pattern of a gap followed by a dot.
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);
    String ReferenceCategory="";
    FirebaseAuth firebaseAuth;
    double lati=0.0,longi=0.0;
    LocationFinder locationFinder;

    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_farm);
        ReferenceCategory=getIntent().getStringExtra("cat");
        firebaseAuth=FirebaseAuth.getInstance();
        getLocationPermission();
        polylines = new ArrayList<Polyline>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void showDialoge(final double destination_lati, final double destination_longi) {

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(NearbyFarmActivity.this);
        alertDialog.setTitle("Need Directions?");

        alertDialog.setPositiveButton("Draw Route",new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+destination_lati+","+destination_longi);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        }

                    }
                });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        alertDialog.create().show();
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

                // here fetch the coordinates of farmers who do offer the desired product ..
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("DSMUsers");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
try {


    for (DataSnapshot ds : dataSnapshot.getChildren()) {

        String lati = ds.child("latitude").getValue().toString();
        String longi = ds.child("longitude").getValue().toString();
        String name = ds.child("name").getValue().toString();
        if (ds.child("farmer").getValue().toString().equalsIgnoreCase("yes")) {
            if ((lati.equalsIgnoreCase("0.0") || longi.equalsIgnoreCase("0.0"))
                    || firebaseAuth.getCurrentUser().getUid().equalsIgnoreCase(ds.child("uid").getValue().toString())) {
            } else {
                if (ds.child(ReferenceCategory).getValue().toString().equalsIgnoreCase("yes")) { // means if coordinates are okay ..
                    double lati_d = Double.parseDouble(lati);
                    double longi_d = Double.parseDouble(longi);
                    Toast.makeText(NearbyFarmActivity.this, lati + "......." + longi, Toast.LENGTH_SHORT).show();
                    // here conversion ffrom string to double is remaining ..

                    LatLng Location = new LatLng(lati_d, longi_d);
                    mMap.addMarker(new MarkerOptions().position(Location).title(name));
                }

            }

        }
    }
}
catch (Exception e)
{
    Toast.makeText(NearbyFarmActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            else {
                Toast.makeText(NearbyFarmActivity.this, "Can't get location, Try Again.", Toast.LENGTH_SHORT).show();
            }
        }


    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {

            if (marker.getTitle().equalsIgnoreCase("Current location")) {
                return true;
            } else {


                double lati_destination = marker.getPosition().latitude;
                double longi_destination = marker.getPosition().longitude;
                // draw route between two points ..

                for(Polyline line : polylines)
                {
                    line.remove();
                }
                polylines.clear();

                Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(lati_destination, longi_destination),
                                new LatLng(lati, longi)));
                polylines.add(polyline1);

                // Position the map's camera near Alice Springs in the center of Australia,
                // and set the zoom factor so most of Australia shows on the screen.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lati_destination,longi_destination),2));

                // Set listeners for click events.
                mMap.setOnPolylineClickListener(NearbyFarmActivity.this);
                polyline1.setEndCap(new RoundCap());
                polyline1.setPattern(PATTERN_POLYLINE_DOTTED);
                polyline1.setWidth(8);
                polyline1.setColor(COLOR_POLYLINE);
                polyline1.setJointType(JointType.ROUND);
                // Toast.makeText(NearbyFarmActivity.this,"marker is clicked.", Toast.LENGTH_SHORT).show();
                showDialoge(lati_destination,longi_destination);
                return true;
            }
        }
    });
    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        polylines.clear();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    polylines.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
    //polylines.clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    //polylines.clear();
    }
}
