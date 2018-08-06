package hmf.anyasoft.es.surveyapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import hmf.anyasoft.es.surveyapp.api.ApiService;
import hmf.anyasoft.es.surveyapp.api.LocCaptureApi;
import hmf.anyasoft.es.surveyapp.api.LocCaptureClient;
import hmf.anyasoft.es.surveyapp.api.RetroClient;
import hmf.anyasoft.es.surveyapp.api.response.UploadImageResult;
import hmf.anyasoft.es.surveyapp.domains.IdDetails;
import hmf.anyasoft.es.surveyapp.domains.PropertiesRes;
import hmf.anyasoft.es.surveyapp.permission.PermissionsChecker;
import hmf.anyasoft.es.surveyapp.utils.InternetConnection;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by home on 4/4/2018.
 */

public class ScreenshotAndCamera extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener,View.OnClickListener {

    private CheckBox mWaitForMapLoadCheckBox;
    ImageView snapshotHolder;
    public GoogleMap mMap;
    GpsTracker gps;
    LatLng userloc;
    String add;
    ArrayList<LatLng> coordinates;
    List<String>  stringList;
    Geocoder geocoder;
    List<Address> addresses;
    public static String GeoImagePath;
    public static String GridViewDemo_ImagePath;
    double latitude,longitude;

    String phone,idnum,idtype,loc,locImage,_path;
    private List<String> listOfImagesPath;
    TextView lat;

