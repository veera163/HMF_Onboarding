package hmf.com.project.onboarding.domains;

public class StatusCountsItem{
	private int total;
	private String status;

	public void setTotal(int total){
		this.total = total;
	}

	public int getTotal(){
		return total;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"StatusCountsItem{" + 
			"total = '" + total + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}
