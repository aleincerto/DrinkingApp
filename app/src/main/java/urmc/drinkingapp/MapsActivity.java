package urmc.drinkingapp;

import android.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import urmc.drinkingapp.model.User;

/**
 * Maps activity displays the user's location and the buddy's location in real time if available
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int MY_LOCATION_REQUEST_CODE = 7;
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    // [START declare_database_ref]
    private DatabaseReference mBuddyReference;
    // [END declare_database_ref]
    private User mUser;
    private User mBuddy;
    private Marker marker;
    private Double mBuddyLat;
    private Double mBuddyLon;
    private Boolean haveBuddy = false;
    private String mKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        // [START initialize_database_ref]
        mBuddyReference = mDatabase.child("users").child(getUid()).child("buddy");
        // [END initialize_database_ref]

/*
        mBuddyReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()==null){
                    Log.d("MAPS","Null Buddy");
                    haveBuddy = false;
                }else {
                    //mAreWeFriends = dataSnapshot.getValue(Boolean.class);
                    if (dataSnapshot.getValue(String.class)!=null) {
                        mKey = dataSnapshot.getValue(String.class);
                        Log.e("MAPS",mKey);
                        haveBuddy = true;
                        mBuddyReference = mDatabase.child("users").child(mKey);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_LOCATION_REQUEST_CODE);
        }
        // Acquire a reference to the system Location Manager
        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                updateLocation(location);
                LatLng newloc = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(newloc));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);



        //Get the location of the buddy if available
        mBuddyReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()==null){
                    Log.d("MAPS","Null Buddy");
                    haveBuddy = false;
                }else {
                    //mAreWeFriends = dataSnapshot.getValue(Boolean.class);
                    if (dataSnapshot.getValue(String.class)!=null) {
                        mKey = dataSnapshot.getValue(String.class);
                        Log.e("MAPS",mKey);
                        haveBuddy = true;
                        mBuddyReference = mDatabase.child("users").child(mKey);
                        Log.e("MAPS",haveBuddy.toString());
                        if (haveBuddy) {
                            Log.e("LATREF", mBuddyReference.child("lat").toString());
                            mBuddyReference.child("lat").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    mBuddyLat = dataSnapshot.getValue(Double.class);
                                    Log.e("MAPS", "DATA IS CHANGING" + mBuddyLat);
                                    // [START_EXCLUDE]
                                    if (mBuddyLat == null) {
                                        // User is null, error out
                                        Log.e("MAPS", "Buddy " + " is unexpectedly null");
                                        Toast.makeText(MapsActivity.this,
                                                "Friend location not available",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.e("LONREF", mBuddyReference.child("lon").toString());
                                        mBuddyReference.child("lon").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                mBuddyLon = dataSnapshot.getValue(Double.class);
                                                Log.e("MAPS", "DATA IS CHANGING" + mBuddyLon);
                                                // [START_EXCLUDE]
                                                if (mBuddyLon == null) {
                                                    // User is null, error out
                                                    Log.e("MAPS", "Buddy " + " is unexpectedly null");
                                                    Toast.makeText(MapsActivity.this,
                                                            "Error: could not fetch user location.",
                                                            Toast.LENGTH_SHORT).show();
                                                } else {
                                                    mBuddyReference.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            mBuddy = dataSnapshot.getValue(User.class);
                                                            Log.e("MAPS", "DATA IS CHANGING");
                                                            // [START_EXCLUDE]
                                                            if (mBuddy == null) {
                                                                // User is null, error out
                                                                Log.e("MAPS", "Buddy " + " is unexpectedly null");
                                                                Toast.makeText(MapsActivity.this,
                                                                        "Error: could not fetch user.",
                                                                        Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                if (mBuddyLat != null && mBuddyLon != null) {
                                                                    if (marker != null) {
                                                                        marker.remove();
                                                                    }
                                                                    LatLng buddyLocation = new LatLng(mBuddyLat, mBuddyLon);
                                                                    marker = mMap.addMarker(new MarkerOptions()
                                                                            .position(buddyLocation)
                                                                            .title(mBuddy.getFullname()));
                                                                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_icon));
                                                                    marker.showInfoWindow();
                                                                    //mMap.addMarker(new MarkerOptions().position(buddyLocation).title(mBuddy.getFullname())).showInfoWindow();
                                                                } else {
                                                                    Log.e("MAPS", "Buddy " + " no Location");
                                                                    Toast.makeText(MapsActivity.this,
                                                                            "Buddy location not available",
                                                                            Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                            Log.w("MAPS", "getUser:onCancelled", databaseError.toException());
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Log.w("MAPS", "getUser:onCancelled", databaseError.toException());
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w("MAPS", "getUser:onCancelled", databaseError.toException());
                                }
                            });

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //mMap.setMyLocationEnabled(true);
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    public String getUid() {return FirebaseAuth.getInstance().getCurrentUser().getUid();}

    //update own location in the database for buddy to see it
    public void updateLocation(final Location location){
        mDatabase.child("users").child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);

                // [START_EXCLUDE]
                if (mUser == null) {
                    // User is null, error out
                    Log.e("MAPS", "User " + getUid() + " is unexpectedly null");
                    Toast.makeText(MapsActivity.this,
                            "Error: could not fetch user.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    mUser.Lat = location.getLatitude();
                    mUser.Lon = location.getLongitude();
                    mDatabase.child("users").child(getUid()).child("lat").setValue(mUser.Lat);
                    mDatabase.child("users").child(getUid()).child("lon").setValue(mUser.Lon);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("MAPS", "getUser:onCancelled", databaseError.toException());
            }
        });
    }


}
