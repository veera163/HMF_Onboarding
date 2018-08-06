package hmf.com.project.onboarding.survey;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import hmf.com.project.onboarding.ESurvey;
import hmf.com.project.onboarding.camera.ImageString;
import hmf.com.project.onboarding.internet.Base64Strings;
import hmf.com.project.onboarding.internet.EncodedJSONRequest;
import hmf.com.project.onboarding.internet.JSONConverter;
import hmf.com.project.onboarding.internet.NetworkUtil;
import hmf.com.project.onboarding.internet.VolleySingleton;
import hmf.com.project.onboarding.logger.L;
import hmf.com.project.onboarding.question.QuestionModel;
import hmf.com.project.onboarding.realm.models.SurveyAnswer;
import hmf.com.project.onboarding.realm.models.SurveyResponse;
import hmf.com.project.onboarding.utility.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by saurabh.singh on 7/3/2016.
 */
public class SurveyResponseManager  {

    private WeakReference<Context> weakContext;
    private ProgressDialog pd;
    private RequestQueue queue;
    private static final int MY_SOCKET_TIMEOUT_MS = 60 * 1000 * 1;

    public SurveyResponseManager(WeakReference<Context> weakContext) {
        this.weakContext = weakContext;
        queue = VolleySingleton.getInstance().getRequestQueue();
    }//
    public void storeSurveyResponse() {
        if (NetworkUtil.isOnline(weakContext.get())) {
            L.d("storeSurveyResponse():: fetching question online ");
            sendRequest();
            L.d("storeSurveyResponse():: fetched question online ");
        }//
        else {
            L.d("storeSurveyResponse():: fetching question locally ");
            cacheSurveyResponseToDB();
            //loadNext();
            L.d("storeSurveyResponse():: fetched question locally ");
        }//
    }//
    private void sendRequest() {


        pd = new ProgressDialog(weakContext.get());
        pd.setMessage("Uploading File to Server");
        pd.setCancelable(false);
        String url = AppConstant.BASEURL+"/postPeopleResponse";
        JSONObject resultJson = null;
        try {
            resultJson = JSONConverter.createJSON(ESurvey.getSurveyActivityId());
        } catch (Exception e) {
            L.e("Error in sending file " + e.getMessage());
            pd.dismiss();
            return;
        }//exception

        L.d("length of file while uploading to server " + resultJson.length());

        L.e("res " + resultJson.toString());

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new EncodedJSONRequest(Request.Method.POST, url, resultJson,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String error = response.getString("error");
                            String message = response.getString("message");
                            L.d(error + " ---" + message);
                            Log.e("res",message);
                            if(Boolean.parseBoolean(error)){
                                cacheSurveyResponseToDB();
                                ((Activity) weakContext.get()).finish();
                                return;
                            }//
                            Toast.makeText(weakContext.get(), "Uploaded SuccessFully", Toast.LENGTH_SHORT).show();
                            QuestionModel.clear();
                            ImageString.clearAll();
                            ((Activity) weakContext.get()).finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ((Activity) weakContext.get()).finish();
                        }
                        L.d("upload successfully");
                        pd.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                error.printStackTrace();
                L.e(error + "");
                L.e(error.getLocalizedMessage() + "");
                pd.dismiss();
                cacheSurveyResponseToDB();
                QuestionModel.clear();
                ImageString.clearAll();
//                launchDialog("That didn't work! Upload fails" , "Close");
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
    private void cacheSurveyResponseToDB(){
        if(QuestionModel.questionList != null && !QuestionModel.questionList.isEmpty()){
            Realm realm =  Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    SurveyResponse survey =  bgRealm.createObject(SurveyResponse.class);
                    survey.setLatLong(QuestionModel.latLong +"");
                    survey.setSetID(QuestionModel.setID);
                    if(null != ImageString.photoStringList) {
                        L.d("ConvertJson()::"+ "attaching the photo");
                        int i = 0;
                        for (String s : ImageString.photoStringList) {
//
                            if(i == 0){
                                survey.setPhoto1(s);
                            }
                            if(i == 1){
                                survey.setPhoto2(s);
                            }
                            if(i == 2){
                                survey.setPhoto3(s);
                            }
                            i++;
                        }//for
                    }//


                    try {
                        survey.setAttachment(Base64Strings.encodeToBase64(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Date date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    String currentDate = dateFormat.format(date);
                    survey.setDate(currentDate);
                    survey.setDetails(QuestionModel.details+"");
                    for (QuestionModel model : QuestionModel.questionList){
                        SurveyAnswer question =  bgRealm.createObject(SurveyAnswer.class);
                        question.setQuesId(model.getQuestId());
                        question.setAnswer(model.getAnswer());
                        question.setTimestamp(model.getBookmark()+"");
                        survey.getQuestions().add(question);
                    }//
                }//
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    // Transaction was a success.
                    L.d("cacheSurveyResponseToDB():: data cached locally");
                    Toast.makeText(weakContext.get(), "Stored Locally", Toast.LENGTH_SHORT).show();
                            ((Activity) weakContext.get()).finish();
                    QuestionModel.clear();
                    ImageString.clearAll();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    // Transaction failed and was automatically canceled.
                    L.e("cacheSurveyResponseToDB():: data caching failed");
                    QuestionModel.clear();
                    ImageString.clearAll();
                }
            });

        }//
    }//cacheSurveyResponseToDB...
   private  RealmResults<SurveyResponse> allSurvey;
    public void emptySurveyResponseCache(){
        Realm realm =  Realm.getDefaultInstance();
        allSurvey = realm.where(SurveyResponse.class)
                .equalTo("isSynced",true).findAllAsync();
        allSurvey.addChangeListener(callback);
    }//emptySurveyResponseCache()...
    private RealmChangeListener callback = new RealmChangeListener() {


        @Override
        public void onChange() {
            Realm realm =  Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    allSurvey.deleteAllFromRealm();
                }
            });
        }
    };

    public void onStop(){
       if(null!= allSurvey)
        allSurvey.removeChangeListener(callback);
    }//onStop
}
