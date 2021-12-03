package com.heracles.net.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.heracles.net.util.JwtUtil.verifier;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthorization extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (request.getServletPath().equals("/login")) {
			filterChain.doFilter(request, response);
		} else {
			String authHeader = request.getHeader(AUTHORIZATION);
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				String authToken = authHeader.substring(7);
				try {
					DecodedJWT decoded = verifier(authToken);
					String email = decoded.getSubject();
					String[] roles = decoded.getClaim("roles").asArray(String.class);
					Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
					Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(auth);
				} catch (JWTVerificationException e) {
					log.error("Invalid token {}", authToken);
					response.setHeader("Error", e.getMessage());
					response.setStatus(FORBIDDEN.value());
					response.setContentType(APPLICATION_JSON_VALUE);
					Map<String, String> map = new HashMap<>();
					map.put("message", e.getMessage());
					new ObjectMapper().writeValue(response.getOutputStream(), map);
				} finally {
					filterChain.doFilter(request, response);
				}
			} else {
				filterChain.doFilter(request, response);
			}
		}
	}
}
