package hmf.com.project.onboarding.domains;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by home on 7/4/2018.
 */

public class Feature {


    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
