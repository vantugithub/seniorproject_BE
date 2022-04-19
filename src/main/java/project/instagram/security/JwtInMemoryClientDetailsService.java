package project.instagram.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import project.instagram.entity.Client;
import project.instagram.entity.Role;
import project.instagram.repository.ClientRepository;

@Service
public class JwtInMemoryClientDetailsService extends JwtInMemoryDetailsService {

	@Autowired
    private ClientRepository clientRepository;

	@Override
	protected UserDetails createJwtDetails(String email, Role role) {
		Client client = clientRepository.findByEmailAndActiveTrueAndRole(email, role)
    			.orElseThrow(() -> 
    				new UsernameNotFoundException("User Not Found with -> email : " + email));
		
		return JwtDetails.build(client);
	}
    
}


