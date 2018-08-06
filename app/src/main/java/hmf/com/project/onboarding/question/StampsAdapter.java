package hmf.com.project.onboarding.question;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hmf.com.project.onboarding.R;
import hmf.com.project.onboarding.views.TimelineView;

/**
 * Created by saurabh.singh on 5/18/2016.
 */
public class StampsAdapter  extends RecyclerView.Adapter<StampsAdapter.ViewHolder>{


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timeline_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Stamps stamp =  Stamps.stampList.get(position);
        holder.tvStamp.setText(stamp.getStampText());
        holder.tm.setText(stamp.getNum());
    }

    @Override
    public int getItemCount() {
        return Stamps.stampList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case

        TimelineView tm;
        TextView tvStamp;
//        @Override
//        public void onClick(View v) {
//            if(null != myListener){
//                myListener.OnItemClicked(v , getPosition());
//            }//if()
//        }


        public ViewHolder(View convertView) {
            super(convertView);


            tvStamp = (TextView) convertView.findViewById(R.id.stamp);
            tm = (TimelineView) convertView.findViewById(R.id.stamps_marker);

        }//ViewHolder()
    }//ViewHolder
    public interface OnItemClickedListener{
        public void OnItemClicked(View view , int postion);
    }//
}
