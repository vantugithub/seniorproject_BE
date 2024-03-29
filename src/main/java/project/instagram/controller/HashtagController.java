package project.instagram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.instagram.response.MessageResponse;
import project.instagram.service.HashtagServive;

@RestController
@RequestMapping(value = "/api/")
@CrossOrigin(origins = "*")
public class HashtagController {
	
	@Autowired
	private HashtagServive hashtagServive;
	
	@PostMapping(value = "client/hashtag/{hashtagName}")
	public ResponseEntity<MessageResponse> createHashtagByClient(
			@PathVariable(name = "hashtagName", required = true) String hashtagName) {
		
		return hashtagServive.createHashtag(hashtagName);
	}
	
	@DeleteMapping(value = "client/hashtag/{hashtagName}")
	public ResponseEntity<MessageResponse> deleteHashtagByClient(
			@PathVariable(name ="hashtagName", required = true) String hashtagName) {
		
		return hashtagServive.deleteHashtag(hashtagName);
	}
	
	@PutMapping(value = "client/hashtag/enablecrawl/{hashtagName}/{crawlDate}")
	public ResponseEntity<MessageResponse> enableCrawlHashtag(
			@PathVariable(name = "hashtagName", required = true) String hashtagName,
			@PathVariable(name = "crawlDate", required = true) String crawlDateStr) {

		return hashtagServive.enableCrawlHashtagByClient(hashtagName, crawlDateStr);
	}
	
}
