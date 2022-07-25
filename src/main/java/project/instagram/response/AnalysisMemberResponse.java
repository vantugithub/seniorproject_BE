package project.instagram.response;

public class AnalysisMemberResponse {
	private String typeOfMember;
	private String monthStr;
	private int count;

	public String getTypeOfMember() {
		return typeOfMember;
	}

	public void setTypeOfMember(String typeOfMember) {
		this.typeOfMember = typeOfMember;
	}

	public String getMonthStr() {
		return monthStr;
	}

	public void setMonthStr(String monthStr) {
		this.monthStr = monthStr;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public AnalysisMemberResponse(String typeOfMember, String monthStr, int count) {
		this.typeOfMember = typeOfMember;
		this.monthStr = monthStr;
		this.count = count;
	}

	public AnalysisMemberResponse() {
	}

}
