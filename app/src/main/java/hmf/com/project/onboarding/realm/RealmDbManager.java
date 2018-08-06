package hmf.com.project.onboarding.realm;

import io.realm.Realm;


/**
 * Created by saurabh.singh on 4/27/2016.
 */
public class RealmDbManager {
    private static RealmDbManager dbManager;

    private Realm realm = null;
    public static RealmDbManager getInstance(){
        if(dbManager == null){
            dbManager = new RealmDbManager();
        }//if()
        return dbManager;
    }//getInstance

    public Realm getRealm() {
        return realm;
    }

    private RealmDbManager(){
    }


}
