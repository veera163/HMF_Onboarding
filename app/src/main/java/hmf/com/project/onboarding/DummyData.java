package hmf.com.project.onboarding;

import hmf.com.project.onboarding.logger.L;
import hmf.com.project.onboarding.question.QuestionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saurabh.singh on 4/14/2016.
 */
public class DummyData {
    private static final String MESSAGE = "message";
    private static final String ARRAY_KEY = "questions";
    private static final String QUESTION_KEY = "question";
    private static final String CHOICES_KEY = "options";
    private static final String QUESTION_ID = "q_id";


    private static String data = "{'set_id': '1','enabled': true," +
            "'questions': [{'q_id': '1','question': 'Do You know your corporator ','options': ['Yes', 'No'],'enabled': true}," +
            "{'q_id': '2','question': 'Do You know which party he belongs to','options': [],'enabled': true}," +
            "{'q_id': '3','question': 'Have you ever seen him personally','options': [],'enabled': true}," +
            "{'q_id': '4','question': 'Do you recognise him','options': ['if yes, by which means (posters etc)'],'enabled': true}," +
            "{'q_id': '5','question': 'Do you know that elections are due in your municipality / constituency','options': ['Yes', 'No'],'enabled': true}," +
            "{'q_id': '6','question': 'Have you ever been visited by any party functionary ?','options': ['Yes', 'No'],'enabled': true}," +
            "{'q_id': '7','question': 'Which party does the person belong to ','options': [],'enabled': true}," +
            "{'q_id': '8','question': 'Do you know him personally ','options': [],'enabled': true}," +
            "{'q_id': '9','question': 'Is he your neighbour ','options': ['Yes', 'No'],'enabled': true}," +
            "{'q_id': '10','question': 'Whom do you go to if you have any problems ','options': ['ward member ','corporator','legislator ','any other person'],'enabled': true}," +
            "{'q_id': '11','question': 'Do you know your Mayor','options': ['Yes', 'No'],'enabled': true}," +
            "{'q_id': '12','question': 'How do you rate the performance of Mayor / Municipality for maintaining Sanitation 5 is best','options': ['1','2','3','4','5'],'enabled': true}]}";
    public static void inflateData() throws JSONException, IllegalArgumentException {
        final JSONObject jsonObject  = new JSONObject(data);
        QuestionModel.questionList.clear();
        JSONArray questionArray = null;
        JSONObject obj;
        if (jsonObject.has(ARRAY_KEY)) {


            questionArray = jsonObject.getJSONArray(ARRAY_KEY);
                // L.d("Inflatedata()1.0 " + "Array Found");
            }//if()
            else {
                throw new IllegalArgumentException("JSON format doesn't contains such key");
            }//else


        if (questionArray.length() > 0) {
            L.d("Inflatedata()1.1 " + "Array Found");
            for (int i = 0; i < questionArray.length(); i++) {
                JSONObject json = (JSONObject) questionArray.get(i);
                if (null != json) {
                    if (json.has(QUESTION_KEY)) {
                        L.d("Inflatedata()1.0 " + "Question found Found");
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
                        if (json.has(CHOICES_KEY)) {
                            JSONArray options = json.getJSONArray(CHOICES_KEY);
                            if (options.length() > 0) {

                                for (int j = 0; j < options.length(); j++) {
                                    switch (j) {
                                        case 0:
                                            model.setOptA(options.optString(0));
                                            break;
                                        case 1:
                                            model.setOptB(options.optString(1));
                                            break;
                                        case 2:
                                            model.setOptC(options.optString(2));
                                            break;
                                        case 3:
                                            model.setOptD(options.optString(3));
                                            break;
                                        case 4:
                                            model.setOptE(options.optString(4));
                                            break;
                                        case 5:
                                            model.setOptF(options.optString(5));
                                            break;
                                        case 6:
                                            model.setOptG(options.optString(6));
                                            break;
                                        case 7:
                                            model.setOptH(options.optString(7));
                                            break;
                                        case 8:
                                            model.setOptI(options.optString(8));
                                            break;
                                        case 9:
                                            model.setOptJ(options.optString(8));
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
