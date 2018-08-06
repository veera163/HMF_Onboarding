package hmf.anyasoft.es.surveyapp.api;

import hmf.anyasoft.es.surveyapp.api.response.UploadImageResult;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by home on 2/15/2018.
 */

public interface LocCaptureApi {

    @Multipart
    @POST("upload/landDetails/boundariesImage/farmerId/{farmerId}")
    Call<UploadImageResult> uploadImage(@Part MultipartBody.Part[]  file, @Path("farmerId") String farmerId);
}
