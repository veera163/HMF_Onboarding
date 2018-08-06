package hmf.com.project.onboarding.details;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import hmf.com.project.onboarding.ESurvey;
import hmf.com.project.onboarding.GpsTracker;
import hmf.com.project.onboarding.R;
import hmf.com.project.onboarding.internet.JSONConverter;
import hmf.com.project.onboarding.internet.VolleySingleton;
import hmf.com.project.onboarding.location.GPSTracker;
import hmf.com.project.onboarding.logger.L;
import hmf.com.project.onboarding.question.QuestionModel;
import hmf.com.project.onboarding.utility.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PersonDetailsActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener  {

    private static final int REQUEST_LOCATION_SETTING = 101;
    private static final int MY_SOCKET_TIMEOUT_MS = 60 * 1000 * 2;
    private Button btnReset, btnSubmit;

    public EditText ed_name,ed_age,ed_phone,ed_loanamt,ed_serveynum,ed_passbok,ed_adhar,ed_village,
            ed_mandal,ed_dist,ed_state,ed_pin,ed_landarea,ed_landtype,ed_crophistory,ed_cropRunningInfo,ed_cropVariety,ed_insurenceperson,ed_reason,ed_whours,ed_mhours,ed_profitnegotiation;

    Spinner gender,farmartype,loantype,landmer,insurencetype,Disastertype,irrigationtype,boardedtype;
    LinearLayout hmfworkerlayout,hmfownerlayout;

    String name,age,phone,loanamt,serveynum,passbooknum,adhar,village,mandal,dist,state,pin,landarea,landtype,crophistory,insurenceperson,reason,
            workhours,monitoryhours,profitneg,fgender,ftype,lontype,lanmer,instype,distype,irrigation,boarded,cropRunningInfo,cropVariety;

    private RequestQueue queue;
    private GPSTracker gps;
    private Location location;
    private ProgressDialog pd;
    Geocoder geocoder;
    List<Address> addresses;
    GpsTracker gpsTracker;
    Double lat,lng;
    String add,city,lstate,postalcode,knownName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gpsTracker=new GpsTracker(PersonDetailsActivity.this);
        if(gpsTracker.canGetLocation()) {
            lat = gpsTracker.getLatitude();
            lng = gpsTracker.getLongitude();
            geocoder = new Geocoder(this, Locale.getDefault());
            try {

                addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                add = addresses.get(0).getSubLocality(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(0).getLocality();
                lstate = addresses.get(0).getAdminArea();
                postalcode = addresses.get(0).getPostalCode();
                knownName = addresses.get(0).getFeatureName();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        hmfworkerlayout=(LinearLayout)findViewById(R.id.hmfworkerlayout);
        hmfownerlayout=(LinearLayout)findViewById(R.id.hmfownerlayout);

        ed_name = (EditText) findViewById(R.id.fname);
        ed_age = (EditText) findViewById(R.id.fage);
        ed_phone = (EditText) findViewById(R.id.fphone);
        ed_loanamt = (EditText) findViewById(R.id.loanamt);
        ed_serveynum = (EditText) findViewById(R.id.servernum);
        ed_passbok = (EditText) findViewById(R.id.fpassnum);
        ed_adhar = (EditText) findViewById(R.id.fadharnum);
        ed_village = (EditText) findViewById(R.id.fvaillage);
        ed_village.setText(knownName);
        ed_mandal = (EditText) findViewById(R.id.fmandel);
        ed_mandal.setText(add);
        ed_dist = (EditText) findViewById(R.id.fdist);
        ed_dist.setText(city);
        ed_state = (EditText)findViewById(R.id.fstate);
        ed_state.setText(lstate);
        ed_pin = (EditText) findViewById(R.id.fpin);
        ed_pin.setText(postalcode);
        ed_landarea = (EditText) findViewById(R.id.landarea);
        ed_landtype = (EditText) findViewById(R.id.landtype);
        ed_crophistory = (EditText) findViewById(R.id.crophistory);
        ed_cropRunningInfo=(EditText)findViewById(R.id.cropRunningInfo);
        ed_cropVariety=(EditText)findViewById(R.id.cropVariety);
        ed_insurenceperson = (EditText) findViewById(R.id.insurenceperson);
        ed_reason = (EditText)findViewById(R.id.reason);
        ed_whours = (EditText) findViewById(R.id.whours);
        ed_mhours = (EditText) findViewById(R.id.mhours);
        ed_profitnegotiation = (EditText)findViewById(R.id.profitnegotiation);

        gender=(Spinner)findViewById(R.id.fgender);
        gender.setOnItemSelectedListener(this);

        farmartype=(Spinner)findViewById(R.id.ftype);
        farmartype.setOnItemSelectedListener(this);

        loantype=(Spinner)findViewById(R.id.loantype);
        loantype.setOnItemSelectedListener(this);

        landmer=(Spinner)findViewById(R.id.landmer);
        landmer.setOnItemSelectedListener(this);

        insurencetype=(Spinner)findViewById(R.id.insurencetype);
        insurencetype.setOnItemSelectedListener(this);

        Disastertype=(Spinner)findViewById(R.id.Disastertype);
        Disastertype.setOnItemSelectedListener(this);

        irrigationtype=(Spinner)findViewById(R.id.irrigationtype);
        irrigationtype.setOnItemSelectedListener(this);
        boardedtype=(Spinner)findViewById(R.id.boardedtype);

        boardedtype.setOnItemSelectedListener(this);



        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnReset = (Button) findViewById(R.id.btnReset);
        btnSubmit.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        queue = VolleySingleton.getInstance().getRequestQueue();


    }

    public void showSettingsAlert() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
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

    private void getLoaction() {
        if (null == gps) {
            gps = new GPSTracker(this);
        }//

        if (!gps.canGetLocation()) {
            showSettingsAlert();
        }//
        else {
            L.d("Gettting loaction");
            location = gps.getLocation();
            if (null != location) {
                gps.stopUsingGPS();
                L.d("Latitude" + location.getLatitude());
                L.d("Longitude" + location.getLongitude());
            }//

        }//
    }//

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION_SETTING) {
            Toast.makeText(this, "You turned on Location.", Toast.LENGTH_SHORT).show();

        }//if()....
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (null != gps) {
            location = gps.getLocation();
            if (location != null) {
                gps.stopUsingGPS();

                Toast.makeText(this, "Got the location", Toast.LENGTH_SHORT).show();
                L.d("Latitude" + location.getLatitude());
                L.d("Longitude" + location.getLongitude());
            }//if()....
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnReset) {
            ed_name.setText("");
            //txtInputCast.getEditText().setText("");
            ed_age.setText("");
            ed_phone.setText("");
            ed_loanamt.setText("");
            ed_serveynum.setText("");

            ed_passbok.setText("");

            // String txtInputWardName.getEditText().getText().toString();;
            ed_adhar.setText("");
            ed_village.setText("");
            ed_mandal.setText("");
            ed_dist.setText("");
            ed_state.setText("");
            ed_pin.setText("");
            ed_landarea.setText("");
            ed_landtype.setText("");
            ed_crophistory.setText("");
            ed_cropRunningInfo.setText("");
            ed_cropVariety.setText("");
            ed_insurenceperson.setText("");
            ed_reason.setText("");
            ed_whours.setText("");
            ed_mhours.setText("");
            ed_profitnegotiation.setText("");

            gender.setSelection(0);
            farmartype.setSelection(0);
            loantype.setSelection(0);
            landmer.setSelection(0);
            insurencetype.setSelection(0);
            Disastertype.setSelection(0);
            irrigationtype.setSelection(0);
            boardedtype.setSelection(0);

        }//
        if (v == btnSubmit) {
            sendRequest();
        }//

    }//

    private void sendRequest() {


         name = ed_name.getText().toString();

         age = ed_age.getText().toString();

         phone = ed_phone.getText().toString();

         loanamt = ed_loanamt.getText().toString();

         serveynum = ed_serveynum.getText().toString();

         passbooknum = ed_passbok.getText().toString();

         adhar = ed_adhar.getText().toString();

         village = ed_village.getText().toString();

         mandal = ed_mandal.getText().toString();

         dist = ed_dist.getText().toString();

         state = ed_state.getText().toString();
         pin = ed_pin.getText().toString();
         landarea = ed_landarea.getText().toString();
         landtype = ed_landtype.getText().toString();
         crophistory = ed_crophistory.getText().toString();
         cropRunningInfo=ed_cropRunningInfo.getText().toString();
        cropVariety=ed_cropVariety.getText().toString();
         insurenceperson = ed_insurenceperson.getText().toString();
         reason=ed_reason.getText().toString();
         workhours = ed_whours.getText().toString();
         monitoryhours = ed_mhours.getText().toString();
         profitneg = ed_profitnegotiation.getText().toString();


        /* fgender = gender.getSelectedItem().toString();
         ftype = farmartype.getSelectedItem().toString();
         lontype = loantype.getSelectedItem().toString();
         lanmer = landmer.getSelectedItem().toString();
         instype = insurencetype.getSelectedItem().toString();
         distype = Disastertype.getSelectedItem().toString();
         irrigation=irrigationtype.getSelectedItem().toString();*/


        if (name == null || name.equals("")) {
            ed_name.setError("Can't be left blank");
            return;
        }
        else if (age == null || age.equals("")) {
            ed_age.setError("Can't be left blank");
            return;
        }
        else if (phone == null || phone.equals("")) {
            ed_phone.setError("Can't be left blank");
            return;
        }else if (serveynum == null || serveynum.equals("")) {
            ed_serveynum.setError("Can't be left blank");
            return;
        }else if (adhar == null || adhar.equals("")) {
            ed_adhar.setError("Can't be left blank");
            return;
        }
        else if (village == null || village.equals("")) {
            ed_village.setError("Can't be left blank");
            return;
        }
        else if (pin == null || pin.equals("")) {
            ed_pin.setError("Can't be left blank");
            return;
        }
        else if (landarea == null || landarea.equals("")) {
            ed_landarea.setError("Can't be left blank");
            return;
        }else if (landtype == null || landtype.equals("")) {
            ed_landtype.setError("Can't be left blank");
            return;
        }
        JSONObject resultJson = null;


        pd = new ProgressDialog(this);
        pd.setMessage("Uploading File to Server");
        pd.setCancelable(false);
        String url = AppConstant.BASEURL+"postPeopleResponse";
        try {
            resultJson = JSONConverter.createJSON(ESurvey.getSurveyActivityId());
            if (location != null) {
                resultJson.put("latlong", location.getLatitude() + "," + location.getLongitude());
                //resultJson.put("Latitude", location.getLatitude());
            }//
            else {
                resultJson.put("latlong", "NA");

            }//
            resultJson.put("name",name);
            resultJson.put("gender",fgender);
            resultJson.put("onBoarded",boarded);
            resultJson.put("age",age);
            resultJson.put("phone",phone+"");
            resultJson.put("type",ftype+"");
            resultJson.put("loanAvailed",lontype+"");
            resultJson.put("loanAmount",loanamt+"");
            resultJson.put("revenueServeyNumber",serveynum+"");
            resultJson.put("passbookNumber",passbooknum+"");
            resultJson.put("adharNumber",adhar+"");
            resultJson.put("village",village+"");
            resultJson.put("mandal",mandal+"");
            resultJson.put("district",dist+"");
            resultJson.put("state",state+"");
            resultJson.put("district",dist+"");
            resultJson.put("pincode",pin+"");
            resultJson.put("landArea",landarea+"");
            resultJson.put("landAreaUnits",lanmer+"");
            resultJson.put("landType",landtype+"");
            resultJson.put("cropHistory",crophistory+"");
            resultJson.put("cropRunningInfo",cropRunningInfo+"");
            resultJson.put("cropVariety",cropVariety);
            resultJson.put("insurancePaid",instype+"");
            resultJson.put("insurer",insurenceperson+"");
            resultJson.put("monitoring",distype+"");
            resultJson.put("disasterRecoveryReason",reason+"");
            resultJson.put("workingHours",workhours+"");
            resultJson.put("monitoringHours",monitoryhours+"");
            resultJson.put("negotiatedProfit",profitneg+"");
            resultJson.put("irrigationType",irrigation+"");
            resultJson.put("status", "Completed");

            Log.e("pasing",resultJson.toString());


        } catch (Exception e) {
            L.e("Error in sending file " + e.getMessage());
            return;
        }//exception

        L.d("length of file while uploading to server " + resultJson.length());


        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, resultJson,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                                mTxtDisplay.setText("Response: " + response.toString());
                        try {
                            String error = response.getString("error");
                            String message = response.getString("message");
                            L.d(error + " ---" + message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(PersonDetailsActivity.this, "Upload Successfull", Toast.LENGTH_SHORT).show();

                        QuestionModel.questionList.clear();
                        makePostGeoRequest();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                error.printStackTrace();
                L.e(error + "");
                L.e(error.getLocalizedMessage() + "");
                pd.dismiss();
                launchDialog("That didn't work! Upload fails");
            }
        });

        // Add the request to the RequestQueue.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
        pd.show();
    }//

    private void makePostGeoRequest() {
        try {
            String url = "http://anyasoftindia.com/postGeoLocation";
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("status", "Completed");
            jsonRequest.put("surveyor", ESurvey.getLoginId());
            if (location != null) {
                jsonRequest.put("latlong", location.getLatitude() + "," + location.getLongitude());
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
                                    pd.dismiss();
                                    launchDialog("Sorry . It seems something is wrong in server." +
                                            " Please try again later");
                                    return;
                                }//catch()
                                catch (IllegalArgumentException e) {
                                    L.e(e.getLocalizedMessage() + " From postGeoLocation");
                                    pd.dismiss();
                                    launchDialog("Sorry . It seems something is wrong in server." +
                                            " Please try again later");
                                    return;
                                }//catch()
                            }
                            pd.dismiss();
                            finish();


                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    L.e(error + "  From postGeoLocation");

                    launchDialog("Server can't be reached . Please check your internet connection." +
                            " Please try again later");
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

    private void launchDialog(String msg) {

        final android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertBuilder.setTitle("ALERT!!");
        alertBuilder.setMessage(msg);

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });
        android.support.v7.app.AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        gender = (Spinner)adapterView;
        farmartype = (Spinner)adapterView;
        loantype = (Spinner)adapterView;
        landmer = (Spinner)adapterView;
        insurencetype = (Spinner)adapterView;
        Disastertype = (Spinner)adapterView;
        irrigationtype = (Spinner)adapterView;
        boardedtype=(Spinner)adapterView;

        if(gender.getId() == R.id.fgender)
        {
            fgender = (String) adapterView.getItemAtPosition(i);
          //  Toast.makeText(this, "Your choose :" + fgender,Toast.LENGTH_SHORT).show();

        }

        if(landmer.getId() == R.id.landmer)
        {
            lanmer = (String) adapterView.getItemAtPosition(i);
          //  Toast.makeText(this, "Your choose :" + lanmer,Toast.LENGTH_SHORT).show();

        }

        if(irrigationtype.getId() == R.id.irrigationtype)
        {
            irrigation = (String) adapterView.getItemAtPosition(i);
          //  Toast.makeText(this, "Your choose :" + irrigation,Toast.LENGTH_SHORT).show();


        }
        if(boardedtype.getId()== R.id.boardedtype){

            boarded = (String) adapterView.getItemAtPosition(i);
          //  Toast.makeText(this, "Your choose :" + boarded,Toast.LENGTH_SHORT).show();
        }

        if(farmartype.getId() == R.id.ftype)
        {
            ftype = (String) adapterView.getItemAtPosition(i);
          //  Toast.makeText(this, "Your choose :" + ftype,Toast.LENGTH_SHORT).show();

            if(ftype.equals("Land Lord")){

                hmfworkerlayout.setVisibility(View.GONE);
                hmfownerlayout.setVisibility(View.VISIBLE);

            }
            else {
                hmfownerlayout.setVisibility(View.GONE);
                hmfworkerlayout.setVisibility(View.VISIBLE);

            }

        }

        if(loantype.getId() == R.id.loantype)
        {
            lontype = (String) adapterView.getItemAtPosition(i);
          //  Toast.makeText(this, "Your choose :" + lontype,Toast.LENGTH_SHORT).show();


            if(lontype.equals("No")){

                ed_loanamt.setVisibility(View.GONE);
            }
            else {
                ed_loanamt.setVisibility(View.VISIBLE);

            }
        }

        if(insurencetype.getId() == R.id.insurencetype)
        {
            instype = (String) adapterView.getItemAtPosition(i);

            if(instype.equals("No")){

                ed_insurenceperson.setVisibility(View.GONE);
            }
            else {
                ed_insurenceperson.setVisibility(View.VISIBLE);

            }
        }


        if(Disastertype.getId() == R.id.Disastertype)
        {
            distype = (String) adapterView.getItemAtPosition(i);

            if(distype.equals("No")){

                ed_reason.setVisibility(View.GONE);
            }
            else {
                ed_reason.setVisibility(View.VISIBLE);

            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}//



