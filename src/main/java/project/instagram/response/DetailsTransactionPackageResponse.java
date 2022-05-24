package project.instagram.response;

public class DetailsTransactionPackageResponse {
	private String transactionPackageId;
	private RunningSummaryResponse runningSummaryResponse;
	private RemainingQuantityResponse remainingQuantityResponse;

	public String getTransactionPackageId() {
		return transactionPackageId;
	}

	public void setTransactionPackageId(String transactionPackageId) {
		this.transactionPackageId = transactionPackageId;
	}

	public RunningSummaryResponse getRunningSummaryResponse() {
		return runningSummaryResponse;
	}

	public void setRunningSummaryResponse(RunningSummaryResponse runningSummaryResponse) {
		this.runningSummaryResponse = runningSummaryResponse;
	}

	public RemainingQuantityResponse getRemainingQuantityResponse() {
		return remainingQuantityResponse;
	}

	public void setRemainingQuantityResponse(RemainingQuantityResponse remainingQuantityResponse) {
		this.remainingQuantityResponse = remainingQuantityResponse;
	}

	public DetailsTransactionPackageResponse(String transactionPackageId, RunningSummaryResponse runningSummaryResponse,
			RemainingQuantityResponse remainingQuantityResponse) {
		super();
		this.transactionPackageId = transactionPackageId;
		this.runningSummaryResponse = runningSummaryResponse;
		this.remainingQuantityResponse = remainingQuantityResponse;
	}

	public DetailsTransactionPackageResponse() {
		super();
	}

}
