package hmf.com.project.onboarding.question;

import java.util.ArrayList;

/**
 * Created by saurabh.singh on 5/18/2016.
 */
public class Stamps {

    public static ArrayList<Stamps> stampList = new ArrayList<>();
    private String num;
    private String stampText;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getStampText() {
        return stampText;
    }

    public void setStampText(String stampText) {
        this.stampText = stampText;
    }
    public static void clearStamps(){
        if(null!=stampList)
        stampList.clear();
    }//
}
