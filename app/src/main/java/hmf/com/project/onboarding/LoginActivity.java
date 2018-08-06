package hmf.com.project.onboarding;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import hmf.com.project.onboarding.api.ForgotPassApi;
import hmf.com.project.onboarding.domains.SimpleRes;
import hmf.com.project.onboarding.utility.AppConstant;
import hmf.com.project.onboarding.utility.ConnectionDetector;
import hmf.com.project.onboarding.utility.HttpHelper;
import hmf.com.project.onboarding.utility.LoggerUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private CardView mLoginFormView;
    TextView forgotpass;
    android.support.v7.app.AlertDialog alertDialogBuilder;
    EditText ed_forgotpass;
    private ProgressDialog progressDialog;
    Button bt_send;
    String MobilePattern = "[0-9]{10}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        forgotpass=(TextView)findViewById(R.id.forgotpass);

        /*mEmailView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_NAVIGATE_NEXT)
                    mPasswordView.setFocusable(true);
                return true;
            }
        });*/
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do here your stuff f
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = (CardView) findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        forgotpass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

    }


    private void showInputDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View promptView = layoutInflater.inflate(R.layout.activity_forgotpassword, null);
        alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this).create();
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.show();
        ed_forgotpass=(EditText)promptView.findViewById(R.id.forgot_phone);
        bt_send=(Button)promptView.findViewById(R.id.forgot_send);
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String forgotpass=ed_forgotpass.getText().toString();
                if(TextUtils.isEmpty(forgotpass)){
                    ed_forgotpass.setError(getString(R.string.error_field_required));

                }
                else  if(!forgotpass.matches(MobilePattern)){
                    ed_forgotpass.setError("Please enter valid 10 digit phone number");

                }

                else{

                    forgetpass(forgotpass);

                }
            }
        });
    }

    private void forgetpass(String forgotpass) {

        String url= AppConstant.BASEURL+"userManager/sendPassword/"+forgotpass;
        Log.e("url",url);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstant.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait.",
                "Being sent..!", true);

        ForgotPassApi forgotPassApi = retrofit.create(ForgotPassApi.class);
        Call<SimpleRes> listCall= forgotPassApi.getData(url);
        listCall.enqueue(new Callback<SimpleRes>() {
            @Override
            public void onResponse(Call<SimpleRes> call, Response<SimpleRes> response) {

                progressDialog.dismiss();
                alertDialogBuilder.dismiss();

                if(response.isSuccessful()){

                    if(response.body()!=null){

                        if(response.body().getMessageType().equals("success")){

                            Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                        else {

                            Toast.makeText(LoginActivity.this, response.body().getMessageType(), Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {


                        Toast.makeText(LoginActivity.this, "Data is Missing", Toast.LENGTH_SHORT).show();

                    }

                }
                else {

                    Toast.makeText(LoginActivity.this,"Something went Wrong", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<SimpleRes> call, Throwable t) {
                progressDialog.dismiss();
                alertDialogBuilder.dismiss();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });
    }


    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            //requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            //requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ESurvey.validateAccessToken()) {
            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
            finish();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_READ_CONTACTS) {
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //populateAutoComplete();
//            }
//        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isValidMobile(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            if (new ConnectionDetector(this).isConnectedToInternet())
                mAuthTask.execute((Void) null);
            else
                Snackbar.make(getCurrentFocus(), "you are in Offline", Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    private boolean isValidMobile(String email) {
        Pattern patterns = Pattern.compile("^[7-9][0-9]{9}$");
        return patterns.matcher(email).matches();
        // return true;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 0;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("would you Like to close this App !")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                                homeIntent.addCategory( Intent.CATEGORY_HOME );
                                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(homeIntent);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected String doInBackground(Void... params) {

            // TODO: attempt authentication against a network service.
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(AppConstant.BASEURL);
            urlBuilder.append(AppConstant.LOGINPATH);
            urlBuilder.append("password=");
            urlBuilder.append(mPassword);
            urlBuilder.append("&username=");
            urlBuilder.append(mEmail);
            ESurvey.setLoginId(mEmail);
            Log.e("veera",String.valueOf(urlBuilder.toString()));
            return HttpHelper.sendPOSTRequest(urlBuilder.toString(), new String(), AppConstant.HEADER);
        }

        @Override
        protected void onPostExecute(final String success) {
            mAuthTask = null;
            showProgress(false);

            LoggerUtils.info(LoginActivity.class.getSimpleName(), "response " + success);
            JSONObject jsonObject;
            try {
                if (!success.equalsIgnoreCase("NA")) {
                    jsonObject = new JSONObject(success);
                    if (jsonObject.has("access_token")) {

                        ESurvey.setAccessToken(jsonObject.getString("access_token"),
                                jsonObject.getString("refresh_token"), jsonObject.getInt("expires_in"));


                        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Bad Credentials", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /* if (success) {
                startActivity(new Intent(LoginActivity.this , ProfileActivity.class));
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }*/
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

