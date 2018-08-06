package hmf.com.project.onboarding.question;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import hmf.com.project.onboarding.R;
import hmf.com.project.onboarding.logger.L;
import hmf.com.project.onboarding.media.MediaRecorderSingleton;

import java.io.IOException;

public class SurveyAllActivity extends AppCompatActivity implements
        View.OnClickListener, AdapterView.OnItemClickListener {

    String answer;
    private Button btnPlay = null;
    private ListView listQuestion;
    private Chronometer chronometer;
    private QuestionAdapter adapter;
    private Dialog dialog;
    private MediaRecorder mediaRecorder;
    private int totalAnswered = 0;
    private TextView txtCount;
    private int totalQuestions = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_survey_all);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(this);

        listQuestion = (ListView) findViewById(R.id.listViewQuestion);
        txtCount = (TextView) findViewById(R.id.txt_count);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        try {
            adapter = new QuestionAdapter(this);
            listQuestion.setAdapter(adapter);
            totalQuestions = QuestionModel.questionList.size();
            listQuestion.setOnItemClickListener(this);
        } catch (IllegalArgumentException e) {
            L.e(e.getMessage() + "");
        }//catch()
        initRecorder();


    }//
    private void initRecorder() {
        try {
            mediaRecorder = MediaRecorderSingleton.getInstance().getRecorder();
            launchDialog();
        } catch (IOException e) {
            e.printStackTrace();
            L.e(e.getLocalizedMessage());
        }
    }//initRecorder()...

    private void launchDialog(final QuestionModel quest) {
        if (null == quest) {
            throw new IllegalArgumentException("Questions are not avilable");
        }//if(null ckeck)
        answer = "";
        int option = 0;
        final long mill = SystemClock.elapsedRealtime() - chronometer.getBase();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Response");

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.edit_question_layout, null, false);
        final TextView tvQuestion = (TextView) view.findViewById(R.id.tvQuestion);

        final RadioButton rdOptA = (RadioButton) view.findViewById(R.id.rdOptionA);
        final RadioButton rdOptB = (RadioButton) view.findViewById(R.id.rdOptionB);
        final RadioButton rdOptC = (RadioButton) view.findViewById(R.id.rdOptionC);
        final RadioButton rdOptD = (RadioButton) view.findViewById(R.id.rdOptionD);
        final RadioButton rdOptE = (RadioButton) view.findViewById(R.id.rdOptionE);
        final EditText edtAns = (EditText) view.findViewById(R.id.edt_answer);
        RadioGroup rdAnsOption = (RadioGroup) view.findViewById(R.id.rdgAnswer);


        tvQuestion.setText(quest.getQuestion());
        if (quest.getOptA().equals("NA") || quest.getOptA().equals("")) {
            rdOptA.setVisibility(View.GONE);


        } else {
            if (rdOptA.getVisibility() != View.VISIBLE) {
                rdOptA.setVisibility(View.VISIBLE);
            }
            rdOptA.setText(quest.getOptA());
            option++;

        }
        if (quest.getOptD().equals("NA") || quest.getOptD().equals("")) {
            rdOptD.setVisibility(View.GONE);

        } else {
            if (rdOptD.getVisibility() != View.VISIBLE) {
                rdOptD.setVisibility(View.VISIBLE);
            }
            option++;
            rdOptD.setText(quest.getOptD());
        }
        if (quest.getOptC().equals("NA") || quest.getOptC().equals("")) {
            rdOptC.setVisibility(View.GONE);

        } else {
            if (rdOptC.getVisibility() != View.VISIBLE) {
                rdOptC.setVisibility(View.VISIBLE);
            }
            option++;
            rdOptC.setText(quest.getOptC());
        }
        if (quest.getOptB().equals("NA") || quest.getOptB().equals("")) {
            rdOptB.setVisibility(View.GONE);

        } else {
            if (rdOptB.getVisibility() != View.VISIBLE) {
                rdOptB.setVisibility(View.VISIBLE);
            }
            option++;
            rdOptB.setText(quest.getOptB());
        }
        if (quest.getOptE().equals("NA") || quest.getOptE().equals("")) {
            rdOptE.setVisibility(View.GONE);

        } else {
            if (rdOptE.getVisibility() != View.VISIBLE) {
                rdOptE.setVisibility(View.VISIBLE);
            }
            option++;
            rdOptE.setText(quest.getOptE());
        }
        if (option == 0) {
            if (edtAns.getVisibility() != View.VISIBLE) {
                edtAns.setVisibility(View.VISIBLE);
            }//
        }//if()..
        else {
            if (edtAns.getVisibility() != View.INVISIBLE) {
                edtAns.setVisibility(View.INVISIBLE);
            }
            option++;
        }
        rdAnsOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.rdOptionA:
                        if (rdOptA.getText() != null)
                            answer = rdOptA.getText().toString();
                        L.d("Option selected from dialog boxes " + answer);
                        break;
                    case R.id.rdOptionB:
                        if (rdOptB.getText() != null)
                            answer = rdOptB.getText().toString();
                        L.d("Option selected from dialog boxes " + answer);
                        break;
                    case R.id.rdOptionC:
                        if (rdOptC.getText() != null)
                            answer = rdOptC.getText().toString();
                        L.d("Option selected from dialog boxes " + answer);
                        break;
                    case R.id.rdOptionD:
                        if (rdOptD.getText() != null)
                            answer = rdOptD.getText().toString();
                        L.d("Option selected from dialog boxes " + answer);
                        break;
                    case R.id.rdOptionE:
                        if (rdOptE.getText() != null)
                            answer = rdOptE.getText().toString();
                        L.d("Option selected from dialog boxes " + answer);
                        break;
                }//switch()
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null != dialog)
                    dialog.dismiss();
            }
        });
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (edtAns.getVisibility() == View.VISIBLE) {
                    answer = edtAns.getText().toString();
                }//if()...
                if (answer.equals("")) {
                    L.e("unchanged");
                    return;
                }//
                else {
                    if(quest.getAnswer() == null || quest.getAnswer().equals("")){
                       totalAnswered++;
                        txtCount.setText("Total question answered is "+totalAnswered +" out of "+totalQuestions);
                        L.d("total question answered are "+ totalAnswered);
                    }//
                    quest.setAnswer(answer);
                    quest.setBookmark(mill);
                    if (null != adapter) {
                        L.d("Logging answer :: " + answer);
                        L.d("Notified " + quest.getAnswer());
                        adapter.notifyDataSetChanged();
                    }//if()...
                    if (null != dialog)
                        dialog.dismiss();
                }//else
            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();


    }//


    @Override
    public void onClick(View v) {
        if (v == btnPlay) {
            launchSubmitAlertDialog(totalAnswered);
        }//

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null == QuestionModel.questionList || QuestionModel.questionList.size() == 0) {
            L.e("Error in question list");
            return;
        }
        QuestionModel model = QuestionModel.questionList.get(position);

        launchDialog(model);

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
    private void launchSubmitAlertDialog(int totalAnswered) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to submit. You  answered " + totalAnswered +" questions");
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(SurveyAllActivity.this, QuestionReviewActivity.class));
                finish();
            }
        });
        builder.create().show();
    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            MediaRecorderSingleton.getInstance().destroyObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//
}
