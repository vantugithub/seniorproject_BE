package project.instagram.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import project.instagram.common.enums.constants.PackageConstants;
import project.instagram.entity.Package;
import project.instagram.entity.TypeOfPackage;
import project.instagram.repository.PackageRepository;
import project.instagram.repository.TypeOfPackageRepository;
import project.instagram.request.PackageFormRequest;
import project.instagram.response.MessageResponse;
import project.instagram.response.PackageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.response.TypeOfPackageResponse;
import project.instagram.service.PackageService;

@Service
public class PackageServiceImpl implements PackageService {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private PackageRepository packageRepository;

	@Autowired
	private TypeOfPackageRepository typeOfPackageRepository;

	Package createPackage(PackageFormRequest packageFormRequest, TypeOfPackage typeOfPackage) {
		Package newPackage = mapper.map(packageFormRequest, Package.class);
		newPackage.setNumberOfMonth(packageFormRequest.getNumberOfMonths());
		newPackage.setTypeOfPackage(typeOfPackage);
		newPackage = packageRepository.save(newPackage);

		return newPackage;
	}

	MessageResponse validateValidPackageAndTypeOfPackage(Optional<TypeOfPackage> typeOfPackage,
			Optional<Package> existsPackage) {
		MessageResponse messageResponse = new MessageResponse();
		if (typeOfPackage.isEmpty()) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			messageResponse.setMessage(PackageConstants.TYPE_OF_PACKAGE_NOT_EXISTS);
		}
		if (!existsPackage.isEmpty()) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			messageResponse.setMessage(PackageConstants.PACKAGE_EXISTS);
		}

		return messageResponse;
	}

	private PackageResponse createPackageResponse(Package p) {
		PackageResponse newPackageResponse = mapper.map(p, PackageResponse.class);
		newPackageResponse.setId(p.getId().toString());
		newPackageResponse.setActive(p.isActive());

		return newPackageResponse;
	}

	@Override
	public ResponseEntity<MessageResponse> createPackage(PackageFormRequest packageFormRequest) {
		UUID packageId = UUID.fromString(packageFormRequest.getTypeOfPackageId());
		Optional<TypeOfPackage> typeOfPackage = typeOfPackageRepository.findById(packageId);
		Optional<Package> existsPackage = packageRepository.findPackageByName(packageFormRequest.getName());

		MessageResponse messageResponse = validateValidPackageAndTypeOfPackage(typeOfPackage, existsPackage);

		if (messageResponse.getMessage() != null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);

		Package newPackage = createPackage(packageFormRequest, typeOfPackage.get());

		if (newPackage == null) {
			messageResponse.setMessage(PackageConstants.CREATED_PACKAGE_CREATED_FAILED);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		messageResponse.setStatus(HttpStatus.OK.value());
		messageResponse.setData(newPackage);
		messageResponse.setMessage(PackageConstants.CREATED_PACKAGE_SUCCESSFULLY);

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

	@Override
	public ResponseEntity<MessageResponse> updateActivePackage(String id) {
		UUID packageId = UUID.fromString(id);
		MessageResponse messageResponse = new MessageResponse();
		Optional<Package> updatingPackage = packageRepository.findById(packageId);

		if (updatingPackage.isEmpty()) {
			messageResponse.setMessage(PackageConstants.PACKAGE_NOT_EXISTS);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		updatingPackage.get().setActive(!updatingPackage.get().isActive());
		packageRepository.save(updatingPackage.get());

		messageResponse.setStatus(HttpStatus.OK.value());
		messageResponse.setMessage(PackageConstants.UPDATED_PACKAGE_SUCCESSFULLY);
		PackageResponse packageResponse = mapper.map(updatingPackage.get(), PackageResponse.class);
		packageResponse.setId(updatingPackage.get().getId().toString());
		messageResponse.setData(packageResponse);

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

	@Override
	public PagedResponse<PackageResponse> findAllPackagesForClient(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		TypeOfPackage typeOfPackage = typeOfPackageRepository.findByName(PackageConstants.PACKAGE_TYPE).get();
		Page<Package> packages = packageRepository.findAllByActiveTrueAndTypeOfPackage(typeOfPackage, pageable);

		List<PackageResponse> packagesResponse = new ArrayList<PackageResponse>(packages.getContent().size());

		for (Package packageInList : packages.getContent()) {
			packagesResponse.add(createPackageResponse(packageInList));
		}

		return new PagedResponse<>(packagesResponse, packages.getNumber(), packages.getSize(),
				packages.getTotalElements(), packages.getTotalPages(), packages.isLast());
	}

	@Override
	public PagedResponse<PackageResponse> findAllExtraPackagesForClient(int page, int size) {

		Pageable pageable = PageRequest.of(page, size);
		TypeOfPackage typeOfPackage = typeOfPackageRepository.findByName(PackageConstants.EXTRA_PACKAGE_TYPE).get();
		Page<Package> packages = packageRepository.findAllByActiveTrueAndTypeOfPackage(typeOfPackage, pageable);

		List<PackageResponse> packagesResponse = new ArrayList<PackageResponse>(packages.getContent().size());

		for (Package packageInList : packages.getContent()) {
			packagesResponse.add(createPackageResponse(packageInList));
		}

		return new PagedResponse<>(packagesResponse, packages.getNumber(), packages.getSize(),
				packages.getTotalElements(), packages.getTotalPages(), packages.isLast());
	}

	@Override
	public PagedResponse<TypeOfPackageResponse> findAllTypeOfPackages(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<TypeOfPackage> typeOfPackages = typeOfPackageRepository.findAll(pageable);

		List<TypeOfPackageResponse> typeOfPackagesResponse = new ArrayList<TypeOfPackageResponse>(
				typeOfPackages.getContent().size());

		for (TypeOfPackage typeOfPackage : typeOfPackages.getContent()) {
			typeOfPackagesResponse.add(createTypeOfPackageResponse(typeOfPackage));
		}

		return new PagedResponse<>(typeOfPackagesResponse, typeOfPackages.getNumber(), typeOfPackages.getSize(),
				typeOfPackages.getTotalElements(), typeOfPackages.getTotalPages(), typeOfPackages.isLast());
	}

	private TypeOfPackageResponse createTypeOfPackageResponse(TypeOfPackage typeOfPackage) {
		TypeOfPackageResponse typeOfPackageResponse = new TypeOfPackageResponse();
		typeOfPackageResponse.setId(typeOfPackage.getId().toString());
		typeOfPackageResponse.setName(typeOfPackage.getName());

		return typeOfPackageResponse;
	}

	@Override
	public PagedResponse<PackageResponse> findAllPackagesForStaff(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		TypeOfPackage typeOfPackage = typeOfPackageRepository.findByName(PackageConstants.PACKAGE_TYPE).get();
		Page<Package> packages = packageRepository.findAllByTypeOfPackage(typeOfPackage, pageable);

		List<PackageResponse> packagesResponse = new ArrayList<PackageResponse>(packages.getContent().size());

		for (Package packageInList : packages.getContent()) {
			packagesResponse.add(createPackageResponse(packageInList));
		}

		return new PagedResponse<>(packagesResponse, packages.getNumber(), packages.getSize(),
				packages.getTotalElements(), packages.getTotalPages(), packages.isLast());
	}

	@Override
	public PagedResponse<PackageResponse> findAllExtraPackagesForStaff(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		TypeOfPackage typeOfPackage = typeOfPackageRepository.findByName(PackageConstants.EXTRA_PACKAGE_TYPE).get();
		Page<Package> packages = packageRepository.findAllByTypeOfPackage(typeOfPackage, pageable);

		List<PackageResponse> packagesResponse = new ArrayList<PackageResponse>(packages.getContent().size());

		for (Package packageInList : packages.getContent()) {
			packagesResponse.add(createPackageResponse(packageInList));
		}

		return new PagedResponse<>(packagesResponse, packages.getNumber(), packages.getSize(),
				packages.getTotalElements(), packages.getTotalPages(), packages.isLast());
	}

	@Override
	public ResponseEntity<MessageResponse> createExtraPackageByStaff(PackageFormRequest packageFormRequest) {
		
		Optional<TypeOfPackage> typeOfPackage = typeOfPackageRepository.findByName(PackageConstants.EXTRA_PACKAGE_TYPE);
		Optional<Package> existsPackage = packageRepository.findPackageByName(packageFormRequest.getName());

		MessageResponse messageResponse = validateValidPackageAndTypeOfPackage(typeOfPackage, existsPackage);

		if (messageResponse.getMessage() != null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);

		Package newPackage = createPackage(packageFormRequest, typeOfPackage.get());

		if (newPackage == null) {
			messageResponse.setMessage(PackageConstants.CREATED_PACKAGE_CREATED_FAILED);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		messageResponse.setStatus(HttpStatus.OK.value());
		messageResponse.setData(newPackage);
		messageResponse.setMessage(PackageConstants.CREATED_PACKAGE_SUCCESSFULLY);

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

}
