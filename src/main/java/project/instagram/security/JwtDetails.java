package project.instagram.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import project.instagram.entity.Client;
import project.instagram.entity.Staff;

public class JwtDetails implements UserDetails {

		private static final long serialVersionUID = 1L;

		private UUID id;

	    private String firstName;

	    private String email;

	    @JsonIgnore
	    private String password;

	    @SuppressWarnings("rawtypes")
		private Collection authorities;

	    @SuppressWarnings("rawtypes")
		public JwtDetails(UUID id, String firstName, String email, String password, 
				    		Collection authorities) {
	        this.id = id;
	        this.firstName = firstName;
	        this.email = email;
	        this.password = password;
	        this.authorities = authorities;
	    }

		public static JwtDetails build(Client client) {
	    	SimpleGrantedAuthority authority = new SimpleGrantedAuthority(client.getRole().getName().name());
			List<SimpleGrantedAuthority> authorities = new ArrayList<>();
	        authorities.add(authority);

	        return new JwtDetails(
	        		client.getId(),
	        		client.getFirstName(),
	        		client.getEmail(),
	        		client.getPassword(),
	                authorities
	        );
	    }
	    
	    public static JwtDetails build(Staff staff) {
	    	SimpleGrantedAuthority authority = new SimpleGrantedAuthority(staff.getRole().getName().name());
	        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
	        authorities.add(authority);

	        return new JwtDetails(
	        		staff.getId(),
	        		staff.getFirstName(),
	        		staff.getEmail(),
	        		staff.getPassword(),
	                authorities
	        );
	    }

	    public UUID getId() {
	        return id;
	    }

	    public String getFirstName() {
	        return firstName;
	    }

	    public String getEmail() {
	        return email;
	    }

	    @Override
	    public String getUsername() {
	        return firstName;
	    }

	    @Override
	    public String getPassword() {
	        return password;
	    }

	    @SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
	    public Collection getAuthorities() {
	        return authorities;
	    }

	    @Override
	    public boolean isAccountNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isAccountNonLocked() {
	        return true;
	    }

	    @Override
	    public boolean isCredentialsNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isEnabled() {
	        return true;
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        
	        JwtDetails user = (JwtDetails) o;
	        return Objects.equals(id, user.id);
	    }
	}

