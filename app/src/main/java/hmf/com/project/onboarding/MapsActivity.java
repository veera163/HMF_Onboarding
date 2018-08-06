package hmf.anyasoft.es.surveyapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private GoogleMap mMap;
    GpsTracker gps;
    LatLng userloc,changeloc;
    double latitude,longitude;
    Geocoder geocoder;
    List<Address> addresses;
    String add;
    Marker mMarker;
    public GoogleApiClient mGoogleApiClient;
   public static ArrayList<LatLng> coordList;
   public static ArrayList<String> arrayList;
    String stringlatlog;
    TextView lat;
    Button preview,clear;
    String phone,idnum,idtype;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Bundle bundle = getIntent().getExtras();
        phone = String.valueOf(bundle.getString("phone"));
        idnum=String.valueOf(bundle.getString("idnum"));
        idtype=String.valueOf(bundle.getString("idtype"));
        coordList = new ArrayList<LatLng>();
        arrayList = new ArrayList<String>();
        lat=(TextView)findViewById(R.id.lat);
        preview=(Button)findViewById(R.id.preview);
        clear=(Button)findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                coordList.removeAll(coordList);
                arrayList.removeAll(arrayList);
            }
        });

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(coordList==null){

                    Toast.makeText(MapsActivity.this,"capture Locations",Toast.LENGTH_LONG).show();
                    Log.e("size",coordList.toString());


                }else  if(coordList.size()==0){

                    Toast.makeText(MapsActivity.this,"capture Locations",Toast.LENGTH_LONG).show();
                    Log.e("size",coordList.toString());

                }else {

                   /* coordList.add( coordList.get(0));
                    arrayList.add(arrayList.get(0));*/
                    Log.e("size",coordList.toString());

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("coordinates", coordList);
                    coordList = bundle.getParcelableArrayList("coordinates");
                    Intent i= new Intent(MapsActivity.this,ScreenshotAndCamera.class);
                    i.putExtras(bundle);
                    i.putExtra("phone",phone);
                    i.putExtra("idnum",idnum);
                    i.putExtra("idtype",idtype);
                    startActivity(i);
                }


            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(MapsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                //Location Permission already granted
                buildGoogleApiClient();

            } else {
                //Request Location Permission
                checkLocationPermission();
            }

        }

        else {

            buildGoogleApiClient();

        }

        gps=new GpsTracker(MapsActivity.this);
        lat.setText("Location: "+add);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    private synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(MapsActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        gps=new GpsTracker(this);
        if(gps.canGetLocation){
            isStoragePermissionGranted();

            geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
            latitude=gps.getLatitude();
            longitude=gps.getLongitude();
            try {
                addresses = geocoder.getFromLocation(gps.getLatitude(),gps.getLongitude(), 1);
                add=addresses.get(0).getAddressLine(0);
                lat.setText("Location: "+add);

                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            //lat.setText(String.valueOf(location.getLatitude()+"_"+location.getLongitude()));
           /* loc= String.valueOf(gps.getLatitude()+"_"+gps.getLongitude());
            latlog= String.valueOf(gps.getLatitude()+","+gps.getLongitude());*/
        }
        else {
            gps.showSettingsAlert();
        }

    }
    private LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((this),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                if(gps.canGetLocation){
                    geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    latitude=gps.getLatitude();
                    longitude=gps.getLongitude();
                    try {
                        addresses = geocoder.getFromLocation(gps.getLatitude(),gps.getLongitude(), 1);
                        add=addresses.get(0).getAddressLine(0);
                        // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    lat.setText("Location: "+add);

                    //lat.setText(String.valueOf(location.getLatitude()+"_"+location.getLongitude()));
                  /*  loc= String.valueOf(gps.getLatitude()+"_"+gps.getLongitude());
                    latlog= String.valueOf(gps.getLatitude()+","+gps.getLongitude());
                    value=loc;*/
                }
                else {
                    gps.showSettingsAlert();
                }

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MapsActivity.this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                stringlatlog= String.valueOf(latLng.latitude+","+latLng.longitude);
                coordList.add(latLng);
                arrayList.add(stringlatlog);

                Log.e("loc",arrayList.toString());

               mMarker= mMap.addMarker(new MarkerOptions().position(latLng));

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                stringlatlog= String.valueOf(marker.getPosition().latitude+","+marker.getPosition().longitude);

                if(coordList.indexOf(marker.getPosition())!=-1) {
                   coordList.remove(coordList.indexOf(marker.getPosition()));
                   // Log.e("loc",coordList.toString());

                }
                if(arrayList.indexOf(stringlatlog)!=-1) {
                    arrayList.remove(arrayList.indexOf(stringlatlog));
                    Log.e("loc",arrayList.toString());

                }

                marker.remove();
                return true;
            }
        });
        // Add a marker in Sydney and move the camera
        if(gps.canGetLocation){
            isStoragePermissionGranted();
            latitude=gps.getLatitude();
            longitude=gps.getLongitude();

            userloc = new LatLng(latitude,longitude);
           // changeloc=new LatLng(latitude,longitude);

           try {
               geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                addresses= new ArrayList<Address>();
                addresses = geocoder.getFromLocation(latitude,longitude, 1);
                add=addresses.get(0).getAddressLine(0);
               //lat.setText("Location: "+add);


                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }
/*
            mMap.addMarker(new MarkerOptions().position(userloc).title(add));
*/
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userloc));
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            CameraPosition cameraPosition = CameraPosition.builder()
                    .target(userloc)
                    .zoom(18)
                    .bearing(0)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                    2000, null);

            if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling

                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            mMap.getUiSettings().setTiltGesturesEnabled(true);
            mMap.getUiSettings().setRotateGesturesEnabled(true);
        }
        else {
            gps.showSettingsAlert();
        }
    }

    public boolean isStoragePermissionGranted() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest mLocationRequest = createLocationRequest();
        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
      //  latitude=location.getLatitude();
       // longitude=location.getLongitude();
       // changeloc=new LatLng(latitude,longitude);

      /*  latlog= String.valueOf(latitude+","+longitude);
        loc= String.valueOf(latitude+"_"+longitude);*/
    }
}
