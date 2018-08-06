package hmf.com.project.onboarding.realm.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by saurabh.singh on 7/3/2016.
 */
public class SurveyResponse extends RealmObject {
    private String setID;
    private String photo1="";
    private String photo2="";
    private String photo3="";
    private String date = "";
    private String attachment= "";
    private String details = "";


    private String latLong;

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    private boolean isSynced = false;

    private RealmList<SurveyAnswer> questions;

    public boolean isSynced() {
        return isSynced;
    }

    public void setIsSynced(boolean isSynced) {
        this.isSynced = isSynced;
    }

    public String getSetID() {
        return setID;
    }

    public void setSetID(String setID) {
        this.setID = setID;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public RealmList<SurveyAnswer> getQuestions() {
        return questions;
    }

    public void setQuestions(RealmList<SurveyAnswer> questions) {
        this.questions = questions;
    }
}
