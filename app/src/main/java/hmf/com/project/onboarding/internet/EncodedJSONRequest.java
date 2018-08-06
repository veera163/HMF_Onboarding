package hmf.com.project.onboarding.internet;

import com.android.volley.Response;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by saurabh.singh on 6/8/2016.
 */
public class EncodedJSONRequest extends JsonObjectRequest {


    public EncodedJSONRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }//

    @Override
    public Priority getPriority() {
        return Priority.IMMEDIATE;
    }
}
