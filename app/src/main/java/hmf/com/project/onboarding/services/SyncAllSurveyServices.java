package hmf.com.project.onboarding.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import hmf.com.project.onboarding.ESurvey;
import hmf.com.project.onboarding.internet.EncodedJSONRequest;
import hmf.com.project.onboarding.internet.JSONConverter;
import hmf.com.project.onboarding.internet.VolleySingleton;
import hmf.com.project.onboarding.logger.L;
import hmf.com.project.onboarding.realm.models.SurveyResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by saurabh.singh on 7/12/2016.
 */
public class SyncAllSurveyServices extends IntentService {

    private static final int MY_SOCKET_TIMEOUT_MS = 2*60*1000;
    private static final int MY_NOTIFICATION_ID = 10;
    private RequestQueue queue;
    private RealmResults<SurveyResponse> allSurvey;
    String errorString  = null;
    private NotificationManager notificationManager;
    private Notification myNotification;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    
    public SyncAllSurveyServices(String name) {
        super(name);

    }
    public SyncAllSurveyServices() {
        super("Sync Service");
    }//
    private Realm realm = null;
    @Override
    protected void onHandleIntent(Intent intent) {
        queue = VolleySingleton.getInstance().getRequestQueue();
        String notificationText = "Uploading";
        L.d("thread name ::"+Thread.currentThread().getName());
        myNotification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Progress")
                .setContentText(notificationText)
                .setTicker("Notification!")
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(hmf.com.project.onboarding.R.mipmap.ic_launcher)
                .build();
        notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
        realm =  Realm.getDefaultInstance();
        L.d("SyncAllSurveyServices::onHandleIntent():::started");
        allSurvey = realm.where(SurveyResponse.class)
                .equalTo("isSynced", false).findAll();
        L.d("SyncAllSurveyServices::onHandleIntent()::List fetched::" + allSurvey.size());
        if(allSurvey != null && !allSurvey.isEmpty()){

            L.d("SyncAllSurveyServices::onHandleIntent():::survey");
           for(SurveyResponse survey : allSurvey){
                JSONObject response = sendRequest(survey);
               L.d("SyncAllSurveyServices::onHandleIntent():::"+response+"----");
               if(null != response){
                   String error = null;
                   try {
                       error = response.getString("error");
                       String message = response.getString("message");
                       L.d(error + " ---" + message);
                       boolean success =  !Boolean.parseBoolean(error);
                       L.d("Success status "+ success);
                       if(success){
                           realm.beginTransaction();
                           survey.setIsSynced(true);
                           L.d("SyncAllSurveyServices::sendRequest()::setting the update values");
                           realm.commitTransaction();
                           L.d("upload successfully");
                       }//
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

               }//
           }//for

        }//
        L.d("SyncAllSurveyServices::sendRequest()::Deleting the synced data from server");
        removeCahedDataFromDataBase();


    }//
    private JSONObject sendRequest(final SurveyResponse survey) {
        JSONObject resultJson =  null;
        try {
            resultJson = JSONConverter.createJSONFromModel(survey);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }//

        if(resultJson== null){
            return null;
        }//
        L.d("SyncAllSurveyServices::sendRequest()::JSON created" );
        String url = ESurvey.URL+"/postPeopleResponse";

        L.d("SyncAllSurveyServices::sendRequest()::length of file while uploading to server " + resultJson.length());


        // Request a string response from the provided URL.
        RequestFuture<JSONObject> future =  RequestFuture.newFuture();
        JsonObjectRequest stringRequest = new EncodedJSONRequest(Request.Method.POST, url, resultJson,future, future);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue
        queue.add(stringRequest);
        try {
            L.d("SyncAllSurveyServices::sendRequest():::done");
            L.d("thread name ::"+Thread.currentThread().getName());

            return future.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            L.e("SyncAllSurveyServices::sendRequest():::"+e.getLocalizedMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            L.e("SyncAllSurveyServices::sendRequest():::" + e.getLocalizedMessage());
        } catch (TimeoutException e) {
            e.printStackTrace();
            L.e("SyncAllSurveyServices::sendRequest():::" + e.getLocalizedMessage());
        }
        L.e("SyncAllSurveyServices::sendRequest():::failed");
        return  null;

    }//

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.d("Service destroyed...");
        notificationManager.cancel(MY_NOTIFICATION_ID);
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

    }//
    public void removeCahedDataFromDataBase(){
        Realm realm =  Realm.getDefaultInstance();
        allSurvey = realm.where(SurveyResponse.class)
                .equalTo("isSynced",true).findAll();
        int records =  allSurvey.size();
        if(!allSurvey.isEmpty()){
            realm.beginTransaction();
            boolean deleted =  allSurvey.deleteAllFromRealm();
            realm.commitTransaction();
            L.d("SyncAllSurveyServices::removeCahedDataFromDataBase()::deleted "+deleted +" " +
                    "total are "+  records  );
        }
    }//removeCahedDataFromDataBase()...
}
