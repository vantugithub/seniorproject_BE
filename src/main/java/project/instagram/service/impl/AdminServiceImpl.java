package project.instagram.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import project.instagram.common.enums.RoleName;
import project.instagram.common.enums.constants.UserConstants;
import project.instagram.entity.Client;
import project.instagram.entity.Role;
import project.instagram.entity.Staff;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.RoleRepository;
import project.instagram.repository.StaffRepository;
import project.instagram.request.CreateStaffRequest;
import project.instagram.request.UpdateStaffRequest;
import project.instagram.response.MessageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.response.UserResponse;
import project.instagram.security.SecurityAuditorAware;
import project.instagram.service.AdminService;
import project.instagram.utils.BCryptUtils;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private StaffRepository staffRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private SecurityAuditorAware securityAuditorAware;

	@Autowired
	private BCryptUtils bCryptUtils;

	private boolean contains(String roleName) {
		for (RoleName role : RoleName.values()) {
			if (role.name().equals(roleName)) {
				return true;
			}
		}

		return false;
	}

	private UserResponse createUserResponse(Client client) {

		return new UserResponse(client.getId().toString(), client.getFirstName(), client.getLastName(),
				client.getEmail(), client.getPhone(), client.getAddress(), client.getBiography(), client.isActive(),
				client.getCreatedDate().toString());
	}

	private UserResponse createUserResponse(Staff staff) {

		return new UserResponse(staff.getId().toString(), staff.getFirstName(), staff.getLastName(), staff.getEmail(),
				staff.getPhone(), staff.getAddress(), staff.isActive(),
				staff.getCreatedDate() == null ? "" : staff.getCreatedDate().toString());
	}

	private UserResponse createStaffResponse(Staff staff) {

		return new UserResponse(staff.getId().toString(), staff.getFirstName(), staff.getLastName(), staff.getEmail(),
				staff.getPhone(), staff.getAddress(), staff.isActive(),
				staff.getCreatedDate() == null ? "" : staff.getCreatedDate().toString(),
				staff.getRole().getName().toString());
	}

	private boolean isEmailExists(String email) {
		Optional<Staff> staff = staffRepository.findByEmail(email);
		if (!staff.isEmpty()) {
			return true;
		}

		return false;
	}

	private Staff createStaff(CreateStaffRequest createStaffRequest, Role role) {
		Staff creator = staffRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();

		Staff staff = new Staff();
		staff.setActive(true);
		staff.setCreatedById(creator);
		staff.setEmail(createStaffRequest.getEmail());
		staff.setPassword(bCryptUtils.bcryptEncoder(createStaffRequest.getPassword()));
		staff.setRole(role);
		staff = staffRepository.save(staff);

		return staff;
	}

	private boolean isStaffExists(UUID staffUUID) {

		Optional<Staff> staff = staffRepository.findById(staffUUID);

		if (staff.isEmpty()) {
			return false;
		}
		return true;
	}

	private void updateActiveStaff(String staffId) {
		UUID StaffUUID = UUID.fromString(staffId);
		Staff staff = staffRepository.getById(StaffUUID);
		staff.setActive(!staff.isActive());

		staffRepository.save(staff);
	}

	private void updateInforStaff(UpdateStaffRequest updateStaffRequest) {

		Staff staff = staffRepository.findByEmail(updateStaffRequest.getEmail()).get();
		Role role = roleRepository.findByName(RoleName.valueOf(updateStaffRequest.getRoleName()));

		staff.setActive(updateStaffRequest.isActive());
		staff.setAddress(updateStaffRequest.getAddress());
		staff.setFirstName(updateStaffRequest.getFirstName());
		staff.setLastName(updateStaffRequest.getLastName());
		staff.setPhone(updateStaffRequest.getPhone());
		staff.setRole(role);

		staffRepository.save(staff);
	}

	@Override
	public PagedResponse<UserResponse> findAllClients(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Client> clients = clientRepository.findAllByOrderByCreatedDateDesc(pageable);

		List<UserResponse> userResponses = new ArrayList<UserResponse>(clients.getContent().size());

		for (Client client : clients.getContent()) {
			userResponses.add(createUserResponse(client));
		}

		return new PagedResponse<>(userResponses, clients.getNumber(), clients.getSize(), clients.getTotalElements(),
				clients.getTotalPages(), clients.isLast());
	}

	@Override
	public PagedResponse<UserResponse> findAllStaffs(int page, int size) {
		Role role = roleRepository.findByName(RoleName.ROLE_STAFF);
		Pageable pageable = PageRequest.of(page, size);
		Page<Staff> staffs = staffRepository.findAllByRoleOrderByCreatedDateDesc(role, pageable);

		List<UserResponse> userResponses = new ArrayList<UserResponse>(staffs.getContent().size());

		for (Staff staff : staffs.getContent()) {
			userResponses.add(createUserResponse(staff));
		}

		return new PagedResponse<>(userResponses, staffs.getNumber(), staffs.getSize(), staffs.getTotalElements(),
				staffs.getTotalPages(), staffs.isLast());
	}

	@Override
	public PagedResponse<UserResponse> findAllManagers(int page, int size) {
		Role role = roleRepository.findByName(RoleName.ROLE_MANAGER);
		Pageable pageable = PageRequest.of(page, size);
		Page<Staff> managers = staffRepository.findAllByRoleOrderByCreatedDateDesc(role, pageable);

		List<UserResponse> userResponses = new ArrayList<UserResponse>(managers.getContent().size());

		for (Staff staff : managers.getContent()) {
			userResponses.add(createUserResponse(staff));
		}

		return new PagedResponse<>(userResponses, managers.getNumber(), managers.getSize(), managers.getTotalElements(),
				managers.getTotalPages(), managers.isLast());
	}

	@Override
	public ResponseEntity<MessageResponse> createStaff(CreateStaffRequest createStaffRequest) {
		MessageResponse messageResponse = new MessageResponse();

		if (!contains(createStaffRequest.getRoleName())) {
			messageResponse.setMessage(UserConstants.ROLE_NOT_EXISTS);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		Role role = roleRepository.findByName(RoleName.valueOf(createStaffRequest.getRoleName()));

		if (role == null) {
			messageResponse.setMessage(UserConstants.ROLE_NOT_EXISTS);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		if (isEmailExists(createStaffRequest.getEmail())) {
			messageResponse.setMessage(UserConstants.EMAIL_EXISTS);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		Staff createStaff = createStaff(createStaffRequest, role);

		if (createStaff == null) {
			messageResponse.setMessage(UserConstants.CREATE_STAFF_FAILED);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		messageResponse.setMessage(UserConstants.CREATE_STAFF_SUCCESSFULLY);
		messageResponse.setStatus(HttpStatus.OK.value());
		messageResponse.setData(createUserResponse(createStaff));

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

	@Override
	public ResponseEntity<MessageResponse> findAllRoles() {
		MessageResponse messageResponse = new MessageResponse();
		List<Role> roles = roleRepository.findAll();
		messageResponse.setData(roles);

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

	@Override
	public ResponseEntity<MessageResponse> changeActiveStaff(String staffId) {
		MessageResponse messageResponse = new MessageResponse();
		UUID StaffUUID = UUID.fromString(staffId);
		if (!isStaffExists(StaffUUID)) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			messageResponse.setMessage(UserConstants.ACCOUNT_NOT_EXISTS);

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		updateActiveStaff(staffId);
		messageResponse.setStatus(HttpStatus.OK.value());
		messageResponse.setMessage(UserConstants.UPDATE_SUCCESS);

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

	@Override
	public ResponseEntity<MessageResponse> updateStaff(UpdateStaffRequest updateStaffRequest) {

		MessageResponse messageResponse = new MessageResponse();
		UUID StaffUUID = UUID.fromString(updateStaffRequest.getStaffId());

		if (!isStaffExists(StaffUUID)) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			messageResponse.setMessage(UserConstants.ACCOUNT_NOT_EXISTS);

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		updateInforStaff(updateStaffRequest);
		messageResponse.setStatus(HttpStatus.OK.value());
		messageResponse.setMessage(UserConstants.UPDATE_SUCCESS);

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

	@Override
	public ResponseEntity<MessageResponse> getStaff(String staffID) {

		MessageResponse messageResponse = new MessageResponse();
		UUID StaffUUID = UUID.fromString(staffID);

		if (!isStaffExists(StaffUUID)) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			messageResponse.setMessage(UserConstants.ACCOUNT_NOT_EXISTS);

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		Staff staff = staffRepository.getById(StaffUUID);
		UserResponse userResponse = createStaffResponse(staff);

		messageResponse.setData(userResponse);
		messageResponse.setStatus(HttpStatus.OK.value());
		messageResponse.setMessage(UserConstants.UPDATE_SUCCESS);

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

}
