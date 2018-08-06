package hmf.anyasoft.es.surveyapp.api;

import hmf.anyasoft.es.surveyapp.utility.AppConstant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by home on 2/15/2018.
 */

public class LocCaptureClient {
  //  private static final String ROOT_URL = "http://192.168.0.105:9090/";

    public LocCaptureClient() {

    }

    /**
     * Get Retro Client
     *
     * @return JSON Object
     */
    private static Retrofit getRetroClient() {
        return new Retrofit.Builder()
                .baseUrl(AppConstant.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static LocCaptureApi getApiService() {
        return getRetroClient().create(LocCaptureApi.class);
    }
}

