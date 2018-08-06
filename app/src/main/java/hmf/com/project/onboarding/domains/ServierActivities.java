package hmf.com.project.onboarding.domains;

import java.util.ArrayList;
import java.util.List;

public class ServierActivities{
	//private ArrayList<StatusCountsItem> statusCounts;
	private ArrayList<SurveyCriteriaItem> surveyCriteria;
	private String surveyActivityId;
	private List<QuestionSet> questionSets;

	public List<QuestionSet> getQuestionSets() {
		return questionSets;
	}

	public void setQuestionSets(List<QuestionSet> questionSets) {
		this.questionSets = questionSets;
	}

	public String getSurveyActivityId() {
		return surveyActivityId;
	}
	public void setSurveyActivityId(String surveyActivityId) {
		this.surveyActivityId = surveyActivityId;
	}
/*	public void setStatusCounts(ArrayList<StatusCountsItem> statusCounts){
		this.statusCounts = statusCounts;
	}
	public ArrayList<StatusCountsItem> getStatusCounts(){
		return statusCounts;
	}*/
	public void setSurveyCriteria(ArrayList<SurveyCriteriaItem> surveyCriteria){
		this.surveyCriteria = surveyCriteria;
	}
	public ArrayList<SurveyCriteriaItem> getSurveyCriteria(){
		return surveyCriteria;
	}

}