package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.request.PackageFormRequest;
import project.instagram.response.MessageResponse;
import project.instagram.response.PackageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.response.TypeOfPackageResponse;

public interface PackageService {
	ResponseEntity<MessageResponse> createPackage(PackageFormRequest packageFormRequest);
	ResponseEntity<MessageResponse> updateActivePackage(String packageId);
	PagedResponse<PackageResponse> findAllPackages(int page, int size);
	PagedResponse<PackageResponse> findAllExtraPackages(int page, int size);
	PagedResponse<TypeOfPackageResponse> findAllTypeOfPackages(int page, int size);
}
