package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.response.MessageResponse;

public interface AnalysisService {
	ResponseEntity<MessageResponse> getAnalysisByDateAndClient(String hashtagRunningHistoryId);

	ResponseEntity<MessageResponse> getAnalysisHashtagByPeriodOfTime(String hashtag, String startDate, String endDate);
}
