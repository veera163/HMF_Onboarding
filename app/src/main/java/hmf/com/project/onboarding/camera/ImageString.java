package hmf.com.project.onboarding.camera;

import java.util.ArrayList;

/**
 * Created by saurabh.singh on 6/9/2016.
 */
public class ImageString {

    public static ArrayList<String> photoStringList =  new ArrayList<>();

    public static void clearAll(){
        if(photoStringList != null){
            photoStringList.clear();
        }
    }//
}
