package project.instagram.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import project.instagram.entity.Role;
import project.instagram.entity.Staff;
import project.instagram.repository.StaffRepository;

@Service
public class JwtInMemoryStaffDetailsService extends JwtInMemoryDetailsService {
	
	@Autowired
	private StaffRepository staffRepository;

	@Override
	protected UserDetails createJwtDetails(String email, Role role) {
		Staff staff = staffRepository.findByEmailAndActiveTrueAndRoleNot(email, role)
    			.orElseThrow(() -> 
    				new UsernameNotFoundException("User Not Found with -> email : " + email));
		
		return JwtDetails.build(staff);
	}

}
