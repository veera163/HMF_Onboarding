package hmf.com.project.onboarding.domains;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by home on 7/4/2018.
 */

public class PolygonRes {

    @SerializedName("features")
    @Expose
    private List<Feature> features = null;
    @SerializedName("type")
    @Expose
    private String type;

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
