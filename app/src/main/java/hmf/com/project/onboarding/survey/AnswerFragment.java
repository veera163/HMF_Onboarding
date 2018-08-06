package hmf.com.project.onboarding.survey;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import hmf.com.project.onboarding.R;
import hmf.com.project.onboarding.logger.L;
import hmf.com.project.onboarding.question.QuestionModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AnswerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AnswerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnswerFragment extends Fragment
        implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView tvQuestion;
    private QuestionModel quest;

    private OnFragmentInteractionListener mListener;
    private RadioButton rdOptA, rdOptB, rdOptC, rdOptD, rdOptE, rdOptF, rdOptG, rdOptH, rdOptI;
    private CheckBox chkOptA, chkOptB, chkOptC, chkOptD, chkOptE, chkOptF, chkOptG, chkOptH, chkOptI;
    private EditText edtAns;
    private TextView tv_done;
    private RadioGroup RrdAnsOption;
    private GridLayout grdCheckBoxes;
    private String answer = "";
    private Button btnMark, btnSubmit;
    private int position = -1;

    public AnswerFragment() {
        // Required empty public constructor
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnswerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnswerFragment newInstance(String param1, String param2) {
        AnswerFragment fragment = new AnswerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_answer, container, false);
        tvQuestion = (TextView) view.findViewById(R.id.tvQuestion);
        rdOptA = (RadioButton) view.findViewById(R.id.rdOptionA);
        rdOptB = (RadioButton) view.findViewById(R.id.rdOptionB);
        rdOptC = (RadioButton) view.findViewById(R.id.rdOptionC);
        rdOptD = (RadioButton) view.findViewById(R.id.rdOptionD);
        rdOptE = (RadioButton) view.findViewById(R.id.rdOptionE);
        rdOptF = (RadioButton) view.findViewById(R.id.rdOptionF);
        rdOptG = (RadioButton) view.findViewById(R.id.rdOptionG);
        rdOptH = (RadioButton) view.findViewById(R.id.rdOptionH);
        rdOptI = (RadioButton) view.findViewById(R.id.rdOptionI);
        edtAns = (EditText) view.findViewById(R.id.edt_answer);
        tv_done = (TextView) view.findViewById(R.id.tv_done);
        RrdAnsOption = (RadioGroup) view.findViewById(R.id.rdgAnswer);
        chkOptA = (CheckBox) view.findViewById(R.id.chk_optA);
        chkOptB = (CheckBox) view.findViewById(R.id.chk_optB);
        chkOptC = (CheckBox) view.findViewById(R.id.chk_optC);
        chkOptD = (CheckBox) view.findViewById(R.id.chk_optD);
        chkOptE = (CheckBox) view.findViewById(R.id.chk_optE);
        chkOptF = (CheckBox) view.findViewById(R.id.chk_optF);
        chkOptG = (CheckBox) view.findViewById(R.id.chk_optG);
        chkOptH = (CheckBox) view.findViewById(R.id.chk_optH);
        chkOptI = (CheckBox) view.findViewById(R.id.chk_optI);
        grdCheckBoxes = (GridLayout) view.findViewById(R.id.grdChk);
        RrdAnsOption.setOnCheckedChangeListener(this);
        btnMark = (Button) view.findViewById(R.id.btnMark);
        btnMark.setVisibility(View.GONE);
        btnMark.setOnClickListener(this);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        /*chkOptA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    quest.setAnswer( quest.getAnswer() + "," + chkOptA.getText().toString());
                }
            }
        });
*/
        rdOptA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    answer = rdOptA.getText().toString();
                    quest.setAnswer(answer);
                    //if (null != mListener)
                        //mListener.onMarked(true);
                } else {
                    answer = "";
                }
            }
        });
        rdOptB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    answer = rdOptB.getText().toString();
                    quest.setAnswer(answer);
                    //if (null != mListener)
                        //mListener.onMarked(true);
                } else {
                    answer = "";
                }
            }
        });
        rdOptC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    answer = rdOptC.getText().toString();
                    quest.setAnswer(answer);
                    //if (null != mListener)
                        //mListener.onMarked(true);
                } else {
                    answer = "";
                }
            }
        });
        rdOptD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    answer = rdOptD.getText().toString();
                    quest.setAnswer(answer);
                    //if (null != mListener)
                        //mListener.onMarked(true);
                } else {
                    answer = "";
                }
            }
        });
        rdOptE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    answer = rdOptE.getText().toString();
                    quest.setAnswer(answer);
                    //if (null != mListener)
                        //mListener.onMarked(true);
                } else {
                    answer = "";
                }
            }
        });
        rdOptF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    answer = rdOptF.getText().toString();
                    quest.setAnswer(answer);
                    //if (null != mListener)
                        //mListener.onMarked(true);
                } else {
                    answer = "";
                }
            }
        });
        rdOptG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    answer = rdOptG.getText().toString();
                    quest.setAnswer(answer);
                    //if (null != mListener)
                        //mListener.onMarked(true);
                } else {
                    answer = "";
                }
            }
        });
        rdOptH.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    answer = rdOptH.getText().toString();
                    quest.setAnswer(answer);
                    //if (null != mListener)
                        //mListener.onMarked(true);
                } else {
                    answer = "";
                }
            }
        });
        rdOptI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    answer = rdOptI.getText().toString();
                    quest.setAnswer(answer);
                    //if (null != mListener)
                        //mListener.onMarked(true);
                } else {
                    answer = "";
                }

            }
        });
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (grdCheckBoxes.getVisibility() == View.VISIBLE) {
                    L.d("setting the question " + 1.2);
                    if (chkOptA.isChecked()) {
                        answer += chkOptA.getText() + ",";
                    }
                    if (chkOptB.isChecked()) {
                        answer += chkOptB.getText() + ",";
                    }
                    if (chkOptC.isChecked()) {
                        answer += chkOptC.getText() + ",";
                    }
                    if (chkOptD.isChecked()) {
                        answer += chkOptD.getText() + ",";
                    }
                    if (chkOptE.isChecked()) {
                        answer += chkOptE.getText() + "";
                    }
                    if (chkOptF.isChecked()) {
                        answer += chkOptF.getText() + "";
                    }
                    if (chkOptG.isChecked()) {
                        answer += chkOptG.getText() + "";
                    }
                    if (chkOptH.isChecked()) {
                        answer += chkOptH.getText() + "";
                    }
                    if (chkOptI.isChecked()) {
                        answer += chkOptI.getText() + "";
                    }
                    quest.setAnswer(answer);
                    //if (null != mListener)
                        //mListener.onMarked(true);

                    //
                }
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {

        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (position != -1) {
            updateFragment(position);
        }//
    }

    public void updateFragment(int position) {
        reset();
        if (QuestionModel.questionList.size() == 0) {
            throw new IllegalArgumentException("No question available");
        }//
        quest = QuestionModel.questionList.get(position);
        if (quest == null) {
            throw new NullPointerException("Question not present ");

        }//
        L.d("Updating the Question " + position);
        int option = 0;
        tvQuestion.setText(quest.getQuestion());
        if (!quest.isMultiple()) {
            L.d("Setting the Radio group..");
            grdCheckBoxes.setVisibility(View.GONE);
            RrdAnsOption.setVisibility(View.VISIBLE);
            if (quest.getOptA().equals("NA") || quest.getOptA().equals("")) {
                rdOptA.setVisibility(View.GONE);
                L.d("Option A Not found");

            } else {
                if (rdOptA.getVisibility() != View.VISIBLE) {
                    rdOptA.setVisibility(View.VISIBLE);
                }
                rdOptA.setText(quest.getOptA());
                option++;

            }
            if (quest.getOptD().equals("NA") || quest.getOptD().equals("")) {
                rdOptD.setVisibility(View.GONE);
                L.d("Option B Not found");
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
            if (quest.getOptF().equals("NA") || quest.getOptF().equals("")) {
                rdOptF.setVisibility(View.GONE);

            } else {
                if (rdOptF.getVisibility() != View.VISIBLE) {
                    rdOptF.setVisibility(View.VISIBLE);
                }
                option++;
                rdOptF.setText(quest.getOptF());
            }
            if (quest.getOptG().equals("NA") || quest.getOptG().equals("")) {
                rdOptG.setVisibility(View.GONE);

            } else {
                if (rdOptG.getVisibility() != View.VISIBLE) {
                    rdOptG.setVisibility(View.VISIBLE);
                }
                option++;
                rdOptG.setText(quest.getOptG());
            }
            if (quest.getOptH().equals("NA") || quest.getOptH().equals("")) {
                rdOptH.setVisibility(View.GONE);

            } else {
                if (rdOptH.getVisibility() != View.VISIBLE) {
                    rdOptH.setVisibility(View.VISIBLE);
                }
                option++;
                rdOptH.setText(quest.getOptH());
            }
            if (quest.getOptI().equals("NA") || quest.getOptI().equals("")) {
                rdOptI.setVisibility(View.GONE);

            } else {
                if (rdOptI.getVisibility() != View.VISIBLE) {
                    rdOptI.setVisibility(View.VISIBLE);
                }
                option++;
                rdOptI.setText(quest.getOptI());
            }
            if (option == 0) {
                RrdAnsOption.setVisibility(View.GONE);
                if (edtAns.getVisibility() != View.VISIBLE) {
                    edtAns.setVisibility(View.VISIBLE);

                }//
            }//if()..
            else {
                if (edtAns.getVisibility() != View.INVISIBLE) {
                    edtAns.setVisibility(View.INVISIBLE);
                }
                option++;
            }//else
        }//
        else {
            RrdAnsOption.setVisibility(View.GONE);
            grdCheckBoxes.setVisibility(View.VISIBLE);
            if (quest.getOptA().equals("NA") || quest.getOptA().equals("")) {
                chkOptA.setVisibility(View.GONE);

            } else {
                if (chkOptA.getVisibility() != View.VISIBLE) {
                    chkOptA.setVisibility(View.VISIBLE);
                }
                chkOptA.setText(quest.getOptA());
                option++;

            }
            if (quest.getOptD().equals("NA") || quest.getOptD().equals("")) {
                chkOptD.setVisibility(View.GONE);
                L.d("Fragment::--->" + quest.getOptD());

            } else {
                if (chkOptD.getVisibility() != View.VISIBLE) {
                    chkOptD.setVisibility(View.VISIBLE);
                }
                option++;
                chkOptD.setText(quest.getOptD());
            }
            if (quest.getOptC().equals("NA") || quest.getOptC().equals("")) {
                chkOptC.setVisibility(View.GONE);

            } else {
                if (chkOptC.getVisibility() != View.VISIBLE) {
                    chkOptC.setVisibility(View.VISIBLE);
                }
                option++;
                chkOptC.setText(quest.getOptC());
            }
            if (quest.getOptB().equals("NA") || quest.getOptB().equals("")) {
                chkOptB.setVisibility(View.GONE);

            } else {
                if (chkOptB.getVisibility() != View.VISIBLE) {
                    chkOptB.setVisibility(View.VISIBLE);
                }
                option++;
                chkOptB.setText(quest.getOptB());
            }
            if (quest.getOptE().equals("NA") || quest.getOptE().equals("")) {
                chkOptE.setVisibility(View.GONE);

            } else {
                if (chkOptE.getVisibility() != View.VISIBLE) {
                    chkOptE.setVisibility(View.VISIBLE);
                }
                option++;
                chkOptE.setText(quest.getOptE());
            }

            if (quest.getOptF().equals("NA") || quest.getOptF().equals("")) {
                chkOptF.setVisibility(View.GONE);

            } else {
                if (chkOptF.getVisibility() != View.VISIBLE) {
                    chkOptF.setVisibility(View.VISIBLE);
                }
                option++;
                chkOptF.setText(quest.getOptF());
            }
            if (quest.getOptG().equals("NA") || quest.getOptG().equals("")) {
                chkOptG.setVisibility(View.GONE);

            } else {
                if (chkOptG.getVisibility() != View.VISIBLE) {
                    chkOptG.setVisibility(View.VISIBLE);
                }
                option++;
                chkOptG.setText(quest.getOptG());
            }
            if (quest.getOptH().equals("NA") || quest.getOptH().equals("")) {
                chkOptH.setVisibility(View.GONE);

            } else {
                if (chkOptH.getVisibility() != View.VISIBLE) {
                    chkOptH.setVisibility(View.VISIBLE);
                }
                option++;
                chkOptH.setText(quest.getOptH());
            }
            if (quest.getOptI().equals("NA") || quest.getOptI().equals("")) {
                chkOptI.setVisibility(View.GONE);

            } else {
                if (chkOptI.getVisibility() != View.VISIBLE) {
                    chkOptI.setVisibility(View.VISIBLE);
                }
                option++;
                chkOptI.setText(quest.getOptI());
            }
            if (option == 0) {
                grdCheckBoxes.setVisibility(View.GONE);
                if (edtAns.getVisibility() != View.VISIBLE) {
                    edtAns.setVisibility(View.VISIBLE);

                }//
            }//if()..
            else {
                if (edtAns.getVisibility() != View.INVISIBLE) {
                    edtAns.setVisibility(View.INVISIBLE);
                }

            }//else
        }//else


    }//


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

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
            case R.id.rdOptionF:
                if (rdOptF.getText() != null)
                    answer = rdOptF.getText().toString();
                L.d("Option selected from dialog boxes " + answer);
                break;
            case R.id.rdOptionG:
                if (rdOptG.getText() != null)
                    answer = rdOptG.getText().toString();
                L.d("Option selected from dialog boxes " + answer);
                break;
            case R.id.rdOptionH:
                if (rdOptH.getText() != null)
                    answer = rdOptH.getText().toString();
                L.d("Option selected from dialog boxes " + answer);
                break;
            case R.id.rdOptionI:
                if (rdOptI.getText() != null)
                    answer = rdOptI.getText().toString();
                L.d("Option selected from dialog boxes " + answer);
                break;
        }//switch()
        //onClick(btnMark);

    }

    @Override
    public void onClick(View v) {
        if (v == btnMark) {
            if (quest == null) {
                L.e("null question");
                return;
            }//
            L.d("setting the question 1.1");
            if (RrdAnsOption.getVisibility() == View.VISIBLE) {
                L.d("setting the question 1.1.1");
            }//
            else if (grdCheckBoxes.getVisibility() == View.VISIBLE) {
                L.d("setting the question " + 1.2);
                if (chkOptA.isChecked()) {
                    answer += chkOptA.getText() + ",";
                }
                if (chkOptB.isChecked()) {
                    answer += chkOptB.getText() + ",";
                }
                if (chkOptC.isChecked()) {
                    answer += chkOptC.getText() + ",";
                }
                if (chkOptD.isChecked()) {
                    answer += chkOptD.getText() + ",";
                }
                if (chkOptE.isChecked()) {
                    answer += chkOptE.getText() + "";
                }
                if (chkOptF.isChecked()) {
                    answer += chkOptF.getText() + "";
                }
                if (chkOptG.isChecked()) {
                    answer += chkOptG.getText() + "";
                }
                if (chkOptH.isChecked()) {
                    answer += chkOptH.getText() + "";
                }
                if (chkOptI.isChecked()) {
                    answer += chkOptI.getText() + "";
                }
                //
            }//else if()...
            else {
                L.d("setting the question 1.3");

                answer = edtAns.getText().toString();

            }
            L.d("setting the question");
            quest.setAnswer(answer);
            //if (null != mListener)
                //mListener.onMarked(true);
            reset();
            // }//if()...
        }//if()...
        if (v == btnSubmit) {
            if (mListener != null) {
                mListener.enterPersonlaDetails();
            }
            //if()...
        }//if()...
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (quest == null) {
            L.e("null question");
            return;
        }//
        L.d("setting the question 1.1");
        if (RrdAnsOption.getVisibility() == View.VISIBLE) {
            L.d("setting the question 1.1.1");
        }//
        else if (grdCheckBoxes.getVisibility() == View.VISIBLE) {
            L.d("setting the question " + 1.2);
            if (chkOptA.isChecked()) {
                answer += chkOptA.getText() + ",";
            }
            if (chkOptB.isChecked()) {
                answer += chkOptB.getText() + ",";
            }
            if (chkOptC.isChecked()) {
                answer += chkOptC.getText() + ",";
            }
            if (chkOptD.isChecked()) {
                answer += chkOptD.getText() + ",";
            }
            if (chkOptE.isChecked()) {
                answer += chkOptE.getText() + "";
            }
            if (chkOptF.isChecked()) {
                answer += chkOptF.getText() + "";
            }
            if (chkOptG.isChecked()) {
                answer += chkOptG.getText() + "";
            }
            if (chkOptH.isChecked()) {
                answer += chkOptH.getText() + "";
            }
            if (chkOptI.isChecked()) {
                answer += chkOptI.getText() + "";
            }
            //
        }//else if()...
        else {
            L.d("setting the question 1.3");

            answer = edtAns.getText().toString();

        }
        L.d("setting the question");
        quest.setAnswer(answer);
        //if (null != mListener)
            //mListener.onMarked(false);
        reset();
        //onClick(btnMark);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onSubmit();

        public void onMarked(boolean marked);

        void enterPersonlaDetails();
    }//

    public void reset() {
        edtAns.setText("");
        tvQuestion.setText("");
        grdCheckBoxes.setVisibility(View.GONE);
        RrdAnsOption.clearCheck();
        RrdAnsOption.setVisibility(View.GONE);
        chkOptA.setChecked(false);
        chkOptB.setChecked(false);
        chkOptC.setChecked(false);
        chkOptD.setChecked(false);
        chkOptE.setChecked(false);
        chkOptF.setChecked(false);
        chkOptG.setChecked(false);
        chkOptH.setChecked(false);
        chkOptI.setChecked(false);
        answer = "";
    }//
}
