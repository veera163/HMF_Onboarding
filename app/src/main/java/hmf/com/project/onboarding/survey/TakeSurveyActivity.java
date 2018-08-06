package hmf.com.project.onboarding.survey;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import hmf.com.project.onboarding.ESurvey;
import hmf.com.project.onboarding.GpsTracker;
import hmf.com.project.onboarding.R;
import hmf.com.project.onboarding.camera.CameraActivity;
import hmf.com.project.onboarding.details.PersonDetailsFragment;
import hmf.com.project.onboarding.internet.NetworkUtil;
import hmf.com.project.onboarding.internet.VolleySingleton;
import hmf.com.project.onboarding.location.GPSTracker;
import hmf.com.project.onboarding.location.LastLocation;
import hmf.com.project.onboarding.logger.L;
import hmf.com.project.onboarding.media.MediaRecorderSingleton;
import hmf.com.project.onboarding.question.QuestionModel;
import hmf.com.project.onboarding.question.Stamps;
import hmf.com.project.onboarding.question.StampsAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakeSurveyActivity extends AppCompatActivity implements QuestionFragment.OnQuestionFragmentInteractionListener,
        AnswerFragment.OnFragmentInteractionListener, PersonDetailsFragment.OnFragmentInteractionListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {
    private AnswerFragment answerFragment;
    private QuestionFragment questionFragment;
    private LinearLayout rootView;
    LinearLayout personaldetails_fragment;
    private boolean isDetailsQuestionAnswered = true;
    private static final int REQUEST_LOCATION_SETTING = 101;
    private static final int MY_SOCKET_TIMEOUT_MS = 60 * 1000 * 3;
    private Chronometer chronometer;
    private MediaRecorder mediaRecorder;
    private RecyclerView timeLine = null;
    private long milli = 1000;
    private StampsAdapter stampsAdapter;
    private int position = -1;
    GpsTracker gpsTracker;
    private GPSTracker gps;
    private ProgressDialog pd;
    private Location clocation;
    private RequestQueue queue;
    private String mCurrentPhotoPath;
    public GoogleApiClient mGoogleApiClient;
    private boolean isPhtotAvialable;
    private Bitmap imageBitmap;
    private FragmentManager fragmentManager;
    private PersonDetailsFragment detailFragment;
    private SurveyResponseManager responseManager;

    @Override
    public void onBackPressed() {
        if (personaldetails_fragment.getVisibility() == View.VISIBLE) {
            questionFragment.getView().setVisibility(View.VISIBLE);
            answerFragment.getView().setVisibility(View.VISIBLE);
            detailFragment.getView().setVisibility(View.GONE);
            personaldetails_fragment.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_survey);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(TakeSurveyActivity.this,
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
        if (QuestionModel.questionList.size() <= 0) {
            launchErrorDialog();
            return;
        }//
        questionFragment = (QuestionFragment) getSupportFragmentManager().findFragmentById(R.id.quest_fragment);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        timeLine = (RecyclerView) findViewById(R.id.listTimeLine);
        rootView = (LinearLayout) findViewById(R.id.root);
        personaldetails_fragment = (LinearLayout) findViewById(R.id.personaldetails_fragment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        timeLine.setLayoutManager(linearLayoutManager);
        Stamps.clearStamps();
        stampsAdapter = new StampsAdapter();
        timeLine.setAdapter(stampsAdapter);
        fragmentManager = getSupportFragmentManager();
        answerFragment = new AnswerFragment();
        detailFragment = new PersonDetailsFragment();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.personaldetails_fragment, detailFragment, "details");
        ft.commit();

        FragmentTransaction ft1 = fragmentManager.beginTransaction();
        ft1.add(R.id.answer_fragment, answerFragment, "answer");
        ft1.commit();
        queue = VolleySingleton.getInstance().getRequestQueue();

        initRecorder();
    }//

    private void initRecorder() {
        try {
            mediaRecorder = MediaRecorderSingleton.getInstance().getRecorder();
            launchDialog();
        } catch (IOException e) {
            e.printStackTrace();
            L.e(e.getLocalizedMessage());
        }
    }//initRecorder()...

    @Override
    public void setSecondFragment(int position) {
        this.position = position;
        onMarked(true);
        /*if (position == QuestionModel.questionList.size() - 1) {
            detailFragment = new PersonDetailsFragment();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.answer_fragment, detailFragment, "details");
            ft.commit();
        }//if()
        else {
        */
        if (!answerFragment.isVisible()) {

//                answerFragment = (AnswerFragment) getSupportFragmentManager()
//                        .findFragmentById(R.id.answer_fragment);
            FragmentTransaction ft = fragmentManager.beginTransaction();
            answerFragment.setPosition(position);
            ft.replace(R.id.answer_fragment, answerFragment, "answer");
            ft.commit();
            return;
        }//
        L.d("Updating fragment");
        answerFragment.updateFragment(position);
    }//else


    //setSecondFragment()...

    private synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(TakeSurveyActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        gpsTracker=new GpsTracker(this);
        if(gpsTracker.canGetLocation()){

            clocation = gpsTracker.getLocation();

        }
        else {
            gpsTracker.showSettingsAlert();
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
        if (ContextCompat.checkSelfPermission(TakeSurveyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((this),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                if(gpsTracker.canGetLocation()){

                    clocation=gpsTracker.getLocation();
                }
                else {
                    gpsTracker.showSettingsAlert();
                }

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new android.support.v7.app.AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(TakeSurveyActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(TakeSurveyActivity.this,
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
                    Toast.makeText(TakeSurveyActivity.this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    public void onSubmit() {

        L.d("Submitting the details");
        if (isDetailsQuestionAnswered) {

            if (NetworkUtil.isOnline(this)) {
                if (!getLocation()) {
                    return;
                }//if()...
            }//if()..
            else {
                if (null == clocation) {
                    clocation = LastLocation.getLastLocation(this);
                }//if()...
            }//else
            if (null == clocation) {
                QuestionModel.latLong = "NA";
            }//
            else {
                QuestionModel.latLong = clocation.getLatitude() + "," + clocation.getLongitude();
            }
            L.d("onSubmit():: latlong" + QuestionModel.latLong);

            launchSubmitAlertDialog();
        }//
        else {
            Snackbar.make(rootView, getString(R.string.last_quest_prompt), Snackbar.LENGTH_SHORT).show();
        }//

    }//onSubmit()

    @Override
    public void onMarked(boolean marked) {
        if (questionFragment == null) {
            L.d("Fragment is null");
            return;
        }//
        if (position == QuestionModel.questionList.size() - 1) {
            isDetailsQuestionAnswered = true;
            if (!answerFragment.isVisible()) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.answer_fragment, answerFragment, "answer");
                ft.commit();
            }//
        }//
        QuestionModel.questionList.get(position).setBookmark(milli);
        Stamps stamp = new Stamps();
        stamp.setNum((position + 1) + "");
        stamp.setStampText(chronometer.getText().toString() + "");
        Stamps.stampList.add(stamp);
        stampsAdapter.notifyDataSetChanged();
        L.d(QuestionModel.questionList.get(position).getBookmark() + "------------from activity");

        milli = SystemClock.elapsedRealtime() - chronometer.getBase();

        questionFragment.updateAdapter();
       /* if (marked)
            questionFragment.clickOnNextQuestion(position + 1);*/
    }

    @Override
    public void gotoBack() {
        personaldetails_fragment.setVisibility(View.GONE);
        questionFragment.getView().setVisibility(View.VISIBLE);
        answerFragment.getView().setVisibility(View.VISIBLE);
        detailFragment.getView().setVisibility(View.GONE);
    }

    @Override
    public void enterPersonlaDetails() {
        personaldetails_fragment.setVisibility(View.VISIBLE);
        questionFragment.getView().setVisibility(View.GONE);
        answerFragment.getView().setVisibility(View.GONE);
        detailFragment.getView().setVisibility(View.VISIBLE);
    }

    private void launchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.media_prompt) +
                "There are " + QuestionModel.questionList.size() + " questions.");
        builder.setTitle(getString(R.string.alert));
        builder.setCancelable(false);
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mediaRecorder.start();
                chronometer.start();
            }
        });
        builder.create().show();
    }

    private void launchErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.no_survey_offline);
        builder.setTitle(getString(R.string.alert));
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.gotit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();
    }

    private void launchSubmitAlertDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.submit_dailog, null, false);
        final TextView txtMessage = (TextView) view.findViewById(R.id.txt_message);
        final Button btnSkip = (Button) view.findViewById(R.id.btn_skip);
        final Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        final Button btnCapture = (Button) view.findViewById(R.id.btn_capture);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        txtMessage.setText(R.string.photo_prompt);
        builder.setCancelable(false);
        builder.setView(view);
        final Dialog dialog = builder.create();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }//
        });
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                try {
                    if (mediaRecorder != null) {
                        mediaRecorder.stop();
                    }//
                }//try...
                catch (Exception e) {

                }//catch()....

                sendRequest();
            }//
        });
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dispatchTakePictureIntent();
            }//
        });
        dialog.show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            MediaRecorderSingleton.getInstance().destroyObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//

    private void sendRequest() {
        if (null == responseManager) {
            responseManager = new SurveyResponseManager(new WeakReference<Context>(this));
        }//
        responseManager.storeSurveyResponse();
        makePostGeoRequest();

    }//

    private void makePostGeoRequest() {
        try {
            String url = ESurvey.URL + "/postGeoLocation";
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("status", "Completed");
            jsonRequest.put("badge_color", "red");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            Date date = new Date();
            String currentDate = dateFormat.format(date);

            jsonRequest.put("timestamp", currentDate);
            L.d("PostLocationServices ::makePostGeoLocation()" + currentDate);
            jsonRequest.put("type", "survey");
            jsonRequest.put("surveyor", ESurvey.getLoginId());
            if (clocation != null) {
                jsonRequest.put("latlong", clocation.getLatitude() + "," + clocation.getLongitude());
            }//
            else {

            }
            //
            L.d("JSON::" + jsonRequest.toString());

            // Request a string response from the provided URL.
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
//

                            if (null != response) {
                                try {
                                    L.d(response.toString());

                                    JSONObject json = new JSONObject(response.toString());
                                    if (json.has("error")) {
                                        L.d(json.getString("error"));
                                    }//

                                } catch (JSONException e) {
                                    L.e(e.getMessage() + " From postGeoLocation");
                                    return;
                                }//catch()
                                catch (IllegalArgumentException e) {
                                    L.e(e.getLocalizedMessage() + " From postGeoLocation");
                                    return;
                                }//catch()
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    L.e(error + "  From postGeoLocation");

                    //launchDialog(getString(R.string.server_not_reachable) ,getString(R.string.close));
                }
            });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }//
        catch (Exception ex) {
            L.e("Error " + ex.getMessage());
            pd.dismiss();
            return;
        }//
    }

    private void launchDialog(String msg, String btnPrompt) {

        final android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertBuilder.setMessage(msg);

        alertBuilder.setNegativeButton(btnPrompt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });
        android.support.v7.app.AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    private void launchDialogWithPhoto(String msg, Bitmap bit) {

        final android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertBuilder.setMessage(msg);

        alertBuilder.setNegativeButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendRequest();
            }//
        });
        android.support.v7.app.AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    private boolean getLocation() {
        if (null == gpsTracker) {
            gpsTracker = new GpsTracker(this);
        }//

        if (!gpsTracker.canGetLocation()) {
            showSettingsAlert();
            return false;
        }//
        else {
            L.d("Getting loaction");
            clocation = gpsTracker.getLocation();
            if (null != clocation) {
                gpsTracker.stopUsingGPS();
                L.d("Latitude" + clocation.getLatitude());
                L.d("Longitude" + clocation.getLongitude());
            }//
            return true;
        }//
    }// @Override

    protected void onRestart() {
        super.onRestart();
        if (null != gpsTracker) {
            clocation = gpsTracker.getLocation();
            if (clocation != null) {
                gpsTracker.stopUsingGPS();

                Toast.makeText(this, "Got the location", Toast.LENGTH_SHORT).show();
                L.d("Latitude" + clocation.getLatitude());
                L.d("Longitude" + clocation.getLongitude());
            }//if()....
        }
    }

    public void showSettingsAlert() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle(getString(R.string.gps_setting));

        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.gps_prompt));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton(getString(R.string.setting), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, REQUEST_LOCATION_SETTING);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
    }//dispatchTakePictureIntent()...

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = ESurvey.FILE_NAME_PHOTO;
        File storageDir = new File(ESurvey.DIR_PATH);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_TAKE_PHOTO == requestCode && resultCode == RESULT_OK) {
            isPhtotAvialable = true;

            launchDialogWithPhoto(getString(R.string.capture_photo_prompt), imageBitmap);
        }//
    }//

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != responseManager) {
            responseManager.onStop();
        }//if()....
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        LocationRequest mLocationRequest = createLocationRequest();
        if (ContextCompat.checkSelfPermission(TakeSurveyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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

        clocation=location;
    }
}//TakeSurveyActivity...
