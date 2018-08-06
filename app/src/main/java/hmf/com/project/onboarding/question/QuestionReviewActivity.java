package hmf.com.project.onboarding.question;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import hmf.com.project.onboarding.ESurvey;
import hmf.com.project.onboarding.R;
import hmf.com.project.onboarding.details.PersonDetailsActivity;
import hmf.com.project.onboarding.logger.L;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class QuestionReviewActivity extends AppCompatActivity implements
        MediaPlayer.OnPreparedListener, View.OnClickListener, AdapterView.OnItemClickListener {
    private MediaPlayer mediaPlayer = null;
    private Button btnPlay = null;
    private ListView listQuestion;
    private QuestionAdapter adapter;
    private LayoutInflater inflater;
    private Dialog dialog;
    String answer = "";
    private View view;
    private TextView txtDuration;
    private TextView txtChronometer;
    private AppCompatSeekBar seekBar;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(this);
        listQuestion = (ListView) findViewById(R.id.listViewQuestion);
        txtChronometer = (TextView) findViewById(R.id.tv_chronometer);
        seekBar = (AppCompatSeekBar) findViewById(R.id.seek_track);
        seekBar.setClickable(false);
        txtDuration = (TextView) findViewById(R.id.tv_duration);
        try {
            adapter = new QuestionAdapter(this);
            listQuestion.setAdapter(adapter);
            listQuestion.setOnItemClickListener(this);
        } catch (IllegalArgumentException e) {
            L.e(e.getMessage() + "");
        }//catch()

    }//onCreate()...

    @Override
    protected void onResume() {
        super.onResume();
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Exception occurred :: " + e.getMessage(), Toast.LENGTH_LONG).show();
            L.e("Exception occurred :: " + e.getMessage());
        }//catch()

    }//onResume()

    private void init() throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(ESurvey.DIR_PATH + "/" + ESurvey.FILE_NAME);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.prepareAsync();
    }//

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (null == btnPlay) {
            return;
        }//
        if (null == mediaPlayer) {
            Toast.makeText(this, "Media Player not ready", Toast.LENGTH_SHORT).show();
            return;
        }//if()...

        mediaPlayer.start();
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();


        seekBar.setMax((int) finalTime);

        txtDuration.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
        );

        txtChronometer.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
        );

        seekBar.setProgress((int) startTime);
        myHandler.postDelayed(UpdateSongTime, 100);
//        btnPlay.setEnabled(true);
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            txtChronometer.setText(String.format("%d : %d",

                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekBar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onClick(View v) {

        if (v == btnPlay) {
            // Add the request to the RequestQueue.

            startActivity(new Intent(this, PersonDetailsActivity.class));
            //finish();
        }//

    }

    @Override
    protected void onPause() {
        super.onPause();
        myHandler.removeCallbacks(UpdateSongTime);
        if (null != mediaPlayer) {
            mediaPlayer.stop();
            mediaPlayer.release();

        }//if()
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null == QuestionModel.questionList || QuestionModel.questionList.size() == 0) {
            L.e("Error in question list");
            return;
        }
        QuestionModel model = QuestionModel.questionList.get(position);
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            L.d(model.getBookmark() + "");
            mediaPlayer.seekTo((int) model.getBookmark());
        } else {
            if (null != mediaPlayer) {
                mediaPlayer.start();
                mediaPlayer.seekTo((int) model.getBookmark());
            }//
            L.e("  errror");
        }

        launchDialog(model);
        L.d(model.getBookmark() + "");
    }

    private void launchDialog(final QuestionModel quest) {
        if (null == quest) {
            throw new IllegalArgumentException("Questions are not avilable");
        }//if(null ckeck)
        answer = "";
        int option = 0;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Response");

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.edit_question_layout, null, false);
        TextView tvQuestion = (TextView) view.findViewById(R.id.tvQuestion);

        final RadioButton rdOptA = (RadioButton) view.findViewById(R.id.rdOptionA);
        final RadioButton rdOptB = (RadioButton) view.findViewById(R.id.rdOptionB);
        final RadioButton rdOptC = (RadioButton) view.findViewById(R.id.rdOptionC);
        final RadioButton rdOptD = (RadioButton) view.findViewById(R.id.rdOptionD);
        final RadioButton rdOptE = (RadioButton) view.findViewById(R.id.rdOptionE);
        final EditText edtAns = (EditText) view.findViewById(R.id.edt_answer);
        RadioGroup rdAnsOption = (RadioGroup) view.findViewById(R.id.rdgAnswer);


        tvQuestion.setText(quest.getQuestion());
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
            rdOptE.setText(quest.getOptB());
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

                    quest.setAnswer(answer);
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

}
