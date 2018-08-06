package hmf.com.project.onboarding.domains;

public class SurveyCriteriaItem {
    private String noOfPending;
    private String noOfOnHold;
    private String category;
    private String noOfSurveys;
    private String noOfCanceled;
    private String noOfCompleted;
    private String pendingToday;

    public String getPendingToday() {
        return pendingToday;
    }

    public void setPendingToday(String pendingToday) {
        this.pendingToday = pendingToday;
    }

    public void setNoOfPending(String noOfPending) {
        this.noOfPending = noOfPending;
    }

    public String getNoOfPending() {
        return noOfPending;
    }

    public void setNoOfOnHold(String noOfOnHold) {
        this.noOfOnHold = noOfOnHold;
    }

    public String getNoOfOnHold() {
        return noOfOnHold;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setNoOfSurveys(String noOfSurveys) {
        this.noOfSurveys = noOfSurveys;
    }

    public String getNoOfSurveys() {
        return noOfSurveys;
    }

    public void setNoOfCanceled(String noOfCanceled) {
        this.noOfCanceled = noOfCanceled;
    }

    public String getNoOfCanceled() {
        return noOfCanceled;
    }

    public void setNoOfCompleted(String noOfCompleted) {
        this.noOfCompleted = noOfCompleted;
    }

    public String getNoOfCompleted() {
        return noOfCompleted;
    }

    @Override
    public String toString() {
        return
                "SurveyCriteriaItem{" +
                        "noOfPending = '" + noOfPending + '\'' +
                        ",noOfOnHold = '" + noOfOnHold + '\'' +
                        ",category = '" + category + '\'' +
                        ",noOfSurveys = '" + noOfSurveys + '\'' +
                        ",noOfCanceled = '" + noOfCanceled + '\'' +
                        ",noOfCompleted = '" + noOfCompleted + '\'' +
                        "}";
    }
}
