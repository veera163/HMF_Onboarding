package hmf.com.project.onboarding.internet;

import hmf.com.project.onboarding.ESurvey;
import hmf.com.project.onboarding.camera.ImageString;
import hmf.com.project.onboarding.domains.UserDomain;
import hmf.com.project.onboarding.logger.L;
import hmf.com.project.onboarding.question.QuestionModel;
import hmf.com.project.onboarding.realm.models.SurveyAnswer;
import hmf.com.project.onboarding.realm.models.SurveyResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by saurabh.singh on 4/20/2016.
 */
public class JSONConverter {
    private static final String KEY_ARRAY = "questions";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_QID = "q_id";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_SET_ID = "set_id";
    private static final String KEY_ATTACHMENT = "attachment";
    private static final String KEY_PHOTO = "photo";

    public static JSONObject createJSON(String surveyActivityId) throws JSONException, IOException {
        if (QuestionModel.questionList == null || QuestionModel.questionList.size() == 0) {
            throw new IllegalArgumentException("List is empty");

        }//
        JSONObject json = new JSONObject();
        json.put(KEY_SET_ID, "1");
        json.put("latlong", QuestionModel.latLong + "");
        JSONArray jsonArray = new JSONArray();
        for (QuestionModel model : QuestionModel.questionList) {
            JSONObject obj = new JSONObject();
            obj.put(KEY_QID, model.getQuestId());
            obj.put(KEY_ANSWER, model.getAnswer());
            obj.put(KEY_TIMESTAMP, model.getBookmark());
            jsonArray.put(obj);
        }//
        json.put(KEY_ARRAY, jsonArray);
        int i = 1;
        UserDomain userDomain = ESurvey.getUser();
        JSONObject jsonDetails = new JSONObject(QuestionModel.details);

        L.d("ConvertJson()::" + "Qustion details feed");
        json.put("phtoto1", "");
        json.put("phtoto2", "");
        json.put("phtoto3", "");
        json.put("badgeColor", userDomain.getBadgeColor());
        json.put("surveyor_id", userDomain.getSurveyorId());
        json.put("surveyActivityId", surveyActivityId);
        if (null != ImageString.photoStringList) {
            L.d("ConvertJson()::" + "attaching the photo");
            for (String s : ImageString.photoStringList) {
//                L.d("createJSON()::"+s.length());
//                L.d("createJSON()::"+s);
                String key = "photo" + i;
                i++;
                json.put(key, s);
            }//for
        }//

        json.put("Reviewed", false);
        json.put("personal_details", jsonDetails);

        json.put(KEY_ATTACHMENT, Base64Strings.encodeToBase64(0));
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String currentDate = dateFormat.format(date);
        json.put("Date", currentDate);


        L.d("" + json.get("personal_details"));
        return json;
    }

    public static JSONObject createJSONFromModel(SurveyResponse response) throws JSONException, IOException {
        if (response == null) {
            throw new IllegalArgumentException("Null object from DB");

        }//
        JSONObject json = new JSONObject();
        json.put(KEY_SET_ID, response.getSetID());
        JSONArray jsonArray = new JSONArray();
        for (SurveyAnswer model : response.getQuestions()) {
            JSONObject obj = new JSONObject();
            obj.put(KEY_QID, model.getQuesId());
            obj.put(KEY_ANSWER, model.getAnswer());
            obj.put(KEY_TIMESTAMP, model.getTimestamp());
            jsonArray.put(obj);
        }//
        json.put(KEY_ARRAY, jsonArray);
        int i = 1;
        L.d("ConvertJson()::" + "Qustion details feed");
        json.put("phtoto1", response.getPhoto1());
        json.put("phtoto2", response.getPhoto2());
        json.put("phtoto3", response.getPhoto3());


        json.put("Reviewed", false);
        json.put(KEY_ATTACHMENT, response.getAttachment());
        json.put("latlong", response.getLatLong() + "");
        json.put("Date", response.getDate());
        JSONObject jsonDetails = new JSONObject(response.getDetails());
        json.put("personal_details", jsonDetails);
        L.d("" + json.get("personal_details"));
        return json;
    }
}
