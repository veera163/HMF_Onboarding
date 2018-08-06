package hmf.com.project.onboarding.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
    private Context _context;


    public ConnectionDetector(Context context) {
        this._context = context;
    }

    /**
     * L̥
     * Checking for all possible internet providers
     **/

    public boolean isConnectedToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null)

                if (info.getState() == NetworkInfo.State.CONNECTED || info.getState() == NetworkInfo.State.CONNECTING) {
                    if (info.isAvailable()) {
                        return true;
                    }

                }

        }
        return false;
    }


}
