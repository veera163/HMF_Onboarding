package hmf.anyasoft.es.surveyapp.domains;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by home on 4/4/2018.
 */

public class IdDetails {
    @SerializedName("proofId")
    @Expose
    private String proofId;

    @Override
    public String toString() {
        return "IdDetails{" +
                "proofId='" + proofId + '\'' +
                ", idProofType='" + idProofType + '\'' +
                '}';
    }

    public String getProofId() {
        return proofId;
    }

    public void setProofId(String proofId) {
        this.proofId = proofId;
    }

    public String getIdProofType() {
        return idProofType;
    }

    public void setIdProofType(String idProofType) {
        this.idProofType = idProofType;
    }

    @SerializedName("idProofType")
    @Expose

    private String idProofType;
}
