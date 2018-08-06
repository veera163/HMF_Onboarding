package hmf.com.project.onboarding.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import java.util.List;

/**
 * Created by saurabh.singh on 8/29/2016.
 * Description
 */
public class LastLocation {

    public static Location getLastLocation(Context context) {
        if(null == context){
                return null;
        }//if()...
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        List<String> matchingProviders = locationManager.getAllProviders();
        for (String provider : matchingProviders) {
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }//if()...
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
               return location;
            }//
        }//for....
        return null;
    }
}
