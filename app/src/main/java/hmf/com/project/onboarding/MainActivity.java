package hmf.com.project.onboarding;

import android.Manifest;
import android.support.v7.app.AlertDialog;
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
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;

import hmf.com.project.onboarding.api.ApiService;
import hmf.com.project.onboarding.api.LocCaptureApi;
import hmf.com.project.onboarding.api.LocCaptureClient;
import hmf.com.project.onboarding.api.RetroClient;
import hmf.com.project.onboarding.api.response.UploadImageResult;
import hmf.com.project.onboarding.domains.IdDetails;
import hmf.com.project.onboarding.domains.PropertiesRes;
import hmf.com.project.onboarding.permission.PermissionsChecker;
import hmf.com.project.onboarding.utils.InternetConnection;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

public class MainActivity extends FragmentActivity implements OnClickListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener, OnMapReadyCallback {
    Button captureBtn = null;
    final int CAMERA_CAPTURE = 1;
    private Uri picUri;
    TextView lat;
    String loc,locImage;
    JsonArray elements;
    Button submit,caploc,clearAll_loc,cap_cancle,preview;
    double latitude,longitude;
    public GoogleApiClient mGoogleApiClient;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private GridView grid;
    String _path,value,latlog,add;
    PermissionsChecker checker;
    private List<String> listOfImagesPath;
    Context mContext;
    public static String GridViewDemo_ImagePath;
    public static String GeoImagePath;

    Geocoder geocoder;
    List<Address> addresses;
    Marker mMarker;
    private ListView list;

    private CheckBox mWaitForMapLoadCheckBox;
    ImageView snapshotHolder,back;
    private Adpter adapter;
     ArrayList<String> arrayList;
    String phone,idnum,idtype;
    GpsTracker gps;
    String[] latlong;
    LatLng userloc,changeloc;
    AlertDialog alertDialogBuilder;
    public GoogleMap mMap;
   // PolylineOptions rectOptions;
   ArrayList<LatLng> coordList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_capture);
        Bundle bundle = getIntent().getExtras();
        gps=new GpsTracker(MainActivity.this);
        phone = String.valueOf(bundle.getString("phone"));
        idnum=String.valueOf(bundle.getString("idnum"));
        idtype=String.valueOf(bundle.getString("idtype"));
        submit=(Button)findViewById(R.id.submit);
        preview=(Button)findViewById(R.id.preview);
        captureBtn = (Button)findViewById(R.id.capture_btn1);
        caploc = (Button)findViewById(R.id.cap_location);
        clearAll_loc=(Button)findViewById(R.id.clearAll_loc);
        cap_cancle=(Button)findViewById(R.id.cap_cancle);
        list = (ListView) findViewById(R.id.listview);
        arrayList = new ArrayList<String>();
        coordList = new ArrayList<LatLng>();
        elements=new JsonArray();
        adapter = new Adpter(getApplicationContext(),arrayList,elements,coordList);
        // Here, you set the data in your ListView
        list.setAdapter(adapter);
        captureBtn.setOnClickListener(this);
        lat=(TextView)findViewById(R.id.lat);
        grid = (GridView) findViewById(R.id.gridviewimg);
        checker = new PermissionsChecker(this);
        gps=new GpsTracker(MainActivity.this);
