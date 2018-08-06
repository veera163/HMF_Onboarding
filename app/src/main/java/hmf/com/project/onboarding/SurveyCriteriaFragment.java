package hmf.com.project.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hmf.com.project.onboarding.domains.SurveyCriteriaItem;
import com.google.gson.Gson;

/**
 * Created by sai on 03-06-2017.
 */

public class SurveyCriteriaFragment extends Fragment {
    public static final String DATA = "data";
    TextView tv_catg, tv_num_surveys_num, tv_cmptd_surveys_num, tv_pending_surveys_num, tv_hold_surveys_num, tv_cancelled_surveys_num;
    TextView tv_pending_today_num;
    Gson gson = new Gson();
    SurveyCriteriaItem surveyCriteriaItem;

    public static SurveyCriteriaFragment newInstance() {
        SurveyCriteriaFragment surveyCriteriaFragment = new SurveyCriteriaFragment();

        return surveyCriteriaFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surveyCriteriaItem = gson.fromJson(getArguments().getString(DATA), SurveyCriteriaItem.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.row_categories, container, false);
        initViewController(view);
        return view;
    }

    private void initViewController(View itemView) {
        tv_catg = (TextView) itemView.findViewById(R.id.tv_catg);
        tv_num_surveys_num = (TextView) itemView.findViewById(R.id.tv_num_surveys_num);
        tv_cmptd_surveys_num = (TextView) itemView.findViewById(R.id.tv_cmptd_surveys_num);
        tv_pending_surveys_num = (TextView) itemView.findViewById(R.id.tv_pending_surveys_num);
        tv_hold_surveys_num = (TextView) itemView.findViewById(R.id.tv_hold_surveys_num);
        tv_cancelled_surveys_num = (TextView) itemView.findViewById(R.id.tv_cancelled_surveys_num);
        tv_pending_today_num = (TextView) itemView.findViewById(R.id.tv_pending_today_num);
        if (surveyCriteriaItem.getCategory() != null)
            tv_catg.setText(surveyCriteriaItem.getCategory());

        if (surveyCriteriaItem.getNoOfSurveys() != null)
            tv_num_surveys_num.setText(surveyCriteriaItem.getNoOfSurveys());
        else
            tv_num_surveys_num.setText("0");

        if (surveyCriteriaItem.getNoOfCanceled() != null)
            tv_cancelled_surveys_num.setText(surveyCriteriaItem.getNoOfCanceled());
        else
            tv_cancelled_surveys_num.setText("0");

        if (surveyCriteriaItem.getNoOfCompleted() != null)
            tv_cmptd_surveys_num.setText(surveyCriteriaItem.getNoOfCompleted());
        else
            tv_cmptd_surveys_num.setText("0");

        if (surveyCriteriaItem.getNoOfOnHold() != null)
            tv_hold_surveys_num.setText(surveyCriteriaItem.getNoOfOnHold());
        else
            tv_hold_surveys_num.setText("0");

        if (surveyCriteriaItem.getNoOfPending() != null)
            tv_pending_surveys_num.setText(surveyCriteriaItem.getNoOfPending());
        else
            tv_pending_surveys_num.setText("0");
        if (surveyCriteriaItem.getPendingToday() != null)
            tv_pending_today_num.setText(surveyCriteriaItem.getPendingToday());
        else
            tv_pending_today_num.setText("0");

    }
}
