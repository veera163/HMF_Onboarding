package hmf.com.project.onboarding.survey;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hmf.com.project.onboarding.R;
import hmf.com.project.onboarding.logger.L;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionFragment.OnQuestionFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment
        implements QuestionAdapter.OnItemClickedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView listQuestion;
    private QuestionAdapter questionAdapter;

    // TODO: Rename and change types of parameters

    private OnQuestionFragmentInteractionListener mListener;

    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(String param1, String param2) {
        QuestionFragment fragment = new QuestionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_question, container, false);
        listQuestion = (RecyclerView) view.findViewById(R.id.recyclerQuestion);

        listQuestion.setLayoutManager(new LinearLayoutManager(getContext()));
        listQuestion.addItemDecoration(new DividerItemDecoration(getContext()));
        try {
            questionAdapter = new QuestionAdapter(this);
            listQuestion.setAdapter(questionAdapter);
        } catch (Exception e) {
            L.e("QuestionFragment()::onCreateView" + e.getLocalizedMessage());
        }//

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {

        }
    }//onButtonPressed()...

    public void updateAdapter() {
        if (questionAdapter != null) {
            L.d("Setting adapter from Question fragment");
            questionAdapter.notifyDataSetChanged();
        } else {
            L.e("Adpter null");
        }
    }//

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnQuestionFragmentInteractionListener) {
            mListener = (OnQuestionFragmentInteractionListener) context;
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
    public void OnItemClicked(View view, int position) {
        if (mListener != null) {
            mListener.setSecondFragment(position);
        }//
    }//OnItemClicked()....

    public void clickOnNextQuestion(final int position) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                listQuestion.scrollToPosition(position);
                listQuestion.getLayoutManager().findViewByPosition(position).performClick();
            }
        });
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
    public interface OnQuestionFragmentInteractionListener {
        // TODO: Update argument type and name
        void setSecondFragment(int position);
    }//
}
