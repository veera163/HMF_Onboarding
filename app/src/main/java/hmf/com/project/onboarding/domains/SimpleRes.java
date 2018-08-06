package hmf.com.project.onboarding.domains;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by home on 6/21/2018.
 */

public class SimpleRes {

    @SerializedName("messageType")
    @Expose
    private String messageType;
    @SerializedName("message")
    @Expose
    private String message;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
