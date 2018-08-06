package hmf.com.project.onboarding.api;

import hmf.com.project.onboarding.domains.PolygonRes;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by home on 7/4/2018.
 */

public interface PolygonApi {
    @GET
    Call<PolygonRes> getData(@Url String url);

}
