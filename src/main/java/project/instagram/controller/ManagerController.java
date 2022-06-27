package project.instagram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.instagram.common.enums.constants.AppConstants;
import project.instagram.response.BlackHashtagResponse;
import project.instagram.response.HashtagResponse;
import project.instagram.response.MessageResponse;
import project.instagram.response.PackageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.response.TypeOfPackageResponse;
import project.instagram.response.UserResponse;
import project.instagram.service.BlackHashtagService;
import project.instagram.service.HashtagServive;
import project.instagram.service.ManagerService;
import project.instagram.service.PackageService;

@RestController
@RequestMapping(value = "/api/manager/")
@CrossOrigin(origins = "*")
public class ManagerController {

	@Autowired
	private ManagerService managerService;

	@Autowired
	private PackageService packageService;

	@Autowired
	private BlackHashtagService blackHashtagService;

	@Autowired
	private HashtagServive hashtagServive;

	@GetMapping(path = "/clients")
	public PagedResponse<UserResponse> findAllClients(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return managerService.findAllClients(page, size);
	}

	@PutMapping(path = "/client/updateactive/{staffId}")
	public ResponseEntity<MessageResponse> updateActiveClient(
			@PathVariable(name = "staffId", required = true) String staffId) {

		return managerService.updateActiveClient(staffId);
	}

	@GetMapping(path = "/client/{staffId}")
	public ResponseEntity<MessageResponse> getStaff(@PathVariable(name = "staffId", required = true) String staffId) {

		return managerService.getClient(staffId);
	}

	@GetMapping(path = "/type-of-packages")
	public PagedResponse<TypeOfPackageResponse> findAllTypeOfPackages(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return packageService.findAllTypeOfPackages(page, size);
	}

	@GetMapping(path = "/packages")
	public PagedResponse<PackageResponse> findAllPackages(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return packageService.findAllPackagesForStaff(page, size);
	}

	@GetMapping(path = "/extra-packages")
	public PagedResponse<PackageResponse> findAllExtraPackages(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return packageService.findAllExtraPackagesForStaff(page, size);
	}

	@GetMapping(value = "/black-hashtags")
	public PagedResponse<BlackHashtagResponse> getAllBlackHashtags(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return blackHashtagService.findAllBlackHashtags(page, size);
	}

	@PostMapping(value = "/black-hashtags")
	public ResponseEntity<MessageResponse> createBlackHashtag(
			@RequestParam(name = "name", required = true) String name) {

		return blackHashtagService.createBlackHashtagByManager(name);
	}

	@PutMapping(value = "/delete/black-hashtag/{name}")
	public ResponseEntity<MessageResponse> updateActiveBlackHashtag(
			@PathVariable(name = "name", required = true) String name) {

		return blackHashtagService.deleteBlackHashtag(name);
	}

	@GetMapping(value = "/hashtags")
	public PagedResponse<HashtagResponse> getAllHashtags(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return hashtagServive.findAllHashtags(page, size);
	}

	@PostMapping(value = "/hashtags")
	public ResponseEntity<MessageResponse> createHashtagByManager(
			@RequestParam(name = "hashtag", required = true) String hashtag) {

		return hashtagServive.createHashtagByManager(hashtag);
	}

}
