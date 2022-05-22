package project.instagram.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import project.instagram.entity.Client;
import project.instagram.repository.ClientRepository;
import project.instagram.response.PagedResponse;
import project.instagram.response.UserResponse;
import project.instagram.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private ClientRepository clientRepository;
	
	private UserResponse createUserResponse(Client client) {
		return new UserResponse(
				client.getId().toString(),
				client.getFirstName(),
				client.getLastName(),
				client.getEmail(),
				client.getPhone(),
				client.getAddress(),
				client.getBiography(),
				client.isActive(),
				client.getCreatedDate().toString());
	}

	@Override
	public PagedResponse<UserResponse> findAllClients(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Client> clients = clientRepository.findAllByOrderByIdDesc(pageable);
		
		List<UserResponse> userResponses = new ArrayList<UserResponse>(clients.getContent().size());
		
		for (Client client : clients.getContent()) {
			userResponses.add(createUserResponse(client));
		}
		
		return new PagedResponse<>(userResponses, clients.getNumber(), clients.getSize(), clients.getTotalElements(),
				clients.getTotalPages(), clients.isLast());
	}

}
