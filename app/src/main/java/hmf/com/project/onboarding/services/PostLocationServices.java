package hmf.com.project.onboarding.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import hmf.com.project.onboarding.ESurvey;
import hmf.com.project.onboarding.internet.NetworkUtil;
import hmf.com.project.onboarding.internet.VolleySingleton;
import hmf.com.project.onboarding.logger.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by saurabh.singh on 6/11/2016.
 */
public class PostLocationServices extends Service implements LocationListener {


    private Context mContext;

    // flag for GPS status
    public boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 10; //  30sec

    // Declaring a Location Manager
    protected LocationManager locationManager;
    private RequestQueue queue;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }//

    @Override
    public void onCreate() {
        super.onCreate();
        L.d("services created:::");
        mContext = getApplicationContext();
        getLocation();
        queue = VolleySingleton.getInstance().getRequestQueue();

    }//onCreate

    @Override
    public void onLocationChanged(Location location) {
        L.d("PostLocationServices::onLocationChanged()::" + location.getLatitude() + "," + location.getLongitude());
        if (NetworkUtil.isOnline(mContext)) {
            makePostGeoRequest(location);
        }
    }//onLocationChanged()..

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        L.d(" PostLocationServices::onStatusChanged()::" + provider + " , " + status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        L.d(" PostLocationServices:: provider enabled");

    }

    @Override
    public void onProviderDisabled(String provider) {
        stopSelf();
    }//

    public void getLocation() {
        try {
            if (null == locationManager)
                locationManager = (LocationManager) mContext
                        .getSystemService(Context.LOCATION_SERVICE);
            // getting GPS status

            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                L.e("PostLocationServices()::Location false");


            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(mContext,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(mContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                }//if()
            }//else
        } catch (Exception e) {
            L.e("PostLocationServices::Location e " + e.getMessage());
        }//catch()
    }//getLocation()

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
        L.d("PostLocationServices  destroyed...");
    }//onDestroyed()...

    private void makePostGeoRequest(Location location) {
        try {
            String url = ESurvey.URL + "/postGeoLocation";
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("status", "started");
            jsonRequest.put("badge_color", "red");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            Date date = new Date();
            String currentDate = dateFormat.format(date);

            jsonRequest.put("timestamp", currentDate);
            L.d("PostLocationServices ::makePostGeoLocation()" + currentDate);
            jsonRequest.put("type", "track");
            jsonRequest.put("surveyor", ESurvey.getLoginId());
            if (location != null) {
                jsonRequest.put("latlong", location.getLatitude() + "," + location.getLongitude());
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
                }
            });
            queue.add(stringRequest);
        }//
        catch (Exception ex) {
            L.e("Error " + ex.getMessage());
            return;
        }//
    }
}
