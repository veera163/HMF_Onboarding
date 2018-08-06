package hmf.com.project.onboarding.realm.models;

import io.realm.RealmObject;

/**
 * Created by saurabh.singh on 7/3/2016.
 */
public class SurveyAnswer extends RealmObject {

    private String quesId;
    private String answer;
    private String timestamp;

    public String getQuesId() {
        return quesId;
    }

    public void setQuesId(String quesId) {
        this.quesId = quesId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