    Button captureBtn = null;
    final int CAMERA_CAPTURE = 1;
    private Uri picUri;
    Button submit,cap_cancle;
    private GridView grid;
    PermissionsChecker checker;
    public GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_capture);
        getSupportActionBar().hide();
        submit=(Button)findViewById(R.id.submit);
        captureBtn = (Button)findViewById(R.id.capture_btn1);
        cap_cancle=(Button)findViewById(R.id.cap_cancle);
        Bundle bundle = getIntent().getExtras();
        phone = String.valueOf(bundle.getString("phone"));
        idnum=String.valueOf(bundle.getString("idnum"));
        idtype=String.valueOf(bundle.getString("idtype"));
        coordinates = getIntent().getParcelableArrayListExtra("coordinates");
        coordinates.add(coordinates.get(0));
        mWaitForMapLoadCheckBox = (CheckBox)findViewById(R.id.wait_for_map_load);
        snapshotHolder = (ImageView)findViewById(R.id.snapshot_holder);
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        captureBtn.setOnClickListener(this);
        lat=(TextView)findViewById(R.id.lat);
        grid = (GridView) findViewById(R.id.gridviewimg);
        checker = new PermissionsChecker(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(ScreenshotAndCamera.this,
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

        gps=new GpsTracker(this);
        loc= String.valueOf(gps.getLatitude()+"_"+gps.getLongitude());
        lat.setText("Location: "+add);
        GridViewDemo_ImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HMF/"+phone+"/"+loc+"/";
        GeoImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Geo/"+phone+"/"+loc+"/";

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        PolylineOptions polylineOptions = new PolylineOptions();

// Create polyline options with existing LatLng ArrayList
       /* polylineOptions.addAll(coordinates);
        polylineOptions
                .width(18)
                .color(Color.parseColor("#4285f4"));
// Adding multiple points in map using polyline and arraylist
        mMap.addPolyline(polylineOptions);
*/

        Polygon UCCpolygon = mMap.addPolygon(new PolygonOptions()
                .addAll(coordinates)
                .strokeColor(Color.BLACK)
                .fillColor(0x2F00FF00));
             String veera= String.valueOf(SphericalUtil.computeArea(coordinates));
              Log.e("veera",veera);


        cap_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File root = new File(GridViewDemo_ImagePath);
                File[] Files = root.listFiles();
                if(Files != null) {
                    int j;
                    for(j = 0; j < Files.length; j++) {
                        System.out.println(Files[j].getAbsolutePath());
                        System.out.println(Files[j].delete());


                    }
                }

                grid.setAdapter(null);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("paths", String.valueOf(listOfImagesPath));

                if(!TextUtils.isEmpty(_path)){
                    if (!TextUtils.isEmpty(String.valueOf(listOfImagesPath))) {

                        if (InternetConnection.checkConnection(ScreenshotAndCamera.this)) {

                            /******************Retrofit***************/
                            uploadImage();

                        } else {
                            Toast.makeText(ScreenshotAndCamera.this,R.string.string_internet_connection_warning, Toast.LENGTH_LONG).show();
                            //Snackbar.make(parentView, R.string.string_internet_connection_warning, Snackbar.LENGTH_INDEFINITE).show();
                        }
                    }
                }
                else {

                    Toast.makeText(ScreenshotAndCamera.this,R.string.string_message_to_attach_file, Toast.LENGTH_LONG).show();
                    //Snackbar.make(parentView, R.string.string_message_to_attach_file, Snackbar.LENGTH_INDEFINITE).show();
                }

    /*  File root = new File(GridViewDemo_ImagePath);
        File[] Files = root.listFiles();
        if(Files != null) {
            int j;
            for(j = 0; j < Files.length; j++) {
                System.out.println(Files[j].getAbsolutePath());
                System.out.println(Files[j].delete());
            }
        }*/

                // grid.setAdapter(null);
            }
        });
        listOfImagesPath = null;
        listOfImagesPath = RetriveCapturedImagePath();
        if(listOfImagesPath!=null){
            grid.setAdapter(new ImageListAdapter(this,listOfImagesPath));
        }


    }

    private void uploadImage() {

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(ScreenshotAndCamera.this);
        progressDialog.setMessage(getString(R.string.string_title_upload_progressbar_));
        progressDialog.show();
        File file = new File(_path);

        //Create Upload Server Client
        ApiService service = RetroClient.getApiService();
        //File creating from selected URL
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        //Log.e("request", String.valueOf(requestFile));
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

        MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[listOfImagesPath.size()];
        Log.e("veera",String.valueOf( surveyImagesParts));

        for (int index = 0; index < listOfImagesPath.size(); index++) {
            file = new File(listOfImagesPath.get(index));
            RequestBody surveyBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            surveyImagesParts[index] = MultipartBody.Part.createFormData("SurveyImage", file.getName(), surveyBody);

        }
          stringList= new ArrayList<String>();
        stringList=MapsActivity.arrayList;
        stringList.add(stringList.get(0));

        PropertiesRes propertiesRes= new PropertiesRes();
        propertiesRes.setLandLordPhoneNumber(phone);
        propertiesRes.setBoundaryPoints(stringList);
        IdDetails idDetails=new IdDetails();
        idDetails.setIdProofType(idtype);
        idDetails.setProofId(idnum);
        propertiesRes.setIdDetails(idDetails);
        propertiesRes.setTenantId(ProfileActivity.tid);
        propertiesRes.setTenantName(ProfileActivity.tname);
        Log.e("veera",propertiesRes.toString());


       /* JsonObject main=new JsonObject();
        JsonObject idproof = new JsonObject();
        JsonArray imagearray=new JsonArray();
      *//*  main.addProperty("landDetailsId","");
        main.addProperty("landLordId","");*//*
        main.addProperty("landLordPhoneNumber",phone);
        main.add("boundaryPoints",MapsActivity.arrayList);
        main.add("imageUrls",imagearray);
        idproof.addProperty("proofId",idnum);
        idproof.addProperty("idProofType",idtype);
        main.add("idProof",idproof);
        main.addProperty("tenantId","5a7be4771794c2c2439f6cb8");
        main.addProperty("tenantName","Kiran");
        Log.e("veera",main.toString());
        Gson gson = new Gson();
        String json = gson.toJson(main);
      //  writeToFile(main.toString());
        Log.e("json",json);*/
        Call<UploadImageResult> resultCall = service.uploadImage(surveyImagesParts,propertiesRes,idnum);

//        Call<Result> resultCall = service.test();
        // finally, execute the request
        resultCall.enqueue(new Callback<UploadImageResult>() {
            @Override
            public void onResponse(Call<UploadImageResult> call, Response<UploadImageResult> response) {
                progressDialog.dismiss();
                _path = "";
                if(response.isSuccessful()){

                    Intent i= new Intent(ScreenshotAndCamera.this,ProfileActivity.class);
                    startActivity(i);

                   // readFromFile();

               /* File root = new File(GridViewDemo_ImagePath);
                    File[] Files = root.listFiles();
                    if(Files != null) {
                        int j;
                        for(j = 0; j < Files.length; j++) {
                            System.out.println(Files[j].getAbsolutePath());
                            System.out.println(Files[j].delete());
                        }
                    }
               */
                    grid.setAdapter(null);


                    Toast.makeText(ScreenshotAndCamera.this,"Successfully Uploaded", Toast.LENGTH_LONG).show();

                }
                else {

                    //  Toast.makeText(MainActivity.this,"Something went wrong,please try Again", Toast.LENGTH_LONG).show();


                }
            }

            @Override
            public void onFailure(Call<UploadImageResult> call, Throwable t) {

                Toast.makeText(ScreenshotAndCamera.this,"Something is missing,please try Again", Toast.LENGTH_LONG).show();

                progressDialog.dismiss();
            }
        });



    }


    public void onScreenshot(View view) {
        takeSnapshot();
    }

    private void takeSnapshot() {
        if (mMap == null) {
            return;
        }


        final GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                // Callback is called from the main thread, so we can modify the ImageView safely.
                snapshotHolder.setImageBitmap(snapshot);
                String imgcurTime = (new Date()).getTime()+"";//dateFormat.format(new Date());
                File imageDirectory = new File(GeoImagePath);
                imageDirectory.mkdirs();
                locImage = GeoImagePath +imgcurTime+".jpg";
                Log.e("path", String.valueOf(locImage));
                try {
                    FileOutputStream out = new FileOutputStream(locImage);
                    snapshot.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                listOfImagesPath = null;
                listOfImagesPath = RetriveLocCapturedImagePath();

            }
        };

        if (mWaitForMapLoadCheckBox.isChecked()) {
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mMap.snapshot(callback);
                }
            });
        } else {

            mMap.snapshot(callback);
        }
    }

    private List<String> RetriveLocCapturedImagePath() {

        List<String> tFileList = new ArrayList<String>();
        File f = new File(GeoImagePath);
        if (f.exists()) {
            File[] files=f.listFiles();
            Arrays.sort(files);

            for(int i=0; i<files.length; i++){
                File file = files[i];
                if(file.isDirectory())
                    continue;
                tFileList.add(file.getPath());
            }
        }
        return tFileList;
    }

    /**
     * Called when the clear button is clicked.
     */
    public void onClearScreenshot(View view) {

        File root = new File(GeoImagePath);
        File[] Files = root.listFiles();
        if(Files != null) {
            int j;
            for(j = 0; j < Files.length; j++) {
                System.out.println(Files[j].getAbsolutePath());
                System.out.println(Files[j].delete());
            }
        }

        snapshotHolder.setImageDrawable(null);

    }

    public  void onSaveScreenshot(View view){

        if(TextUtils.isEmpty(locImage)){

            Toast.makeText(ScreenshotAndCamera.this,"plz Capture Image",Toast.LENGTH_LONG).show();

        }
        else {

            File file = new File(locImage);
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(ScreenshotAndCamera.this);
            progressDialog.setMessage(getString(R.string.string_title_upload_progressbar_));
            progressDialog.show();

            LocCaptureApi service = LocCaptureClient.getApiService();
            //File creating from selected URL
            // create RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            //Log.e("request", String.valueOf(requestFile));
            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

            MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[listOfImagesPath.size()];
            Log.e("veera",String.valueOf( surveyImagesParts));

            for (int index = 0; index < listOfImagesPath.size(); index++) {
                file = new File(listOfImagesPath.get(index));
                RequestBody surveyBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                surveyImagesParts[index] = MultipartBody.Part.createFormData("SurveyImage", file.getName(), surveyBody);
                Call<UploadImageResult> resultCall = service.uploadImage(surveyImagesParts,idnum);

//        Call<Result> resultCall = service.test();
                // finally, execute the request
                resultCall.enqueue(new Callback<UploadImageResult>() {
                    @Override
                    public void onResponse(Call<UploadImageResult> call, Response<UploadImageResult> response) {
                        _path = "";
                        if(response.isSuccessful()){

                            progressDialog.dismiss();
                            Toast.makeText(ScreenshotAndCamera.this,"Successfully Uploaded", Toast.LENGTH_LONG).show();


                        }
                        else {

                            progressDialog.dismiss();
                            Intent i= new Intent(getApplicationContext(),FarmerDetails.class);
                            startActivity(i);
                            Toast.makeText(ScreenshotAndCamera.this,response.message(), Toast.LENGTH_LONG).show();
                            //alertDialogBuilder.dismiss();

                        }
                    }

                    @Override
                    public void onFailure(Call<UploadImageResult> call, Throwable t) {
                        progressDialog.dismiss();
                        // alertDialogBuilder.dismiss();
                       /* Intent i= new Intent(getApplicationContext(),FarmerDetails.class);
                        startActivity(i);*/
                        Toast.makeText(ScreenshotAndCamera.this,"Something is missing,please try Again", Toast.LENGTH_LONG).show();

                    }
                });

            }
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

    private synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(ScreenshotAndCamera.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        gps=new GpsTracker(this);
        if(gps.canGetLocation){
            geocoder = new Geocoder(ScreenshotAndCamera.this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(gps.getLatitude(),gps.getLongitude(), 1);
                add=addresses.get(0).getAddressLine(0);
                isStoragePermissionGranted();

                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }
            //lat.setText(String.valueOf(location.getLatitude()+"_"+location.getLongitude()));
            loc= String.valueOf(gps.getLatitude()+"_"+gps.getLongitude());
            lat.setText("Location: "+add);
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
        if (ContextCompat.checkSelfPermission(ScreenshotAndCamera.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((this),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                if(gps.canGetLocation){

                    geocoder = new Geocoder(ScreenshotAndCamera.this, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(gps.getLatitude(),gps.getLongitude(), 1);
                        add=addresses.get(0).getAddressLine(0);
                        // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //lat.setText(String.valueOf(location.getLatitude()+"_"+location.getLongitude()));
                    loc= String.valueOf(gps.getLatitude()+"_"+gps.getLongitude());
                    lat.setText("Location: "+add);
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
                                ActivityCompat.requestPermissions(ScreenshotAndCamera.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(ScreenshotAndCamera.this,
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
                    Toast.makeText(ScreenshotAndCamera.this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if(gps.canGetLocation){

            userloc = new LatLng(gps.getLatitude(),gps.getLongitude());
           // changeloc=new LatLng(latitude,longitude);

            try {
                geocoder = new Geocoder(ScreenshotAndCamera.this, Locale.getDefault());

                addresses = geocoder.getFromLocation(gps.getLatitude(),gps.getLongitude(), 1);
                add=addresses.get(0).getAddressLine(0);
                isStoragePermissionGranted();

                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMap.addMarker(new MarkerOptions().position(userloc).title(add));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userloc));

            CameraPosition cameraPosition = CameraPosition.builder()
                    .target(userloc)
                    .zoom(18)
                    .bearing(0)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                    2000, null);

            if (ActivityCompat.checkSelfPermission(ScreenshotAndCamera.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ScreenshotAndCamera.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void onClick(View view) {
// TODO Auto-generated method stub
        if (view.getId() == R.id.capture_btn1) {

            try {
//use standard intent to capture an image
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//we will handle the returned data in onActivityResult
                startActivityForResult(captureIntent, CAMERA_CAPTURE);
            } catch(ActivityNotFoundException anfe){
//display an error message
                String errorMessage = "Whoops - your device doesn't support capturing images!";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
//user is returning from capturing an image using the camera
            if(requestCode == CAMERA_CAPTURE){
                Bundle extras = data.getExtras();
                Bitmap thePic = extras.getParcelable("data");
                String imgcurTime = (new Date()).getTime()+"";//dateFormat.format(new Date());
                File imageDirectory = new File(GridViewDemo_ImagePath);
                imageDirectory.mkdirs();
                _path = GridViewDemo_ImagePath +imgcurTime+".jpg";
                Log.e("path", String.valueOf(_path));
                try {
                    FileOutputStream out = new FileOutputStream(_path);
                    thePic.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                listOfImagesPath = null;
                listOfImagesPath = RetriveCapturedImagePath();
                Log.e("values", String.valueOf(listOfImagesPath));

                if(listOfImagesPath!=null){
                    grid.setAdapter(new ImageListAdapter(this,listOfImagesPath));
                }
            }
        }
    }

    private List<String> RetriveCapturedImagePath() {
        List<String> tFileList = new ArrayList<String>();
        File f = new File(GridViewDemo_ImagePath);
        if (f.exists()) {
            File[] files=f.listFiles();
            Arrays.sort(files);

            for(int i=0; i<files.length; i++){
                File file = files[i];
                if(file.isDirectory())
                    continue;
                tFileList.add(file.getPath());
            }
        }
        return tFileList;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        LocationRequest mLocationRequest = createLocationRequest();
        if (ContextCompat.checkSelfPermission(ScreenshotAndCamera.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
        latitude=location.getLatitude();
        longitude=location.getLongitude();

        loc= String.valueOf(latitude+"_"+longitude);
    }


    public class ImageListAdapter extends BaseAdapter
    {
        private Context context;
        private List<String> imgPic;
        public ImageListAdapter(Context c, List<String> thePic)
        {
            context = c;
            imgPic = thePic;
        }
        public int getCount() {
            if(imgPic != null)
                return imgPic.size();
            else
                return 0;
        }

        //---returns the ID of an item---
        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        //---returns an ImageView view---
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView imageView;
            BitmapFactory.Options bfOptions=new BitmapFactory.Options();
            bfOptions.inDither=false;                     //Disable Dithering mode
            bfOptions.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
            bfOptions.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
            bfOptions.inTempStorage=new byte[32 * 1024];
            if (convertView == null) {
                imageView = new ImageView(context);




                GridView.LayoutParams lp= new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT, GridView.LayoutParams.WRAP_CONTENT);
                //   lp.setMargins(left, top, right, bottom);

                // view.setLayoutParams(/* your layout params */); //where view is cell view
                imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT, GridView.LayoutParams.WRAP_CONTENT));
                imageView.setPadding(10, 10, 10, 10);
                // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }
            FileInputStream fs = null;
            Bitmap bm;
            try {
                fs = new FileInputStream(new File(imgPic.get(position).toString()));

                if(fs!=null) {
                    bm= BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
                    imageView.setImageBitmap(bm);
                    imageView.setId(position);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setLayoutParams(new GridView.LayoutParams(700, 700));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if(fs!=null) {
                    try {
                        fs.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return imageView;
        }
    }
}
