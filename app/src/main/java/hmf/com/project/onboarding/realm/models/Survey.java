package hmf.com.project.onboarding.realm.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by saurabh.singh on 7/2/2016.
 */
public class Survey extends RealmObject {

    @PrimaryKey
    private String setID;
    private RealmList<SurveyQuestion> questions;


    public String getSetID() {
        return setID;
    }

    public void setSetID(String setID) {
        this.setID = setID;
    }

    public RealmList<SurveyQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(RealmList<SurveyQuestion> questions) {
        this.questions = questions;
    }
}
