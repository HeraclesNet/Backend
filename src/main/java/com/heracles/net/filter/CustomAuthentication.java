package com.heracles.net.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heracles.net.util.CustomUserDetails;
import com.heracles.net.util.UserLoginDTO;

import static com.heracles.net.util.JwtUtil.generateToken;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
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
		log.info("email: " + email + " password: " + password);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
		return authenticationManager.authenticate(token);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		log.info("Successful authentication");
		CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
		User user = new User(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
		String token = generateToken(user, new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 5));
		String refreshToken = generateToken(user, new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
		Map<String, String> tokens = new HashMap<>(2);
		UserLoginDTO userData = new UserLoginDTO(userDetails.getUser());
		tokens.put("token", token);
		tokens.put("refreshToken", refreshToken);
		tokens.put("userData", userData.toString());
		response.setContentType(APPLICATION_JSON_VALUE);
		new ObjectMapper().writeValue(response.getOutputStream() , tokens);
	}
}
