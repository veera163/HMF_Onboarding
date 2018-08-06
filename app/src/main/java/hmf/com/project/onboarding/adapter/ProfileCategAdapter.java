package hmf.com.project.onboarding.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import hmf.com.project.onboarding.SurveyCriteriaFragment;
import hmf.com.project.onboarding.domains.SurveyCriteriaItem;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by sai on 31-05-2017.
 */

public class ProfileCategAdapter extends FragmentStatePagerAdapter {

    ArrayList<SurveyCriteriaItem> statusCountsItems;
    Gson gson = new Gson();

    public ProfileCategAdapter(FragmentManager supportFragmentManager, ArrayList<SurveyCriteriaItem> statusCountsItems) {
        super(supportFragmentManager);
        this.statusCountsItems = statusCountsItems;
    }

    @Override
    public Fragment getItem(int position) {
        SurveyCriteriaItem surveyCriteriaItem = statusCountsItems.get(position);
        SurveyCriteriaFragment surveyCriteriaFragment = SurveyCriteriaFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString(SurveyCriteriaFragment.DATA, gson.toJson(surveyCriteriaItem));
        surveyCriteriaFragment.setArguments(bundle);
        return surveyCriteriaFragment;
    }

    @Override
    public int getCount() {
        return statusCountsItems.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
