package hmf.com.project.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 2/7/2018.
 */

public class FarmerDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    List<String> categories;
    ArrayAdapter<String> dataAdapter;
    EditText ed_phone,ed_idnum;
    String phone,idnumber,idtype;
    Button details_submit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmer_details);
        getSupportActionBar().hide();
        details_submit=(Button)findViewById(R.id.details_submit);
        ed_phone=(EditText)findViewById(R.id.phone);
        ed_idnum=(EditText)findViewById(R.id.idnumber);

        details_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            phone=ed_phone.getText().toString();
            idnumber=ed_idnum.getText().toString();
                boolean isValid = true;
                View focusView = null;
                ed_phone.setError(null);

                if (TextUtils.isEmpty(phone)) {
                    ed_phone.setError(getString(R.string.error_field_required));
                    focusView = ed_phone;
                    isValid = false;
                }
                else if((!isPhonevalid(String.valueOf(phone)))){
                    ed_phone.setError("Enter validate patteren");
                    focusView = ed_phone;
                    isValid = false;
                }
                else  if(TextUtils.isEmpty(idnumber)){
                    ed_idnum.setError(getString(R.string.error_field_required));
                    focusView = ed_idnum;
                    isValid = false;
                }

                if (isValid){
                    Intent i= new Intent(FarmerDetails.this,MapsActivity.class);
                    i.putExtra("phone",phone);
                    i.putExtra("idnum",idnumber);
                    i.putExtra("idtype",idtype);
                    startActivity(i);
                }
                else {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                }

            }
        });

         spinner = (Spinner) findViewById(R.id.idtype);
        spinner.setOnItemSelectedListener(this);
         categories = new ArrayList<String>();
        categories.add("Aadhaar Card");
        categories.add("Voter Id");
        categories.add("Pan Card");
        categories.add("Kisan id");
         dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);


        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public void onBackPressed() {
        Intent i= new Intent(FarmerDetails.this,ProfileActivity.class);
        startActivity(i);
    }

    private boolean isPhonevalid(String mobile) {
        //TODO: Replace this with your own logic
        return mobile.length() >= 10;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        idtype = adapterView.getItemAtPosition(i).toString();
        // Showing selected spinner item
       // Toast.makeText(adapterView.getContext(), "Selected: " + idtype, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
