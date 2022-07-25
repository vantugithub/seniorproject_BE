package project.instagram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.instagram.response.MessageResponse;
import project.instagram.service.RecommendService;

@RestController
@RequestMapping(value = "/api/recommmend")
@CrossOrigin(origins = "*")
public class RecommendController {

	@Autowired
	private RecommendService recommendService;

	@GetMapping(value = "/longest-hashtags")
	public ResponseEntity<MessageResponse> getLongestHashtags(
			@RequestParam(name = "hashtagRunningHistoryId", required = true) String hashtagRunningHistoryId) {

		return recommendService.getLongestHashtags(hashtagRunningHistoryId);
	}

	@GetMapping(value = "/related-hashtags")
	public ResponseEntity<MessageResponse> getRelatedHashtags(
			@RequestParam(name = "hashtags", required = true) String hashtags) {

		return recommendService.getRelatedHashtags(hashtags);
	}
}
