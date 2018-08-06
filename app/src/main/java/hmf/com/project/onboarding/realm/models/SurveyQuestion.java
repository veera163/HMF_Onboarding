package hmf.com.project.onboarding.realm.models;

import io.realm.RealmObject;

/**
 * Created by saurabh.singh on 7/2/2016.
 */
public class SurveyQuestion extends RealmObject {

    
    private String qId ;
    private String question ;
    private String optA;
    private String optB;
    private String optC;
    private String optD;
    private String optE;
    private String optF;
    private String optG;
    private String optH;
    private String optI;

    public String getOptF() {
        return optF;
    }

    public void setOptF(String optF) {
        this.optF = optF;
    }

    public String getOptG() {
        return optG;
    }

    public void setOptG(String optG) {
        this.optG = optG;
    }

    public String getOptH() {
        return optH;
    }

    public void setOptH(String optH) {
        this.optH = optH;
    }

    public String getOptI() {
        return optI;
    }

    public void setOptI(String optI) {
        this.optI = optI;
    }

    public String getOptJ() {
        return optJ;
    }

    public void setOptJ(String optJ) {
        this.optJ = optJ;
    }

    private String optJ;

    private boolean isMultiple = false;

    public String getqId() {
        return qId;
    }//

    public void setqId(String qId) {
        this.qId = qId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptE() {
        return optE;
    }

    public void setOptE(String optE) {
        this.optE = optE;
    }

    public String getOptD() {
        return optD;
    }

    public void setOptD(String optD) {
        this.optD = optD;
    }

    public String getOptA() {
        return optA;
    }

    public void setOptA(String optA) {
        this.optA = optA;
    }

    public String getOptB() {
        return optB;
    }

    public void setOptB(String optB) {
        this.optB = optB;
    }

    public String getOptC() {
        return optC;
    }

    public void setOptC(String optC) {
        this.optC = optC;
    }

    public boolean isMultiple() {
        return isMultiple;
    }

    public void setIsMultiple(boolean isMultiple) {
        this.isMultiple = isMultiple;
    }
}
