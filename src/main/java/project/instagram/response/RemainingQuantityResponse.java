package project.instagram.response;

public class RemainingQuantityResponse {
	private byte remainingQuantityCrawlHashtag;
	private byte remainingQuantitySearchHashtag;

	public byte getRemainingQuantityCrawlHashtag() {
		return remainingQuantityCrawlHashtag;
	}

	public void setRemainingQuantityCrawlHashtag(byte remainingQuantityCrawlHashtag) {
		this.remainingQuantityCrawlHashtag = remainingQuantityCrawlHashtag;
	}

	public byte getRemainingQuantitySearchHashtag() {
		return remainingQuantitySearchHashtag;
	}

	public void setRemainingQuantitySearchHashtag(byte remainingQuantitySearchHashtag) {
		this.remainingQuantitySearchHashtag = remainingQuantitySearchHashtag;
	}

	public RemainingQuantityResponse(byte remainingQuantityCrawlHashtag, byte remainingQuantitySearchHashtag) {
		this.remainingQuantityCrawlHashtag = remainingQuantityCrawlHashtag;
		this.remainingQuantitySearchHashtag = remainingQuantitySearchHashtag;
	}

	public RemainingQuantityResponse() {
	}
}