/*
        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(gps.getLatitude(),gps.getLongitude(), 1);
            add=addresses.get(0).getAddressLine(0);
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //lat.setText(String.valueOf(location.getLatitude()+"_"+location.getLongitude()));
        loc= String.valueOf(gps.getLatitude()+"_"+gps.getLongitude());
        lat.setText("Location: "+add);
        latlog= String.valueOf(gps.getLatitude()+","+gps.getLongitude());
        value=loc;
        GridViewDemo_ImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HMF/"+phone+"/"+loc+"/";
        GeoImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Geo/"+phone+"/"+loc+"/";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(MainActivity.this,
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

        caploc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                changeloc=new LatLng(latitude,longitude);
                arrayList.add(latlog);
                elements.add(latlog);
                coordList.add(changeloc);

                adapter = new Adpter(MainActivity.this,arrayList,elements,coordList);
                // Here, you set the data in your ListView
                list.setAdapter(adapter);
                // next thing you have to do is check if your adapter has changed
                adapter.notifyDataSetChanged();

            }
        });
        clearAll_loc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                arrayList.clear();

                elements=new JsonArray();
                coordList.clear();
                ;

                adapter.notifyDataSetChanged();
                Log.e("veera",arrayList.toString());
                Log.e("veera",elements.toString());
                Log.e("veera",coordList.toString());



            }
        });
        preview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(coordList.size()==0){

                    Toast.makeText(MainActivity.this,"capture Locations",Toast.LENGTH_LONG).show();

                }else {

                    LatLng latLng=coordList.get(0);
                    coordList.add(latLng);
                    Log.e("veera",coordList.toString());
                    showInputDialog(arrayList,coordList);
                }



            }
        });

        cap_cancle.setOnClickListener(new OnClickListener() {
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
        submit.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View view) {

        Log.e("paths", String.valueOf(listOfImagesPath));

        if(!TextUtils.isEmpty(_path)){
            if (!TextUtils.isEmpty(String.valueOf(listOfImagesPath))) {

                if (InternetConnection.checkConnection(MainActivity.this)) {

                    /******************Retrofit***************/
                    uploadImage();

                } else {
                    Toast.makeText(MainActivity.this,R.string.string_internet_connection_warning, Toast.LENGTH_LONG).show();
                    //Snackbar.make(parentView, R.string.string_internet_connection_warning, Snackbar.LENGTH_INDEFINITE).show();
                }
            }
        }
        else {

            Toast.makeText(MainActivity.this,R.string.string_message_to_attach_file, Toast.LENGTH_LONG).show();
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

    private View showInputDialog(ArrayList<String> elements, ArrayList<LatLng> coordList) {


        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);

        View promptView = layoutInflater.inflate(R.layout.preview, null );

        mWaitForMapLoadCheckBox = (CheckBox)promptView.findViewById(R.id.wait_for_map_load);
         snapshotHolder = (ImageView)promptView.findViewById(R.id.snapshot_holder);
         back=(ImageView)promptView.findViewById(R.id.back);
         back.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View view) {

                // alertDialogBuilder.dismiss();
                Intent i= new Intent(MainActivity.this,FarmerDetails.class);
                 startActivity(i);
             }
         });

        alertDialogBuilder = new AlertDialog.Builder(MainActivity.this).create();
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.show();
        alertDialogBuilder.setCancelable(false);

        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.e("veera",coordList.toString());
        PolylineOptions polylineOptions = new PolylineOptions();

// Create polyline options with existing LatLng ArrayList
        polylineOptions.addAll(coordList);
        polylineOptions
                .width(18)
                .color(Color.parseColor("#4285f4"));
// Adding multiple points in map using polyline and arraylist
        mMap.addPolyline(polylineOptions);

     /*   ArrayList<String> listdata = new ArrayList<String>();
        if (elements != null) {

            for (int i=0;i<elements.size();i++){
                listdata.add(elements.get(i));
            }
        }
        for(int i = 0 ; i < listdata.size() ; i++ ) {
            latlong = listdata.get(i).split(",");
            Double lat1 = Double.parseDouble(latlong[0]);
            Double log = Double.parseDouble(latlong[1]);
            LatLng latLng=new LatLng(lat1,log);
            createMarker(latLng);

            Log.e("veea",latLng.toString());


        }*/



