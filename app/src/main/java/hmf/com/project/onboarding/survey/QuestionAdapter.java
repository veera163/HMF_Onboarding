package hmf.com.project.onboarding.survey;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hmf.com.project.onboarding.R;
import hmf.com.project.onboarding.logger.L;
import hmf.com.project.onboarding.question.QuestionModel;

import java.util.ArrayList;

/**
 * Created by saurabh.singh on 5/15/2016.
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private ArrayList<QuestionModel> questionModels;
    private OnItemClickedListener myListener;
    private int blueAnswered;
    private int greyUnAnswered;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }//onCreateViewHolder


    public QuestionAdapter(OnItemClickedListener listener) {
        if (null == listener) {
            throw new NullPointerException("Listener can not be null");
        }//if(null)..
        myListener = listener;

        if (null == QuestionModel.questionList || QuestionModel.questionList.size() == 0) {
            throw new IllegalArgumentException("Question list is empty");
        }
        questionModels = QuestionModel.questionList;
        blueAnswered = Color.parseColor("#446CB3");
        greyUnAnswered = Color.parseColor("#BDC3C7");

    }//QuestionAdapter()...

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final QuestionModel model = questionModels.get(position);
        if (holder.tvQuestion == null) {
            L.e("Null reference");
        }//
        holder.tvQuestion.setText(model.getQuestId() + " : " + model.getQuestion());

//        L.d("Position at "+position + "setting answers " + model.getAnswer());
        if (null != model.getAnswer() && !model.getAnswer().equals("")) {
            holder.tvAnswer.setText(model.getAnswer());
            holder.rlMain.setCardBackgroundColor(blueAnswered);
        }
        //if()....
        else {
            holder.tvAnswer.setText("Not Answered");
            holder.rlMain.setCardBackgroundColor(greyUnAnswered);
        }
        if (-1 != model.getBookmark()) {
            long min = model.getBookmark() / 60000;
            long sec = (model.getBookmark() % 60000) / 1000;
            L.d("time in milli " + model.getBookmark() + "-----" + min + ":" + sec);
            String m = "";
            String s = "";
            if (min < 9) {
                m = "0" + min;
            }//
            else {
                m = min + "";
            }
            if (sec < 9) {
                s = "0" + sec;
            } else {
                s = sec + "";
            }
            holder.tvTimeStamp.setText(m + ":" + s);
            holder.tvTimeStamp.setTextColor(Color.parseColor("#000000"));
        }//if()....
        else {
            holder.tvTimeStamp.setText("00:00");
            holder.tvTimeStamp.setTextColor(Color.parseColor("#000000"));
        }
        if (model.isCurrent()) {
            holder.rlMain.setCardBackgroundColor(Color.parseColor("#C4DF9C"));
        }
        holder.convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != myListener) {
                    myListener.OnItemClicked(v, position);
                }
                for (QuestionModel questionModel : questionModels)
                    questionModel.setCurrent(false);
                model.setCurrent(true);
                notifyDataSetChanged();
            }
        });

    }//onBindViewHolder()...

    @Override
    public int getItemCount() {
        return QuestionModel.questionList.size();
    }//getItemCount()


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        TextView tvQuestion;
        TextView tvAnswer;
        TextView tvTimeStamp;
        CardView rlMain;
        View convertView;


        public ViewHolder(View convertView) {
            super(convertView);
            this.convertView = convertView;
            tvQuestion = (TextView) convertView.findViewById(R.id.tvListQuestion);
            tvAnswer = (TextView) convertView.findViewById(R.id.tvAnswer);
            rlMain = (CardView) convertView.findViewById(R.id.main_list_cardView);
            tvTimeStamp = (TextView) convertView.findViewById(R.id.tvTimeStamp);

        }//ViewHolder()
    }//ViewHolder

    public interface OnItemClickedListener {
        public void OnItemClicked(View view, int postion);
    }//
}
