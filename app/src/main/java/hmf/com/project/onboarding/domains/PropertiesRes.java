package hmf.anyasoft.es.surveyapp.domains;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by home on 4/4/2018.
 */

public class PropertiesRes {

    @SerializedName("landLordPhoneNumber")
    @Expose
    private String landLordPhoneNumber;

    @SerializedName("tenantId")
    @Expose
    private String tenantId;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    @SerializedName("tenantName")

    @Expose
    private String tenantName;




    @Override
    public String toString() {
        return "PropertiesRes{" +
                "landLordPhoneNumber='" + landLordPhoneNumber + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", tenantName='" + tenantName + '\'' +
                ", idDetails=" + idDetails +
                ", boundaryPoints=" + boundaryPoints +
                ", imageUrls=" + imageUrls +
                '}';
    }



    @SerializedName("idProof")

    @Expose
    private IdDetails idDetails;


    public IdDetails getIdDetails() {
        return idDetails;
    }

    public void setIdDetails(IdDetails idDetails) {
        this.idDetails = idDetails;
    }

    @SerializedName("boundaryPoints")
    @Expose
    private List<String> boundaryPoints;

    public String getLandLordPhoneNumber() {
        return landLordPhoneNumber;
    }

    public void setLandLordPhoneNumber(String landLordPhoneNumber) {
        this.landLordPhoneNumber = landLordPhoneNumber;
    }

    @SerializedName("imageUrls")
    @Expose
    private List<String> imageUrls;



    public List<String> getBoundaryPoints() {
        return boundaryPoints;
    }

    public void setBoundaryPoints(List<String> boundaryPoints) {
        this.boundaryPoints = boundaryPoints;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
