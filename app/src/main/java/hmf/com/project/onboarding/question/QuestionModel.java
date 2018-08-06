package hmf.com.project.onboarding.question;

import java.util.ArrayList;

/**
 * Created by saurabh.singh on 4/12/2016.
 */
public class QuestionModel {


    public static ArrayList<QuestionModel> questionList = new ArrayList<>();
    public static String details = "";

    public static String setID;
    public static String latLong;
    private String question;
    private String optA;
    private String optB;
    private String optC;
    private String optD;
    private String optE;
    private String optF;
    private String optG;

    private boolean current = false;

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public String getOptF() {
        return optF;
    }

    public String getOptA() {
        return optA;
    }//getOptA()

    public void setOptA(String optA) {
        this.optA = optA;
    }//setOptB

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

    private String optH;
    private String optI;
    private String optJ;


    private String questId;
    private long bookmark = -1;
    private String answer;
    private boolean isMultiple = false;


    public boolean isMultiple() {
        return isMultiple;
    }

    public void setIsMultiple(boolean isMultiple) {
        this.isMultiple = isMultiple;
    }

    public String getOptE() {
        return optE;
    }

    public void setOptE(String optE) {
        this.optE = optE;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public long getBookmark() {
        return bookmark;
    }//getBookmark()

    public void setBookmark(long bookmark) {
        this.bookmark = bookmark;
    }//setBookmark()

    public String getQuestion() {
        return question;
    }//getQuestion()

    public void setQuestion(String question) {
        this.question = question;
    }//setQuestion()


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

    public String getOptD() {
        return optD;
    }

    public void setOptD(String optD) {
        this.optD = optD;
    }

    public String getQuestId() {
        return questId;
    }

    public void setQuestId(String questId) {
        this.questId = questId;
    }

    public static void clear() {
        if (questionList != null)
            questionList.clear();
        details = "";
    }//
}
