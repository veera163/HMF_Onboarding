package hmf.com.project.onboarding;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;

import java.util.ArrayList;

/**
 * Created by home on 2/7/2018.
 */

public class Adpter extends BaseAdapter {
    Context context;
    ArrayList<String> arrayList;
    JsonArray elements;
    ArrayList<LatLng> coordList;


    public Adpter(Context context, ArrayList<String> arrayList, JsonArray elements, ArrayList<LatLng> coordList) {
        this.context = context;
        this.arrayList = arrayList;
        this.elements = elements;
        this.coordList = coordList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    public class Holder
    {

        TextView tv;
        ImageView cancle;
    }
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        Holder holder=new Holder();
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.locationlist, viewGroup, false);
        holder.tv=(TextView) itemView.findViewById(R.id.veera);
        holder.cancle=(ImageView) itemView.findViewById(R.id.cancle);
        holder.tv.setText(arrayList.get(i));
        holder.cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.remove(i);
                elements.remove(i);
                coordList.remove(i);
                Log.e("veera",arrayList.toString());
                Log.e("veera",elements.toString());
                notifyDataSetChanged();
            }
        });
        return itemView;
    }
}
