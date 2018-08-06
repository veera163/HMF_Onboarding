package hmf.com.project.onboarding.utility;

import android.text.format.DateUtils;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Sai on 9/16/2015.
 */
public class HttpHelper {
    private static long TIMEOUT_CONNECT_MILLIS = (60 * DateUtils.SECOND_IN_MILLIS);
    private long TIMEOUT_READ_MILLIS = TIMEOUT_CONNECT_MILLIS - 5000;

    public HttpHelper() {
   }

    public static String sendPOSTRequest(String urltoPOST, String paramtoPOST, String header) {
        LoggerUtils.info(HttpHelper.class.getSimpleName(), "url to post" + urltoPOST);
        LoggerUtils.info(HttpHelper.class.getSimpleName(), "params to post" + paramtoPOST);

        DataOutputStream outputstream;

        int statuscode = 0;

        String response = "NA";

        try {
            URL Url = null;

            Url = new URL(urltoPOST.replace(" ", "%20"));


            try {
                HttpURLConnection urlConnection = (HttpURLConnection) Url.openConnection();

                urlConnection.setRequestMethod("POST");

                urlConnection.setConnectTimeout((int) TIMEOUT_CONNECT_MILLIS);

                urlConnection.setDoInput(true);

                urlConnection.setDoOutput(true);

                urlConnection.setUseCaches(false);

                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("authorization", AppConstant.HEADER);

                LoggerUtils.info(HttpHelper.class.getSimpleName(), "header   " + header);

                outputstream = new DataOutputStream(urlConnection.getOutputStream());

                outputstream.writeBytes(paramtoPOST);

                outputstream.flush();

                outputstream.close();

                urlConnection.connect();

                //Get Response

                statuscode = urlConnection.getResponseCode();

                LoggerUtils.debug(HttpHelper.class.getSimpleName(), "HTTP STATUS CODE is" + statuscode);

                if (statuscode == HttpURLConnection.HTTP_OK) {
                    InputStream responseStream = new BufferedInputStream(urlConnection.getInputStream());

                    BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

                    String line = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = responseStreamReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    responseStreamReader.close();

                    response = stringBuilder.toString();
                    if (response.equalsIgnoreCase("")) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("statusCode", 200);
                        response = jsonObject.toString();
                    }
                } else if (statuscode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    InputStream responseStream = new BufferedInputStream(urlConnection.getErrorStream());

                    BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

                    String line = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = responseStreamReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    responseStreamReader.close();

                    response = stringBuilder.toString();

                } else {
                    response ="NA";
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {


            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static String sendGETRequest(String urltoPOST, String header) {
        LoggerUtils.info(HttpHelper.class.getSimpleName(), "on HttpHelper sendGETRequest");

        DataOutputStream outputstream;


        int statuscode = 0;

        String response = "NA";

        try {
            LoggerUtils.info(HttpHelper.class.getName(), urltoPOST);
            URL Url = new URL(urltoPOST);
            LoggerUtils.info(HttpHelper.class.getName(), "after url");
            try {
                HttpURLConnection connection = (HttpURLConnection) Url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout((int) TIMEOUT_CONNECT_MILLIS);
                connection.setDoOutput(false);
                connection.setRequestProperty("authorization", "Bearer "+header);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.connect();
                //Get Response
                statuscode = connection.getResponseCode();
                LoggerUtils.debug(HttpHelper.class.getSimpleName(), "HTTP STATUS CODE is" + statuscode);
                if (statuscode == HttpURLConnection.HTTP_OK) {
                    InputStream responseStream = connection.getInputStream();
                    BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

                    String line = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = responseStreamReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    responseStreamReader.close();

                    response = stringBuilder.toString();
                } else {
                    response ="NA";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return response;
    }
}