// Get back the mutable Polyline

     /*  if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
        polylinePaths = new ArrayList<>();
        PolylineOptions polylineOptions = new PolylineOptions().
                geodesic(true).
                color(Color.parseColor("#4285f4")).
                width(18);


        for (int i = 0; i < elements.size(); i++){
            StringTokenizer token = new StringTokenizer( elements.get(i).getAsString(),",");
            LatLng latLng= new LatLng(Double.parseDouble(token.nextToken()),Double.parseDouble(token.nextToken()));

            polylineOptions.add(latLng);
            Log.e("veera",polylineOptions.toString());


        }

       polylinePaths.add(mMap.addPolyline(polylineOptions));*/
     return promptView;
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

            Toast.makeText(MainActivity.this,"plz Capture Image",Toast.LENGTH_LONG).show();

        }
        else {

            File file = new File(locImage);
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(MainActivity.this);
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
                            Toast.makeText(MainActivity.this,"Successfully Uploaded", Toast.LENGTH_LONG).show();
                            preview.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Toast.makeText(MainActivity.this,"You already submit the preview Image at this Location",Toast.LENGTH_LONG).show();
                                }
                            });
                            alertDialogBuilder.dismiss();

                        }
                        else {

                            progressDialog.dismiss();
                            Intent i= new Intent(getApplicationContext(),FarmerDetails.class);
                            startActivity(i);
                            Toast.makeText(MainActivity.this,response.message(), Toast.LENGTH_LONG).show();
                            //alertDialogBuilder.dismiss();

                        }
                    }

                    @Override
                    public void onFailure(Call<UploadImageResult> call, Throwable t) {
                        progressDialog.dismiss();
                       // alertDialogBuilder.dismiss();
                       /* Intent i= new Intent(getApplicationContext(),FarmerDetails.class);
                        startActivity(i);*/
                        Toast.makeText(MainActivity.this,"Something is missing,please try Again", Toast.LENGTH_LONG).show();

                    }
                });

            }
        }
    }


    private Marker createMarker(LatLng latLng) {

        return mMarker= mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(add)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
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

        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        gps=new GpsTracker(this);
        if(gps.canGetLocation){
            geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            latitude=gps.getLatitude();
            longitude=gps.getLongitude();
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
            latlog= String.valueOf(gps.getLatitude()+","+gps.getLongitude());
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

    private void uploadImage() {

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(MainActivity.this);
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
        PropertiesRes propertiesRes= new PropertiesRes();
        propertiesRes.setLandLordPhoneNumber(phone);
        propertiesRes.setBoundaryPoints(MapsActivity.arrayList);
        propertiesRes.setImageUrls(listOfImagesPath);
        IdDetails idDetails=new IdDetails();
        idDetails.setIdProofType(idtype);
        idDetails.setProofId(idnum);
        propertiesRes.setIdDetails(idDetails);
        propertiesRes.setTenantId("5a7be4771794c2c2439f6cb8");
        propertiesRes.setTenantName("Kiran");


       /* JsonObject main=new JsonObject();
        JsonObject idproof = new JsonObject();
        JsonArray imagearray=new JsonArray();
        elements.add(elements.get(0));
      *//*  main.addProperty("landDetailsId","");
        main.addProperty("landLordId","");*//*
        main.addProperty("landLordPhoneNumber",phone);
        main.add("boundaryPoints",elements);
        main.add("imageUrls",imagearray);
        idproof.addProperty("proofId",idnum);
        idproof.addProperty("idProofType",idtype);
        main.add("idProof",idproof);
        main.addProperty("tenantId","5a7be4771794c2c2439f6cb8");
        main.addProperty("tenantName","Kiran");
        Log.e("veera",main.toString());
        Gson gson = new Gson();
        String json = gson.toJson(main);
        writeToFile(main.toString());
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

                    Intent i= new Intent(MainActivity.this,ProfileActivity.class);
                    startActivity(i);

                    readFromFile();

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
                    arrayList.clear();
                    coordList.clear();
                    elements=new JsonArray();

                    adapter.notifyDataSetChanged();

                    Toast.makeText(MainActivity.this,"Successfully Uploaded", Toast.LENGTH_LONG).show();

                }
                else {

                  //  Toast.makeText(MainActivity.this,"Something went wrong,please try Again", Toast.LENGTH_LONG).show();


                }
            }

            @Override
            public void onFailure(Call<UploadImageResult> call, Throwable t) {

                Toast.makeText(MainActivity.this,"Something is missing,please try Again", Toast.LENGTH_LONG).show();

                progressDialog.dismiss();
            }
        });


    }

    private void writeToFile(String data) {

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = MainActivity.this.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
    @Override
    public void onClick(View arg0) {

// TODO Auto-generated method stub
        if (arg0.getId() == R.id.capture_btn1) {

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
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
        changeloc=new LatLng(latitude,longitude);

        latlog= String.valueOf(latitude+","+longitude);
        loc= String.valueOf(latitude+"_"+longitude);

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((this),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                if(gps.canGetLocation){

                    geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    latitude=gps.getLatitude();
                    longitude=gps.getLongitude();
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
                    latlog= String.valueOf(gps.getLatitude()+","+gps.getLongitude());
                    value=loc;
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
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainActivity.this,
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
                    Toast.makeText(MainActivity.this, "permission denied", Toast.LENGTH_LONG).show();
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

             userloc = new LatLng(latitude,longitude);
             changeloc=new LatLng(latitude,longitude);

            try {

                addresses = geocoder.getFromLocation(latitude,longitude, 1);
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

            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        // Add a marker in Sydney and move the camera

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
                    imageView.setLayoutParams(new GridView.LayoutParams(500, 500));
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