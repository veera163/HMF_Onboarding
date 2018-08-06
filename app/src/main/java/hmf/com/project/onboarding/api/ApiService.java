package hmf.com.project.onboarding.api;




import hmf.com.project.onboarding.api.response.UploadImageResult;
import hmf.com.project.onboarding.domains.PropertiesRes;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    /*
    Retrofit get annotation with our URL
    And our method that will return us the List of Contacts
    */
    @Multipart
    @POST("upload/landDetails/v3/farmerId/{farmerId}")
    Call<UploadImageResult> uploadImage(@Part MultipartBody.Part[]  file, @Part("json") PropertiesRes json, @Path("farmerId") String farmerId);
   /* @GET("location")
    Call<Result> test();*/
}
