package hmf.com.project.onboarding.details;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import hmf.com.project.onboarding.GpsTracker;
import hmf.com.project.onboarding.R;
import hmf.com.project.onboarding.logger.L;
import hmf.com.project.onboarding.question.QuestionModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PersonDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PersonDetailsFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener  {
    private Button btnReset, btnSubmit;
    public EditText ed_name,ed_age,ed_phone,ed_loanamt,ed_serveynum,ed_passbok,ed_adhar,ed_village,
            ed_mandal,ed_dist,ed_state,ed_pin,ed_landarea,ed_landtype,ed_crophistory,ed_insurenceperson,ed_reason,
            ed_whours,ed_mhours,ed_profitnegotiation,ed_cropRunningInfo,ed_cropVariety;

    LinearLayout hmfworkerlayout,hmfownerlayout;
    Spinner gender,farmartype,loantype,landmer,insurencetype,Disastertype,irrigationtype,boardedtype;
    String name,age,phone,loanamt,serveynum,passbooknum,adhar,village,mandal,dist,state,pin,landarea,landtype,crophistory,insurenceperson,reason,
            workhours,monitoryhours,profitneg,fgender,ftype,lontype,lanmer,instype,distype,irrigation,boarded,cropRunningInfo,cropVariety;
    private OnFragmentInteractionListener mListener;
    Double lat,lng;
    GpsTracker gps;
    String add,city,lstate,postalcode, knownName;
    Geocoder geocoder;
    List<Address> addresses;
    public PersonDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.personaldetails, container, false);
        gps=new GpsTracker(getContext());
        if(gps.canGetLocation()){
            lat=gps.getLatitude();
            lng=gps.getLongitude();
            geocoder = new Geocoder(getContext(), Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                add = addresses.get(0).getSubLocality(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(0).getLocality();
                lstate = addresses.get(0).getAdminArea();
                 postalcode = addresses.get(0).getPostalCode();
                 knownName = addresses.get(0).getFeatureName();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        hmfworkerlayout=(LinearLayout)view.findViewById(R.id.hmfworkerlayout);
        hmfownerlayout=(LinearLayout)view.findViewById(R.id.hmfownerlayout);

        ed_name = (EditText) view.findViewById(R.id.fname);

        //txtInputCast = (TextInputLayout) view.findViewById(R.id.textLayoutCaste);
        ed_age = (EditText) view.findViewById(R.id.fage);
        ed_phone = (EditText) view.findViewById(R.id.fphone);
        ed_loanamt = (EditText) view.findViewById(R.id.loanamt);
        ed_loanamt.setText("0");
        ed_serveynum = (EditText) view.findViewById(R.id.servernum);
        ed_passbok = (EditText) view.findViewById(R.id.fpassnum);
        ed_adhar = (EditText) view.findViewById(R.id.fadharnum);
        ed_village = (EditText) view.findViewById(R.id.fvaillage);
        ed_village.setText(knownName);
        ed_mandal = (EditText) view.findViewById(R.id.fmandel);
        ed_mandal.setText(add);
        ed_dist = (EditText) view.findViewById(R.id.fdist);
        ed_dist.setText(city);
        ed_state = (EditText) view.findViewById(R.id.fstate);
        ed_state.setText(lstate);
        ed_pin = (EditText) view.findViewById(R.id.fpin);
        ed_pin.setText(postalcode);
        ed_landarea = (EditText) view.findViewById(R.id.landarea);
        ed_landtype = (EditText) view.findViewById(R.id.landtype);
        ed_crophistory = (EditText) view.findViewById(R.id.crophistory);
        ed_cropRunningInfo=(EditText)view.findViewById(R.id.cropRunningInfo);
        ed_cropVariety=(EditText)view.findViewById(R.id.cropVariety);
        ed_insurenceperson = (EditText) view.findViewById(R.id.insurenceperson);
        ed_reason = (EditText) view.findViewById(R.id.reason);
        ed_whours = (EditText) view.findViewById(R.id.whours);
        ed_mhours = (EditText) view.findViewById(R.id.mhours);
        ed_profitnegotiation = (EditText) view.findViewById(R.id.profitnegotiation);

        gender=(Spinner)view.findViewById(R.id.fgender);
        gender.setOnItemSelectedListener(this);

        farmartype=(Spinner)view.findViewById(R.id.ftype);
        farmartype.setOnItemSelectedListener(this);

        loantype=(Spinner)view.findViewById(R.id.loantype);
        loantype.setOnItemSelectedListener(this);

        landmer=(Spinner)view.findViewById(R.id.landmer);
        landmer.setOnItemSelectedListener(this);

        insurencetype=(Spinner)view.findViewById(R.id.insurencetype);
        insurencetype.setOnItemSelectedListener(this);

        Disastertype=(Spinner)view.findViewById(R.id.Disastertype);
        Disastertype.setOnItemSelectedListener(this);

        irrigationtype=(Spinner)view.findViewById(R.id.irrigationtype);
        irrigationtype.setOnItemSelectedListener(this);
        boardedtype=(Spinner)view.findViewById(R.id.boardedtype);
        boardedtype.setOnItemSelectedListener(this);


        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnReset = (Button) view.findViewById(R.id.btnReset);
        btnSubmit.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onSubmit();
        }//if()...
    }

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
    public void onClick(View v) {
        if (v == btnReset) {
           // mListener.gotoBack();
            ed_name.setText("");
            //txtInputCast.getEditText().setText("");
            ed_age.setText("");
            ed_phone.setText("");
            ed_loanamt.setText("");
            ed_serveynum.setText("");

            ed_passbok.setText("");

            // String txtInputWardName.getEditText().getText().toString();;
            ed_adhar.setText("");
            ed_village.setText("");
            ed_mandal.setText("");
            ed_dist.setText("");
            ed_state.setText("");
            ed_pin.setText("");
            ed_landarea.setText("");
            ed_landtype.setText("");
            ed_crophistory.setText("");
            ed_cropRunningInfo.setText("");
            ed_cropVariety.setText("");
            ed_insurenceperson.setText("");
            ed_reason.setText("");
            ed_whours.setText("");
            ed_mhours.setText("");
            ed_profitnegotiation.setText("");

            gender.setSelection(0);
            farmartype.setSelection(0);
            loantype.setSelection(0);
            landmer.setSelection(0);
            insurencetype.setSelection(0);
            Disastertype.setSelection(0);
            irrigationtype.setSelection(0);
            boardedtype.setSelection(0);

            /*txtInputName.getEditText().setText("");
            txtInputCast.getEditText().setText("");
            txtInputAge.getEditText().setText("");
            txtInputNumOfChildren.getEditText().setText("");
            txtInputWardName.getEditText().setText("");
            txtInputWardName.getEditText().toString();
            txtInputCorpName.getEditText().setText("");

            txtInputWardNum.getEditText().setText("");

            // String txtInputWardName.getEditText().getText().toString();;
            txtInputReligion.getEditText().setText("");

            txtInputSubcaste.getEditText().setText("");

            txtInputProfession.getEditText().setText("");

            txtInputPlace.getEditText().setText("");

            txtInputNumOfChildren.getEditText().setText("");

            spnGender.setSelection(0);*/

        }//
        if (v == btnSubmit) {
            sendRequest();

        }//
    }
    private void sendRequest() {

         name = ed_name.getText().toString();

         age = ed_age.getText().toString();

         phone = ed_phone.getText().toString();

         loanamt = ed_loanamt.getText().toString();

         serveynum = ed_serveynum.getText().toString();

         passbooknum = ed_passbok.getText().toString();

         adhar = ed_adhar.getText().toString();

         village = ed_village.getText().toString();

         mandal = ed_mandal.getText().toString();

         dist = ed_dist.getText().toString();

         state = ed_state.getText().toString();
         pin = ed_pin.getText().toString();
         landarea = ed_landarea.getText().toString();
         landtype = ed_landtype.getText().toString();
         crophistory = ed_crophistory.getText().toString();
        cropRunningInfo=ed_cropRunningInfo.getText().toString();
         cropVariety=ed_cropVariety.getText().toString();
         insurenceperson = ed_insurenceperson.getText().toString();
         reason=ed_reason.getText().toString();
         workhours = ed_whours.getText().toString();
         monitoryhours = ed_mhours.getText().toString();
         profitneg = ed_profitnegotiation.getText().toString();


        /* fgender = gender.getSelectedItem().toString();
         ftype = farmartype.getSelectedItem().toString();
         lontype = loantype.getSelectedItem().toString();
         lanmer = landmer.getSelectedItem().toString();
         instype = insurencetype.getSelectedItem().toString();
         distype = Disastertype.getSelectedItem().toString();
         irrigation=irrigationtype.getSelectedItem().toString();
*/

        if (name == null || name.equals("")) {
            ed_name.setError("Can't be left blank");
            return;
        }
        else if (age == null || age.equals("")) {
            ed_age.setError("Can't be left blank");
            return;
        }
        else if (phone == null || phone.equals("")) {
            ed_phone.setError("Can't be left blank");
            return;
        }else if (serveynum == null || serveynum.equals("")) {
            ed_serveynum.setError("Can't be left blank");
            return;
        }else if (adhar == null || adhar.equals("")) {
            ed_adhar.setError("Can't be left blank");
            return;
        }
        else if (village == null || village.equals("")) {
            ed_village.setError("Can't be left blank");
            return;
        }
        else if (pin == null || pin.equals("")) {
            ed_pin.setError("Can't be left blank");
            return;
        }
        else if (landarea == null || landarea.equals("")) {
            ed_landarea.setError("Can't be left blank");
            return;
        }else if (landtype == null || landtype.equals("")) {
            ed_landtype.setError("Can't be left blank");
            return;
        }


        //
        //int position =  QuestionModel.questionList.size()-1;
        // QuestionModel model =  QuestionModel.questionList.get(position);
        JSONObject jsonDetails =  new JSONObject();
        L.d("farmerName = " + name);
        try {
            jsonDetails.put("name",name);
            jsonDetails.put("gender",fgender);
            jsonDetails.put("onBoarded",boarded);
            jsonDetails.put("age",age);
            jsonDetails.put("phone",phone+"");
            jsonDetails.put("type",ftype+"");
            jsonDetails.put("loanAvailed",lontype+"");
            jsonDetails.put("loanAmount",loanamt+"");
            jsonDetails.put("revenueServeyNumber",serveynum+"");
            jsonDetails.put("passbookNumber",passbooknum+"");
            jsonDetails.put("adharNumber",adhar+"");
            jsonDetails.put("village",village+"");
            jsonDetails.put("mandal",mandal+"");
            jsonDetails.put("district",dist+"");
            jsonDetails.put("state",state+"");
            jsonDetails.put("district",dist+"");
            jsonDetails.put("pincode",pin+"");
            jsonDetails.put("landArea",landarea+"");
            jsonDetails.put("landAreaUnits",lanmer+"");
            jsonDetails.put("landType",landtype+"");
            jsonDetails.put("cropHistory",crophistory+"");
            jsonDetails.put("cropRunningInfo",cropRunningInfo);
            jsonDetails.put("cropVariety",cropVariety);
            jsonDetails.put("insurancePaid",instype+"");
            jsonDetails.put("insurer",insurenceperson+"");
            jsonDetails.put("monitoring",distype+"");
            jsonDetails.put("disasterRecoveryReason",reason+"");
            jsonDetails.put("workingHours",workhours+"");
            jsonDetails.put("monitoringHours",monitoryhours+"");
            jsonDetails.put("negotiatedProfit",profitneg+"");
            jsonDetails.put("irrigationType",irrigation+"");


        } catch (JSONException e) {
            e.printStackTrace();
            L.e("sendRequest()::Creating json "+e.getLocalizedMessage());
        }//catch()....
        //model.setAnswer("Attached");

        QuestionModel.details = jsonDetails.toString();
        L.d("PersonDeatailActivity:: SendRequest():: answer"+ jsonDetails.toString());
        onButtonPressed();
    }//

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        gender = (Spinner)adapterView;
        farmartype = (Spinner)adapterView;
        loantype = (Spinner)adapterView;
        landmer = (Spinner)adapterView;
        insurencetype = (Spinner)adapterView;
        Disastertype = (Spinner)adapterView;
        irrigationtype = (Spinner)adapterView;
        boardedtype=(Spinner)adapterView;


        if(gender.getId() == R.id.fgender)
        {
            fgender = (String) adapterView.getItemAtPosition(i);
           // Toast.makeText(getContext(), "Your choose :" + fgender,Toast.LENGTH_SHORT).show();

        }

        if(landmer.getId() == R.id.landmer)
        {
            lanmer = (String) adapterView.getItemAtPosition(i);
          //  Toast.makeText(getContext(), "Your choose :" + lanmer,Toast.LENGTH_SHORT).show();

        }

        if(irrigationtype.getId() == R.id.irrigationtype)
        {
            irrigation = (String) adapterView.getItemAtPosition(i);
         //   Toast.makeText(getContext(), "Your choose :" + irrigation,Toast.LENGTH_SHORT).show();


        }
        if(boardedtype.getId() == R.id.boardedtype)
        {
            boarded = (String) adapterView.getItemAtPosition(i);
          //  Toast.makeText(getContext(), "Your choose :" + boarded,Toast.LENGTH_SHORT).show();


        }

        if(farmartype.getId() == R.id.ftype)
        {
            ftype = (String) adapterView.getItemAtPosition(i);
           // Toast.makeText(getContext(), "Your choose :" + ftype,Toast.LENGTH_SHORT).show();

            if(ftype.equals("Land Lord")){

                hmfworkerlayout.setVisibility(View.GONE);
                hmfownerlayout.setVisibility(View.VISIBLE);

            }
            else {
                hmfownerlayout.setVisibility(View.GONE);
                hmfworkerlayout.setVisibility(View.VISIBLE);

            }

        }

        if(loantype.getId() == R.id.loantype)
        {
            lontype = (String) adapterView.getItemAtPosition(i);
           // Toast.makeText(getContext(), "Your choose :" + lontype,Toast.LENGTH_SHORT).show();


            if(lontype.equals("No")){

                ed_loanamt.setVisibility(View.GONE);
            }
            else {
                ed_loanamt.setVisibility(View.VISIBLE);

            }
        }

        if(insurencetype.getId() == R.id.insurencetype)
        {
            instype = (String) adapterView.getItemAtPosition(i);

            if(instype.equals("No")){

                ed_insurenceperson.setVisibility(View.GONE);
            }
            else {
                ed_insurenceperson.setVisibility(View.VISIBLE);

            }
        }


        if(Disastertype.getId() == R.id.Disastertype)
        {
            distype = (String) adapterView.getItemAtPosition(i);

            if(distype.equals("No")){

                ed_reason.setVisibility(View.GONE);
            }
            else {
                ed_reason.setVisibility(View.VISIBLE);

            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
        void onMarked(boolean marked);

        void gotoBack();

        void onSubmit();
    }
}
