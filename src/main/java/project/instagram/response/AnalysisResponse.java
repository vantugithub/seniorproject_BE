package project.instagram.response;

import java.io.Serializable;
import java.util.List;

public class AnalysisResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private String dateOfAnalysis;
	private String hashtag;
	private List<CounterResponse> counterResponses;

	public String getDateOfAnalysis() {
		return dateOfAnalysis;
	}

	public void setDateOfAnalysis(String dateOfAnalysis) {
		this.dateOfAnalysis = dateOfAnalysis;
	}

	public String getHashtag() {
		return hashtag;
	}

	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}

	public List<CounterResponse> getCounterResponses() {
		return counterResponses;
	}

	public void setCounterResponses(List<CounterResponse> counterResponses) {
		this.counterResponses = counterResponses;
	}

	public AnalysisResponse() {
	}

	public AnalysisResponse(String dateOfAnalysis, String hashtag, List<CounterResponse> counterResponses) {
		this.dateOfAnalysis = dateOfAnalysis;
		this.hashtag = hashtag;
		this.counterResponses = counterResponses;
	}

}
