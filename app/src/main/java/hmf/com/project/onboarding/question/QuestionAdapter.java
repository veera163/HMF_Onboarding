package hmf.com.project.onboarding.question;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hmf.com.project.onboarding.R;

/**
 * Created by saurabh.singh on 4/18/2016.
 */
public class QuestionAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<QuestionModel> questionModels;

    public QuestionAdapter(Context context) {
        if(null == context){
           throw new IllegalArgumentException("Context is null");
        }//
        this.context = context;
        if(null == QuestionModel.questionList || QuestionModel.questionList.size() == 0){
            throw new IllegalArgumentException("Question list is empty");
        }
        questionModels =  QuestionModel.questionList;
    }

    @Override
    public int getCount() {
        return questionModels.size();
    }

    @Override
    public Object getItem(int position) {
        return questionModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater =  LayoutInflater.from(context);
            convertView    = inflater.inflate(hmf.com.project.onboarding.R.layout.list_item , parent ,false);
        }//
        TextView tvQuestion = (TextView) convertView.findViewById(R.id.tvListQuestion);
        TextView tvAnswer = (TextView) convertView.findViewById(R.id.tvAnswer);
        TextView tvTimeStamp = (TextView) convertView.findViewById(R.id.tvTimeStamp);
        QuestionModel model =  questionModels.get(position);
        tvQuestion.setText(model.getQuestId() +" : "+model.getQuestion());
//        L.d("Position at "+position + "setting answers " + model.getAnswer());
        if(null != model.getAnswer() && !model.getAnswer().equals("")){
            tvAnswer.setText(model.getAnswer());
        }//if()....
        else{
            tvAnswer.setText("Not Answered");
        }
        if(-1 != model.getBookmark()){
            tvTimeStamp.setText(model.getBookmark()/60000 + ":"+ (model.getBookmark()%60000)/1000);
        }//if()....

        return convertView;
    }//getView()
}
