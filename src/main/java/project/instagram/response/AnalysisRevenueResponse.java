package project.instagram.response;

public class AnalysisRevenueResponse {
	private String typeOfMember;
	private String monthStr;
	private Double totalPrice;

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

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public AnalysisRevenueResponse() {
	}

	public AnalysisRevenueResponse(String typeOfMember, String monthStr, Double totalPrice) {
		this.typeOfMember = typeOfMember;
		this.monthStr = monthStr;
		this.totalPrice = totalPrice;
	}

}
