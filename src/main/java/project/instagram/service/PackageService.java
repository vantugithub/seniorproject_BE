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

	PagedResponse<PackageResponse> findAllPackagesForClient(int page, int size);

	PagedResponse<PackageResponse> findAllExtraPackagesForClient(int page, int size);

	PagedResponse<TypeOfPackageResponse> findAllTypeOfPackages(int page, int size);

	PagedResponse<PackageResponse> findAllPackagesForStaff(int page, int size);

	PagedResponse<PackageResponse> findAllExtraPackagesForStaff(int page, int size);
	
	ResponseEntity<MessageResponse> createExtraPackageByStaff(PackageFormRequest packageFormRequest);
}
