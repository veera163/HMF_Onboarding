package hmf.com.project.onboarding.internet;

import hmf.com.project.onboarding.logger.L;
import hmf.com.project.onboarding.question.QuestionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by saurabh.singh on 4/18/2016.
 */
public class JSONParser {
    private static final String MESSAGE = "questionSets";
    private static final String ARRAY_KEY = "questions";
    private static final String QUESTION_KEY = "question";
    private static final String CHOICES_KEY = "options";
    private static final String QUESTION_ID = "q_id";
    private static final String TYPE_KEY = "type";

    public static void inflateData(JSONObject jsonObject) throws JSONException, IllegalArgumentException, UnsupportedEncodingException {

        QuestionModel.questionList.clear();
        JSONArray mainArray;
        JSONArray questionArray = null;
        JSONObject obj;
         L.d("Inflate data()1.0 " + jsonObject.toString());
        if (jsonObject.has(MESSAGE)) {
            String temp = jsonObject.getString(MESSAGE);
            mainArray =  jsonObject.getJSONArray(MESSAGE);
            L.d("Array size = " +mainArray.length());
            obj =  mainArray.getJSONObject(0);
            if (obj != null || obj.has(ARRAY_KEY)) {

                questionArray = obj.getJSONArray(ARRAY_KEY);
            }//if()
            else {
                throw new IllegalArgumentException("JSON format doesn't contains such key");
            }//else
        }//
        else {
            throw new IllegalArgumentException("JSON format doesn't contains such key");
        }


        if (questionArray.length() > 0) {
            L.d("inflateData()1.1 " + "Array Found");
            for (int i = 0; i < questionArray.length(); i++) {
                JSONObject json = (JSONObject) questionArray.get(i);
                if (null != json) {
                    if (json.has(QUESTION_KEY)) {
//                        L.d("Inflatedata()1.0 " + "Question found Found");
                        QuestionModel model = new QuestionModel();
                        model.setQuestion(json.getString(QUESTION_KEY));
                        model.setOptA("NA");
                        model.setOptB("NA");
                        model.setOptC("NA");
                        model.setOptD("NA");
                        model.setOptE("NA");
                        model.setOptF("NA");
                        model.setOptG("NA");
                        model.setOptH("NA");
                        model.setOptI("NA");
                        model.setOptJ("NA");
                        if (json.has(TYPE_KEY)) {
                            model.setIsMultiple(json.getBoolean(TYPE_KEY));
                        }//
                        if (json.has(CHOICES_KEY)) {
                            String options = json.getString(CHOICES_KEY);
                            String opt[] = options.split(",");
                            if (opt.length > 0) {

                                for (int j = 0; j < opt.length; j++) {
                                    switch (j) {
                                        case 0:
                                            model.setOptA(opt[j]);
                                            break;
                                        case 1:
                                            model.setOptB(opt[j]);
                                            break;
                                        case 2:
                                            model.setOptC(opt[j]);
                                            break;
                                        case 3:
                                            model.setOptD(opt[j]);
                                            break;
                                        case 4:
                                            model.setOptE(opt[j]);
                                            break;
                                        case 5:
                                            model.setOptF(opt[j]);
                                            break;
                                        case 6:
                                            model.setOptG(opt[j]);
                                            break;
                                        case 7:
                                            model.setOptH(opt[j]);
                                            break;
                                        case 8:
                                            model.setOptI(opt[j]);
                                            break;
                                    }//switch()...

                                }//for()...

                            }//if()

                        }//if()...

                        model.setQuestId(json.getString(QUESTION_ID));
                        QuestionModel.questionList.add(model);

                    }//
                    else {
                        throw new IllegalArgumentException("JSON format doesn't contains question");
                    }

                }//if()
            }//for()
        }//if()....
        else {
            throw new IllegalArgumentException("JSON arrays is empty");
        }//


    }//inflateData()
    public static void inflateDataDummy(JSONObject jsonObject) throws JSONException, IllegalArgumentException, UnsupportedEncodingException {

        QuestionModel.questionList.clear();
        JSONArray questionArray = null;
        JSONObject obj;
        L.d("Inflatedata()1.0 " + jsonObject.toString());
        if (jsonObject.has(MESSAGE)) {
            String temp = jsonObject.getString(MESSAGE);
            obj = new JSONObject(temp);
            if (obj != null || obj.has(ARRAY_KEY)) {

                questionArray = obj.getJSONArray(ARRAY_KEY);

            }//if()
            else {
                throw new IllegalArgumentException("JSON format doesn't contains such key");
            }//else
        }//
        else {
            throw new IllegalArgumentException("JSON format doesn't contains such key");
        }

        if (questionArray.length() > 0) {
            L.d("inflateData()1.1 " + "Array Found");
            for (int i = 0; i < questionArray.length(); i++) {
                JSONObject json = (JSONObject) questionArray.get(i);
                if (null != json) {
                    if (json.has(QUESTION_KEY)) {
//                        L.d("Inflatedata()1.0 " + "Question found Found");
                        QuestionModel model = new QuestionModel();
                        model.setQuestion(json.getString(QUESTION_KEY));
                        model.setOptA("NA");
                        model.setOptB("NA");
                        model.setOptC("NA");
                        model.setOptD("NA");
                        model.setOptE("NA");
                        model.setOptF("NA");
                        model.setOptG("NA");
                        model.setOptH("NA");
                        model.setOptI("NA");
                        model.setOptJ("NA");

                        if (json.has(TYPE_KEY)) {
                            model.setIsMultiple(json.getBoolean(TYPE_KEY));
                        }//
                        if (json.has(CHOICES_KEY)) {
                            String options = json.getString(CHOICES_KEY);
                            String opt[] = options.split(",");
                            if (opt.length > 0) {

                                for (int j = 0; j < opt.length; j++) {
                                    switch (j) {
                                        case 0:
                                            model.setOptA(opt[j]);
                                            break;
                                        case 1:
                                            model.setOptB(opt[j]);
                                            break;
                                        case 2:
                                            model.setOptC(opt[j]);
                                            break;
                                        case 3:
                                            model.setOptD(opt[j]);

                                            break;
                                        case 4:
                                            model.setOptE(opt[j]);
                                            break;
                                        case 5:
                                            model.setOptF(opt[j]);
                                            break;
                                        case 6:
                                            model.setOptG(opt[j]);
                                            break;
                                        case 7:
                                            model.setOptH(opt[j]);
                                            break;
                                        case 8:
                                            model.setOptI(opt[j]);
                                            break;


                                    }//switch()...

                                }//for()...

                            }//if()

                        }//if()...

                        model.setQuestId(json.getString(QUESTION_ID));
                        QuestionModel.questionList.add(model);

                    }//
                    else {
                        throw new IllegalArgumentException("JSON format doesn't contains question");
                    }

                }//if()
            }//for()
        }//if()....
        else {
            throw new IllegalArgumentException("JSON arrays is empty");
        }//


    }//inflateData()
}
