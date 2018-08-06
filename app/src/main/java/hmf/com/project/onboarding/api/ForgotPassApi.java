package hmf.com.project.onboarding.api;

import hmf.com.project.onboarding.domains.SimpleRes;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by home on 6/21/2018.
 */

public interface ForgotPassApi {

    @GET
    Call<SimpleRes> getData(@Url String url);
}
