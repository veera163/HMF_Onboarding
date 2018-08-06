package hmf.com.project.onboarding.question;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import hmf.com.project.onboarding.R;
import hmf.com.project.onboarding.logger.L;
import hmf.com.project.onboarding.media.MediaRecorderSingleton;

import java.io.IOException;

public class SurveyActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvQuestion;
    //private TextView tvOptA;
//    private TextView tvOptB;
//    private TextView tvOptC;
//    private TextView tvOptD;
    private RadioButton rdOptA, rdOptB, rdOptC, rdOptD;
    private RadioGroup rdAnsOption;
    private Button btnPausePlay, btnNext;
    private int questionCount = 0;
    private MediaRecorder mediaRecorder;
    private Chronometer chronometer;
    private EditText edtAnswer;
    private QuestionModel quest;
    long mill = 0;
    private RadioButton rdOptE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_actvitiy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
//        tvOptA      =   (TextView)findViewById(R.id.tvOptionA);
//        tvOptB      =   (TextView)findViewById(R.id.tvOptionB);
//        tvOptC      =   (TextView)findViewById(R.id.tvOptionC);
//        tvOptD      =   (TextView)findViewById(R.id.tvOptionD);
        rdOptA = (RadioButton) findViewById(R.id.rdOptionA);
        rdOptB = (RadioButton) findViewById(R.id.rdOptionB);
        rdOptC = (RadioButton) findViewById(R.id.rdOptionC);
        rdOptD = (RadioButton) findViewById(R.id.rdOptionD);
        rdOptE = (RadioButton) findViewById(R.id.rdOptionE);
        rdAnsOption = (RadioGroup) findViewById(R.id.rdgAnswer);
        edtAnswer = (EditText) findViewById(R.id.edtResponse);

        btnNext = (Button) findViewById(R.id.btnNext);
        btnPausePlay = (Button) findViewById(R.id.btnPause);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        btnPausePlay.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        initRecorder();

        rdAnsOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.rdOptionA:
                        if (rdOptA.getText() != null)
                            edtAnswer.setText(rdOptA.getText() + "");
                        break;
                    case R.id.rdOptionB:
                        if (rdOptB.getText() != null)
                            edtAnswer.setText(rdOptB.getText() + "");
                        break;
                    case R.id.rdOptionC:
                        if (rdOptC.getText() != null)
                            edtAnswer.setText(rdOptC.getText() + "");
                        break;
                    case R.id.rdOptionD:
                        if (rdOptD.getText() != null)
                            edtAnswer.setText(rdOptD.getText() + "");
                        break;
                    case R.id.rdOptionE:
                        if (rdOptE.getText() != null)
                            edtAnswer.setText(rdOptE.getText() + "");
                        break;
                }//switch()
            }
        });
    }//onCreate()

    @Override
    protected void onResume() {
        super.onResume();
        try {
            updateQuestion();
        } catch (Exception e) {
            L.e("Error " + e.getMessage());
        }
    }

    private void initRecorder() {
        try {
            mediaRecorder = MediaRecorderSingleton.getInstance().getRecorder();
            launchDialog();
        } catch (IOException e) {
            e.printStackTrace();
            L.e(e.getLocalizedMessage());
        }
    }//initRecorder()...

    @Override
    public void onClick(View v) {
        if (v == btnNext) {

            try {
                updateModel(quest);
            } catch (IllegalArgumentException e) {
                L.e(e.getLocalizedMessage() + "");
            }//
            updateQuestion();


        }//if()
        if (v == btnPausePlay) {


        }//

    }//onClick()

    private void updateModel(QuestionModel model) {
        if (null == model) {
            throw new IllegalArgumentException("Null model found.");
        }//
        if (!edtAnswer.getText().toString().equals("")) {

//            L.d(SystemClock.elapsedRealtime() + "");
//            L.d(mill + "");
            model.setAnswer(edtAnswer.getText().toString());


            L.d("Reset Fields");
            rdAnsOption.clearCheck();
            edtAnswer.setText("");

        }//
        model.setBookmark(mill);
        mill = SystemClock.elapsedRealtime() - chronometer.getBase();

    }//UpdateModel()

    private void updateQuestion() {
        if (null == QuestionModel.questionList && QuestionModel.questionList.size() != 0) {
            throw new IllegalArgumentException("Questions are not avilable");
        }//if(null ckeck)
        L.d("Count is " + questionCount);
        if (questionCount == (QuestionModel.questionList.size())) {
            btnNext.setText("Done");
            questionCount++;
            return;
        }
        if (questionCount == QuestionModel.questionList.size() + 1) {
//            mediaRecorder.stop();
            chronometer.stop();
            L.d(chronometer.getText().toString());
            startActivity(new Intent(this, QuestionReviewActivity.class));
            finish();
            return;
        }//
        quest = QuestionModel.questionList.get(questionCount);

        tvQuestion.setText(quest.getQuestion());
        if(quest.getOptA().equals("NA") || quest.getOptA().equals("")){
            rdOptA.setVisibility(View.GONE);
        }
        else{
            if(rdOptA.getVisibility()!= View.VISIBLE){
                rdOptA.setVisibility(View.VISIBLE);
            }
            rdOptA.setText(quest.getOptA());
        }
        if(quest.getOptD().equals("NA") || quest.getOptD().equals("")){
            rdOptD.setVisibility(View.GONE);
        }
        else{
            if(rdOptD.getVisibility()!= View.VISIBLE){
                rdOptD.setVisibility(View.VISIBLE);
            }
            rdOptD.setText(quest.getOptD());
        }
        if(quest.getOptC().equals("NA") || quest.getOptC().equals("")){
            rdOptC.setVisibility(View.GONE);
        }
        else{
            if(rdOptC.getVisibility()!= View.VISIBLE){
                rdOptC.setVisibility(View.VISIBLE);
            }
            rdOptC.setText(quest.getOptC());
        }
        if(quest.getOptB().equals("NA")|| quest.getOptB().equals("")){
            rdOptB.setVisibility(View.GONE);
        }
        else{
            if(rdOptB.getVisibility()!= View.VISIBLE){
                rdOptB.setVisibility(View.VISIBLE);
            }
            rdOptB.setText(quest.getOptB());
        }
        if(quest.getOptE().equals("NA")|| quest.getOptE().equals("")){
            rdOptE.setVisibility(View.GONE);
        }
        else{
            if(rdOptE.getVisibility()!= View.VISIBLE){
                rdOptE.setVisibility(View.VISIBLE);
            }
            rdOptE.setText(quest.getOptE());
        }




        questionCount++;


    }//updateQuestion()

    @Override
    protected void onPause() {
        super.onPause();
        try {
            MediaRecorderSingleton.getInstance().destroyObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//

    private void launchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This will start the Recording. You need to read the questions. " +
                "There are " + QuestionModel.questionList.size() + " questions.");
        builder.setTitle("Alert");
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mediaRecorder.start();
                chronometer.start();

            }
        });
        builder.create().show();
    }

}
