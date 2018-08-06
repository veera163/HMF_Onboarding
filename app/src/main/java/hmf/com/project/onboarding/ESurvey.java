package hmf.com.project.onboarding;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Environment;
import android.preference.PreferenceManager;

import hmf.com.project.onboarding.domains.UserDomain;
import hmf.com.project.onboarding.logger.L;
import hmf.com.project.onboarding.utility.AppConstant;
import com.google.gson.Gson;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class ESurvey extends Application {
    private static final String DIR_NAME = "eSurvey";
    public static final String FILE_NAME = "conversation.mp3";
    public static final String FILE_NAME_PHOTO = "JPEG_Temp";
    public static final String DIR_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/" + DIR_NAME;
    public static final boolean IS_DEBUG = true;
    private static final String LANGUAGE = "LANGUAGE";
    private static ESurvey mInstance;
   // public static final String URL = "http://34.195.106.0";
   public static final String URL = AppConstant.BASEURL;

   // public static final String URL = "http://192.168.0.105:9090";
    static SharedPreferences preference;
    static SharedPreferences.Editor editor;
    private static String ACCESSTOKEN = "accesstoken";
    private static String REFRESHTOKEN = "refreshtoken";
    private static String USERINFO = "userInfo";
    private static Gson gson = new Gson();
    private static String SURVEYORACTIVITYID = "surveyorActivityId";
    private static String EXPIREDATE = "ExpiteDate";
    private static String USERLOGINID = "USERLOGINID";

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        preference = this.getSharedPreferences("Esurvey", 0);
        editor = preference.edit();
        File file = new File(DIR_PATH);
        if (!file.exists()) {
            file.mkdir();
        }//if()
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfig);
        //changeLang("te");

    }//onCreate()

    public static void setAccessToken(String accessToken, String refreshToken, int expires_in) {
        editor.putString(ACCESSTOKEN, accessToken);
        editor.putString(REFRESHTOKEN, refreshToken);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, expires_in);
        editor.putLong(EXPIREDATE, calendar.getTime().getTime());
        editor.commit();
    }

    public static String getAccessToken() {
        return preference.getString(ACCESSTOKEN, "NA");
    }

    public static UserDomain getUser() {

        return gson.fromJson(preference.getString(USERINFO, "NA"), UserDomain.class);

    }

    public static void setUser(String user) {
        editor.putString(USERINFO, user);
        editor.commit();
    }

    public static void setSurveyActivityId(String surveyActivityId) {
        editor.putString(SURVEYORACTIVITYID, surveyActivityId);
        editor.commit();
    }

    public static String getSurveyActivityId() {
        return preference.getString(SURVEYORACTIVITYID, "NA");
    }

    public static ESurvey getmInstance() {
        return mInstance;

    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        L.d("Application Terminated");
    }

    public void changeLang(String lang) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
            SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(this).edit();
            ed.putString(LANGUAGE, lang);
            ed.commit();
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration conf = new Configuration(config);
            conf.locale = locale;
            getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources()
                    .getDisplayMetrics());
        }
    }

    public String getLang() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString(LANGUAGE, "te");
    }

    public static Context getApplication() {

        return mInstance.getApplicationContext();
    }

    @SuppressLint({"CommitPrefEdits", "ApplySharedPref"})
    public static void clearCache() {
        SharedPreferences.Editor editor = preference.edit();
        editor.clear();
        editor.commit();
    }

    public static boolean validateAccessToken() {
        long l = preference.getLong(EXPIREDATE, 0);
        Date expireDate = new Date(l);
        Calendar expireDateOnCal = Calendar.getInstance();
        expireDateOnCal.setTime(expireDate);
        Calendar currentDateOnCal = Calendar.getInstance();
        return expireDate.after(currentDateOnCal.getTime());
    }

    public static void setLoginId(String mEmail) {
        editor.putString(USERLOGINID, mEmail);
        editor.commit();
    }

    public static String getLoginId() {
        return preference.getString(USERLOGINID, "NA");
    }
}//ESurvey
