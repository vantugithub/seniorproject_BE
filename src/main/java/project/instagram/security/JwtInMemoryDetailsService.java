package project.instagram.security;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import project.instagram.common.enums.RoleName;
import project.instagram.entity.Role;
import project.instagram.repository.RoleRepository;

public abstract class JwtInMemoryDetailsService implements UserDetailsService {

	@Autowired
	private RoleRepository roleRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
    	Role role = roleRepository.findByName(RoleName.ROLE_CLIENT);
    	
    	return createJwtDetails(email, role);
    }
    
    protected abstract UserDetails createJwtDetails(String email, Role role);

}
