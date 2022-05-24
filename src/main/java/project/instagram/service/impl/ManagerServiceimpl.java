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

import project.instagram.common.enums.constants.UserConstants;
import project.instagram.entity.Client;
import project.instagram.repository.ClientRepository;
import project.instagram.response.MessageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.response.UserResponse;
import project.instagram.service.ManagerService;

@Service
public class ManagerServiceimpl implements ManagerService {
	
	@Autowired
	private ClientRepository clientRepository;
	
	private UserResponse createClientResponse(Client client) {

		return new UserResponse(client.getId().toString(), client.getFirstName(), client.getLastName(), client.getEmail(),
				client.getPhone(), client.getAddress(), client.isActive(),
				client.getCreatedDate() == null ? "" : client.getCreatedDate().toString());
	}
	
	private boolean isClientExists(UUID staffUUID) {

		Optional<Client> client = clientRepository.findById(staffUUID);

		if (client.isEmpty()) {
			return false;
		}
		return true;
	}
	
	private UserResponse createUserResponse(Client client) {

		return new UserResponse(client.getId().toString(), client.getFirstName(), client.getLastName(),
				client.getEmail(), client.getPhone(), client.getAddress(), client.getBiography(), client.isActive(),
				client.getCreatedDate().toString());
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
	public ResponseEntity<MessageResponse> updateActiveClient(String clientId) {
		MessageResponse messageResponse = new MessageResponse();
		UUID clientUUID = UUID.fromString(clientId);
		if (!isClientExists(clientUUID)) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			messageResponse.setMessage(UserConstants.ACCOUNT_NOT_EXISTS);

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		Client client = clientRepository.getById(clientUUID);
		client.setActive(!client.isActive());
		clientRepository.save(client);
		
		messageResponse.setStatus(HttpStatus.OK.value());
		messageResponse.setMessage(UserConstants.UPDATE_SUCCESS);

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

	@Override
	public ResponseEntity<MessageResponse> getClient(String clientId) {
		MessageResponse messageResponse = new MessageResponse();
		UUID clientUUID = UUID.fromString(clientId);
		if (!isClientExists(clientUUID)) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			messageResponse.setMessage(UserConstants.ACCOUNT_NOT_EXISTS);

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		Client client = clientRepository.getById(clientUUID);
		UserResponse userResponse = createClientResponse(client);

		messageResponse.setData(userResponse);
		messageResponse.setStatus(HttpStatus.OK.value());
		messageResponse.setMessage(UserConstants.UPDATE_SUCCESS);

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}
	
}
