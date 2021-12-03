package com.heracles.net.util;

import java.util.Date;
import java.util.stream.Collectors;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class JwtUtil {
	private static final String SECRET = "a566ae184cba696418545178427f0755d72a6bfed7528ca1e464e2f8181f4a0d";
	private static final String ISS = "http://www.heracles.com";

	private static final Algorithm ALGORITHM= Algorithm.HMAC256(SECRET);
	private static final JWTVerifier verifier = JWT.require(ALGORITHM).build();

	private JwtUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static String generateToken(User user, Date expirationDate) {
		return createToken(user, expirationDate);
	}

	public static String generateToken(com.heracles.net.model.User user, Date expirationDate) {
		return createToken(user, expirationDate);
	}

	private static String createToken(User user, Date expirationDate) {
		return JWT.create()
				.withIssuer(ISS)
				.withSubject(user.getUsername())
				.withIssuedAt(new Date(System.currentTimeMillis()))
				.withExpiresAt(expirationDate)
				.withClaim("roles",
						user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(ALGORITHM);
	}
	
	private static String createToken(com.heracles.net.model.User user, Date expirationDate) {
		return JWT.create()
				.withIssuer(ISS)
				.withSubject(user.getEmail())
				.withIssuedAt(new Date(System.currentTimeMillis()))
				.withExpiresAt(expirationDate)
				.withArrayClaim("roles", new String[] { "ROLE_USER" })
				.sign(ALGORITHM);
	}

	public static DecodedJWT verifier(String token) throws JWTVerificationException{
		return verifier.verify(token);
	}

}
