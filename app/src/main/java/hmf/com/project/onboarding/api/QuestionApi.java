package hmf.anyasoft.es.surveyapp.api;

import hmf.anyasoft.es.surveyapp.domains.QuestionSet;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by home on 2/22/2018.
 */

public interface QuestionApi {
    @Headers("Content-Type: application/json")
    @POST("productViewDetails/getShippingCharges")
    Call<QuestionSet> getUser(@Body String body);
}
