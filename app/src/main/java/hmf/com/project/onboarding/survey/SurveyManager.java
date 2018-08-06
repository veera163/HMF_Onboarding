package hmf.com.project.onboarding.survey;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import hmf.com.project.onboarding.ESurvey;
import hmf.com.project.onboarding.ProfileActivity;
import hmf.com.project.onboarding.internet.EncodedJSONRequest;
import hmf.com.project.onboarding.internet.JSONParser;
import hmf.com.project.onboarding.internet.NetworkUtil;
import hmf.com.project.onboarding.internet.VolleySingleton;
import hmf.com.project.onboarding.logger.L;
import hmf.com.project.onboarding.question.QuestionModel;
import hmf.com.project.onboarding.realm.models.Survey;
import hmf.com.project.onboarding.realm.models.SurveyQuestion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;

/**
 * Created by saurabh.singh on 7/2/2016.
 */
public class SurveyManager {

    private WeakReference<Context> weakContext;
    private ProgressDialog pd;
    private RequestQueue queue;
    private static final int MY_SOCKET_TIMEOUT_MS = 15 * 1000 * 1;

    public SurveyManager(WeakReference<Context> weakContext) {
        this.weakContext = weakContext;
        queue = VolleySingleton.getInstance().getRequestQueue();
    }//

    public void getSurveySet() {
        if (NetworkUtil.isOnline(weakContext.get())) {
            L.d("getSurveySet():: fetching question online ");
            sendRequest();
            L.d("getSurveySet():: fetched question online ");
        }//
        else {
            L.d("getSurveySet():: fetching question locally ");
            loadSurveyFromLocalDB();
            //loadNext();
            L.d("getSurveySet():: fetched question locally ");
        }//
    }//
    private void loadNext(){
        Activity activity = (Activity)weakContext.get();
        activity.startActivity(new Intent(activity , TakeSurveyActivity.class));
    }

    private void sendRequest() {
        pd = new ProgressDialog(weakContext.get());
        pd.setMessage("Fetching the Question from server.");

        try {

            String url = ESurvey.URL+"surveyActivity/tenant/getSurveyorActivitiesV2/"+ProfileActivity.tid+"/"+ProfileActivity.phone;
            Log.e("ram",url);
         //   String url = "https://s3-us-west-2.amazonaws.com/www.myeventorganiser.com/getQuestions.json";
            JsonObjectRequest stringRequest = new EncodedJSONRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    L.d("Success Received");
                    if (null != response) {
                        pd.dismiss();
                        try {
                            L.d(response + "-----------");
                            JSONParser.inflateData(response);
                            cacheSurveyToDB();
                            loadNext();
                        } catch (Exception e) {
                            L.e(e.getMessage() + "From getQuestion continuing with cached data");
                            loadSurveyFromLocalDB();
                            pd.dismiss();
                            return;
                        }//catch()
                    }//if()....
                }//onResponse()...
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    L.e(" From getQuestion Continue with Cached data" + error);
                    pd.dismiss();
                    loadSurveyFromLocalDB();
                }//OnErrorResponse...
            });

            // Add the request to the RequestQueue.
            pd.show();
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        }//
        catch (Exception ex) {
            L.e("Error 1.222" + ex.getMessage());
            pd.dismiss();
            //loadSurveyFromLocalDB();
            return;
        }//
    }//sendRequest()..
    private void cacheSurveyToDB(){
        if(QuestionModel.questionList != null && !QuestionModel.questionList.isEmpty()){
            Realm realm =  Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    Survey survey =  bgRealm.createObject(Survey.class);
                    survey.setSetID(QuestionModel.setID);

                    for (QuestionModel model : QuestionModel.questionList){
                        SurveyQuestion question =  bgRealm.createObject(SurveyQuestion.class);
                        question.setqId(model.getQuestId());
                        question.setQuestion(model.getQuestion());
                        question.setOptA(model.getOptA());
                        question.setOptB(model.getOptB());
                        question.setOptD(model.getOptD());
                        question.setOptC(model.getOptC());
                        question.setOptE(model.getOptE());
                        question.setIsMultiple(model.isMultiple());
                        survey.getQuestions().add(question);
                    }//
                }//
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    // Transaction was a success.
                    L.d("cacheSurveyToDB():: data cached locally");
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    // Transaction failed and was automatically canceled.
                    L.e("cacheSurveyToDB():: data caching failed");
                }
            });

        }//
    }//cacheSurveyToDB...
    private void loadSurveyFromLocalDB(){

        QuestionModel.questionList.clear();
        Realm realm =  Realm.getDefaultInstance();
        RealmQuery<Survey> query =  realm.where(Survey.class);
        Survey survey = query.findFirst();
        if(null ==  survey){
            loadNext();
            L.e("NO data offline");
            return;
        }//null check...
        QuestionModel.setID =  survey.getSetID();
        RealmList<SurveyQuestion> list = survey.getQuestions();
        if(list!= null && !list.isEmpty()){
            for(SurveyQuestion s: list){
                QuestionModel model  = new QuestionModel();
                model.setQuestId(s.getqId());
                model.setQuestion(s.getQuestion());
                model.setIsMultiple(s.isMultiple());
                model.setOptA(s.getOptA());
                model.setOptB(s.getOptB());
                model.setOptC(s.getOptC());
                model.setOptD(s.getOptD());
                model.setOptE(s.getOptE());
                model.setOptF(s.getOptF());
                model.setOptG(s.getOptG());

                model.setOptH(s.getOptH());
                model.setOptI(s.getOptI());
                model.setOptJ(s.getOptJ());
                QuestionModel.questionList.add(model);
            }//for...
        }//if()...

        loadNext();
    }//
    public void destroySurveyManager(){
        weakContext.clear();
        weakContext =  null;
    }//

    public void testConnection() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                L.d("Calling");
                String url = "https://s3-us-west-2.amazonaws.com/www.myeventorganiser.com/getQuestions.json";
                URL urlString = null;
                try {
                    urlString = new URL(url);
                    HttpURLConnection urlConnection = (HttpURLConnection) urlString.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                    StringBuilder sb = new StringBuilder();
                    String line = "";
                    int count = 0;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        count++;
                    }
                    in.close();
                    L.d("Count = " + count);
                   // L.d("Response" + sb.toString());
                    JSONParser.inflateDataDummy(new JSONObject(sb.toString()));
                    cacheSurveyToDB();
                    loadNext();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    L.e("Exception " + e.getLocalizedMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    L.e("Exception " + e.getLocalizedMessage());
                }//
                catch (JSONException e) {
                    e.printStackTrace();
                    L.e("Exception " + e.getLocalizedMessage());
                }


            }
        }).start();
    }
}
