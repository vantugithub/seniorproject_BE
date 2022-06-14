package project.instagram.response;

import java.util.HashMap;

public class RevenueExtrapackageResponse {
	private String name;
	private Double totalPrice;
	private HashMap<String, Integer> hashMap;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public HashMap<String, Integer> getHashMap() {
		return hashMap;
	}

	public void setHashMap(HashMap<String, Integer> hashMap) {
		this.hashMap = hashMap;
	}

	public RevenueExtrapackageResponse() {
	}

	public RevenueExtrapackageResponse(String name, Double totalPrice, HashMap<String, Integer> hashMap) {
		this.name = name;
		this.totalPrice = totalPrice;
		this.hashMap = hashMap;
	}

}
