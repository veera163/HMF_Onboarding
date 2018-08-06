package hmf.com.project.onboarding.utility;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by anurag.nigam on 5/4/2016.
 */
public class LoggerUtils {
    public static boolean isLogEnabled         = true;
    public static final String SERVICE_LOG_TAG = "TestServiceLog";
    public static final String APP_TAG         = "TestProject";

    private static String TAG = "Chathri";

    public static void d(String message){

        Log.d(TAG, message+"");
    }

    public static void t(String msg, Context context){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }//t

    public static void e(String message){

        Log.e(TAG, message+"");
    }
    public static void error(String tag, String msg)
    {
        if(isLogEnabled)
            Log.e(tag, msg);
    }

    public static void info(String tag, String msg)
    {
        if(isLogEnabled)
            Log.i(tag, msg);
    }

    public static void verbose(String tag, String msg)
    {
        if(isLogEnabled)
            Log.v(tag, msg);
    }

    public static void debug(String tag, String msg)
    {
        if(isLogEnabled)
            Log.d(tag, msg);
    }

    public static void printMessage(String msg)
    {
        if (isLogEnabled)
            System.out.println(msg);
    }
}
