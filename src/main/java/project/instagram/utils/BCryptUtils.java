package project.instagram.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptUtils {
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}
	
	public String bcryptEncoder(String a) {
		return passwordEncoder.encode(a);
	}
	
	public boolean compare(String a, String b) {
		return passwordEncoder.matches(a, b);
	}
}
