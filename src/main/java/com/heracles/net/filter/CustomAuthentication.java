package com.heracles.net.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.heracles.net.message.AuthMessage;
import com.heracles.net.util.CustomUserDetails;
import com.heracles.net.util.UserDTO;

import static com.heracles.net.util.JwtUtil.generateToken;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthentication extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	public CustomAuthentication(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		log.info("Attempting authentication");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
		return authenticationManager.authenticate(token);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		log.info("Successful authentication");
		response.setContentType(APPLICATION_JSON_VALUE);
		CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
		User user = new User(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
		String token = generateToken(user, new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 5));
		String refreshToken = generateToken(user, new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
		AuthMessage authMessage = new AuthMessage(token, refreshToken, new UserDTO(userDetails.getUser()));
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());	
		response.getWriter().write(mapper.writeValueAsString(authMessage));
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		log.info("Unsuccessful authentication");
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(APPLICATION_JSON_VALUE);
		response.getWriter().write("{\"error\":\"" + failed.getMessage() + "\"}");
	}
}
