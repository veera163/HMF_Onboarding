package hmf.com.project.onboarding.utility;

import android.app.ProgressDialog;
import android.content.Context;

import android.view.Window;



/**
 * Created by sai on 9/18/2016.
 */
public class CustomProgressLoaderDialog {
    Context context;
    ProgressDialog progressDialog;

    public CustomProgressLoaderDialog(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please wait...");
    }

    public void showProgressLoader() {

        progressDialog.show();
    }

    public void dismissProgressLoader() {
        if (isShowing())
        progressDialog.dismiss();
    }

    public boolean isShowing() {
        return progressDialog.isShowing();
    }
}
