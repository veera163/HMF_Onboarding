package hmf.com.project.onboarding.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hmf.com.project.onboarding.R;
import hmf.com.project.onboarding.domains.StatusCountsItem;

import java.util.ArrayList;

/**
 * Created by sai on 31-05-2017.
 */

public class ProfilePlansAdapter extends RecyclerView.Adapter<ProfilePlansAdapter.ProfilePlansViewHolder> {

    ArrayList<StatusCountsItem> statusCountsItems;
    Context context;

    public ProfilePlansAdapter(ArrayList<StatusCountsItem> statusCountsItems) {
        this.statusCountsItems = statusCountsItems;
    }

    @Override
    public ProfilePlansViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        return new ProfilePlansViewHolder(layoutInflater.inflate(R.layout.row_plans, parent, false));
    }

    @Override
    public void onBindViewHolder(ProfilePlansViewHolder holder, int position) {
        StatusCountsItem statusCountsItem = statusCountsItems.get(position);
        holder.tv_count.setText(String.valueOf(statusCountsItem.getTotal()));
        holder.tv_status.setText(statusCountsItem.getStatus());
    }

    @Override
    public int getItemCount() {
        return statusCountsItems.size();
    }

    public class ProfilePlansViewHolder extends RecyclerView.ViewHolder {
        TextView tv_count, tv_status;

        public ProfilePlansViewHolder(View itemView) {
            super(itemView);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            tv_count = (TextView) itemView.findViewById(R.id.tv_count);
        }
    }
}
