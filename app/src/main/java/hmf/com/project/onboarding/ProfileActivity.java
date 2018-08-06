package hmf.com.project.onboarding;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import hmf.com.project.onboarding.adapter.ProfileCategAdapter;
import hmf.com.project.onboarding.domains.QuestionSet;
import hmf.com.project.onboarding.domains.ServierActivities;
import hmf.com.project.onboarding.domains.SurveyCriteriaItem;
import hmf.com.project.onboarding.domains.UserDomain;
import hmf.com.project.onboarding.internet.NetworkUtil;
import hmf.com.project.onboarding.internet.VolleySingleton;
import hmf.com.project.onboarding.logger.L;
import hmf.com.project.onboarding.realm.models.SurveyResponse;
import hmf.com.project.onboarding.services.PostLocationServices;
import hmf.com.project.onboarding.services.SyncAllSurveyServices;
import hmf.com.project.onboarding.survey.SurveyManager;
import hmf.com.project.onboarding.utility.AppConstant;
import hmf.com.project.onboarding.utility.ConnectionDetector;
import hmf.com.project.onboarding.utility.CustomProgressLoaderDialog;
import hmf.com.project.onboarding.utility.HttpHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {
    private static final int REQUEST_LOCATION_SETTING = 101;
    CardView cardStartSurvey;
    CardView cardSyncAllData;
    //SwipeRefreshLayout refresh;
    CustomProgressLoaderDialog loader;
    ProfileCategAdapter profileCategAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menus, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Logout ?");
                alertDialogBuilder
                        .setMessage("would you Like to Logout this App !")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        gotoLoginActivity();
                                    }
                                })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            case R.id.refresh:
                if (new ConnectionDetector(this).isConnectedToInternet())

                    new DashBoardCalls().execute(GETUSER);
                else
                    Snackbar.make(getCurrentFocus(), "you are in Offline", Snackbar.LENGTH_INDEFINITE).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void gotoLoginActivity() {

        ESurvey.clearCache();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    DashBoardCalls dashBoardCalls;
    RecyclerView rec_plans;
    ViewPager rec_categ;
    TextView tv_no_activities;
    private CardView cardStartSurveyAll,cardlocation;
    private static final int MY_SOCKET_TIMEOUT_MS = 60 * 1000 * 3;
    RequestQueue queue;
    private Location clocation;
    private GpsTracker gpsTracker;
    boolean isAlltime = true;
    private ProgressDialog pd;
    private Intent intent;
    private boolean isServiceStarted;
    private SurveyManager surveyManager;
    private RealmResults<SurveyResponse> allSurvey;
    ArrayList<SurveyCriteriaItem> surveyCriteriaItems = new ArrayList<>();
    ArrayList<QuestionSet> questionSets=new ArrayList<>();
    public GoogleApiClient mGoogleApiClient;

    private TextView txtCountSurvey;
    private int count = 0;
    private String GETUSER = "getUser";
    private String GETACTIVITIES = "getActivities";
    private  UserDomain user;
    public int criteriaPosition = 0;
    public  static String  phone,tid,tname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Hire My Farmer");
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        profileCategAdapter = new ProfileCategAdapter(getSupportFragmentManager(), surveyCriteriaItems);
        dashBoardCalls = new DashBoardCalls();
        rec_plans = (RecyclerView) findViewById(R.id.rec_plans);
        rec_categ = (ViewPager) findViewById(R.id.rec_categ);
        rec_categ.setAdapter(profileCategAdapter);
        tv_no_activities = (TextView) findViewById(R.id.tv_no_activities);
        cardlocation=(CardView)findViewById(R.id.card_view_location);
        cardStartSurvey = (CardView) findViewById(R.id.card_view_back);
        cardSyncAllData = (CardView) findViewById(R.id.sync_data);
        loader = new CustomProgressLoaderDialog(this);
        txtCountSurvey = (TextView) findViewById(R.id.title_back_all_sync);
        rec_plans.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //rec_categ.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        cardStartSurvey.setOnClickListener(this);
        Log.e("key",ESurvey.getAccessToken());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(ProfileActivity.this,
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
        cardSyncAllData.setOnClickListener(this);
        cardlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i= new Intent(ProfileActivity.this,FarmerDetails.class);
                startActivity(i);
            }
        });

        cardStartSurveyAll = (CardView) findViewById(R.id.card_all_quest);

        cardStartSurveyAll.setOnClickListener(this);
        rec_categ.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ProfileActivity.this.criteriaPosition = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        queue = VolleySingleton.getInstance().getRequestQueue();
        isAlltime = true;
        intent = new Intent(this, PostLocationServices.class);
        surveyManager = new SurveyManager(new WeakReference<Context>(this));

        //Snackbar.make(cardStartSurvey ,getLocaleLanguage()+"",Snackbar.LENGTH_SHORT ).show();
    }

    private synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(ProfileActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        gpsTracker=new GpsTracker(this);
        if(gpsTracker.canGetLocation){

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
    //


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((this),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                if(gpsTracker.canGetLocation){

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
                                ActivityCompat.requestPermissions(ProfileActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(ProfileActivity.this,
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
                    Toast.makeText(ProfileActivity.this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public String getLocaleLanguage() {
        Configuration config = getBaseContext().getResources().getConfiguration();
        return config.locale.getLanguage();
    }//


    @Override
    protected void onResume() {
        super.onResume();
        if (new ConnectionDetector(this).isConnectedToInternet())
            new DashBoardCalls().execute(GETUSER);
        else
            Snackbar.make(findViewById(R.id.parentPanel), "you are in Offline", Snackbar.LENGTH_INDEFINITE).show();

        countPendingSurvey();
        if (NetworkUtil.isOnline(this)) {
            if (clocation == null)
                getLocation();
        }//

    }//

    @Override
    public void onClick(View v) {
        if (v == cardStartSurvey) {
            isAlltime = false;
            getLocation();

        }//if()
        if (v == cardStartSurveyAll) {
            isAlltime = true;
            if (null != surveyManager) {
                surveyManager.getSurveySet();
            }//
            if (NetworkUtil.isOnline(this)) {
                makePostGeoRequest();
            }//
        }//if()
        if (v == cardSyncAllData) {
            if (NetworkUtil.isOnline(this)) {

                if (count == 0) {
                    Snackbar.make(v, getString(R.string.no_survey_to_sync), Snackbar.LENGTH_SHORT).show();
                    return;
                }//count
                startService(new Intent(this, SyncAllSurveyServices.class));
            }//
            else {
                Snackbar.make(v, getString(R.string.no_intenet_to_sync), Snackbar.LENGTH_SHORT).show();
            }//
        }//if()
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("would you Like to close this App !")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                                homeIntent.addCategory( Intent.CATEGORY_HOME );
                                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(homeIntent);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

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

    private void getLocation() {

        if (gpsTracker == null) {
            gpsTracker = new GpsTracker(this);
        }//if


        if (!gpsTracker.canGetLocation()) {
            showSettingsAlert();
        }//
        else {
            if (!isServiceStarted) {
                startService(intent);
                isServiceStarted = true;
                L.d("GetLocation():: Service Created from Activity");
            }


            clocation = gpsTracker.getLocation();
            if (null != clocation) {
                gpsTracker.stopUsingGPS();
                L.d("LatLong" + clocation.getLongitude() + "," + clocation.getLatitude());
            }//

        }//
    }//

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }//

    @Override
    protected void onRestart() {
        super.onRestart();
        if (gpsTracker == null) {
            return;
        }
        clocation = gpsTracker.getLocation();
        if (clocation != null) {
            gpsTracker.stopUsingGPS();
//            sendRequest();

        }//if()....

    }//

    private void makePostGeoRequest() {
        try {
            String url = ESurvey.URL + "/postGeoLocation";
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("status", "started");
            jsonRequest.put("badge_color", "red");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            Date date = new Date();
            String currentDate = dateFormat.format(date);

            jsonRequest.put("timestamp", currentDate);
            L.d("ProfileActivity ::makePostGeoLocation()" + currentDate);
            jsonRequest.put("type", "survey");
            jsonRequest.put("surveyor", ESurvey.getLoginId());
            if (clocation != null) {
                jsonRequest.put("latlong", clocation.getLatitude() + "," + clocation.getLongitude());
            }//
            else {

            }
            //
            L.d("makePostGeoRequest():::JSON::" + jsonRequest.toString());

            // Request a string response from the provided URL.
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
//
                            L.d(",ProfileActivity::makePostGeoLocation():: Success");
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    L.e(",ProfileActivity::makePostGeoLocation():: Error");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
        if (surveyManager != null) {

            surveyManager.destroySurveyManager();
        }//if()..

        surveyManager = null;
    }

    public void countPendingSurvey() {
        Realm realm = Realm.getDefaultInstance();
//        allSurvey = realm.where(SurveyResponse.class)
//                .findAllAsync();
        allSurvey = realm.where(SurveyResponse.class)
                .equalTo("isSynced", false).findAllAsync();
//
        allSurvey.addChangeListener(realmChangeListener);
    }//removeCahedDataFromDataBase()...

    RealmChangeListener realmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            count = allSurvey.size();
            L.d("RealmChangeListener::onChange:: size = " + count);
            if (count > 0) {
                txtCountSurvey.setText(count + getString(R.string.survey_to_sync));
            }//if()...
            else {
                txtCountSurvey.setText(R.string.no_survey_to_sync);
            }//else...
        }//onChange()...
    };//

    @Override
    protected void onPause() {
        super.onPause();
        allSurvey.removeChangeListener(realmChangeListener);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest mLocationRequest = createLocationRequest();
        if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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

    private class DashBoardCalls extends AsyncTask<String, Void, ServierActivities> {

        String currentMethod;
        Gson gson = new Gson();

        @Override
        protected ServierActivities doInBackground(String... params) {
            String s = params[0];
           /* runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!loader.isShowing())
                        loader.showProgressLoader();
                }
            });*/

            currentMethod = s;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(AppConstant.BASEURL);
            stringBuilder.append(AppConstant.USERPATH);
            stringBuilder.append(ESurvey.getLoginId());
            if (new ConnectionDetector(ProfileActivity.this).isConnectedToInternet()) {


                Log.e("veera",String.valueOf(HttpHelper.sendGETRequest(stringBuilder.toString(), ESurvey.getAccessToken())));
                Log.e("veera",String.valueOf((ESurvey.getAccessToken())));


                ESurvey.setUser(HttpHelper.sendGETRequest(stringBuilder.toString(), ESurvey.getAccessToken()));
            }
            ProfileActivity.this.user = ESurvey.getUser();
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append(AppConstant.BASEURL);
            stringBuilder1.append(AppConstant.ACTIVITIESPATH);

            return gson.fromJson(HttpHelper.sendPOSTRequest(stringBuilder1.toString(), getParams(), ESurvey.getAccessToken()), ServierActivities.class);
        }

        private String getParams() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("surveyorPhone", user.getPhoneNumber());
                jsonObject.put("tenantId",user.getTenantId());

                Log.e("ram",String.valueOf(user.getTenantId()));

                phone=user.getPhoneNumber();
                tid=user.getTenantId();
                tname=user.getTenantName();
                //jsonObject.put("surveyorPhone", user.getPhoneNumber());
                //jsonObject.put("startDate", "01/06/2017");
                //jsonObject.put("endDate", "05/06/2017");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }

        @Override
        protected void onPostExecute(ServierActivities servierActivities) {
            if (servierActivities != null) {
                //rec_plans.setAdapter(new ProfilePlansAdapter(servierActivities.getStatusCounts()));
                surveyCriteriaItems.clear();
               // Log.e("hima",servierActivities.getQuestionSets().toString());

                if (servierActivities.getSurveyCriteria() != null) {
                    if (servierActivities.getSurveyCriteria().size() > 0) {
                        rec_categ.setVisibility(View.VISIBLE);
                        tv_no_activities.setVisibility(View.GONE);
                        questionSets.addAll(servierActivities.getQuestionSets());
                       // Log.e("hima",questionSets.get(0).getQuestions().get(0).getQuestion());

                        surveyCriteriaItems.addAll(servierActivities.getSurveyCriteria());
                        profileCategAdapter.notifyDataSetChanged();
                        ESurvey.setSurveyActivityId(servierActivities.getSurveyActivityId());
                        //rec_categ.setCurrentItem(criteriaPosition);
                        loader.dismissProgressLoader();
                    } else {
                        rec_categ.setVisibility(View.GONE);
                        tv_no_activities.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    rec_categ.setVisibility(View.GONE);
                    tv_no_activities.setVisibility(View.VISIBLE);
                }
            }
            else {

                Toast.makeText(ProfileActivity.this,"No Responce",Toast.LENGTH_LONG).show();
            }

        }
    }
}

