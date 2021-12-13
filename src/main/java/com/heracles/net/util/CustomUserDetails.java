package com.heracles.net.util;

import java.util.Arrays;
import java.util.Collection;

import com.heracles.net.model.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.Data;

@Data
public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private String email;
	private String password;
	private User user;

	public CustomUserDetails(User user) {
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
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
}
