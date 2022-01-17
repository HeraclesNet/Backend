package com.heracles.net.api;

import static com.heracles.net.util.JwtUtil.verifier;
import static com.heracles.net.util.JwtUtil.generateToken;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.GATEWAY_TIMEOUT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.heracles.net.message.ResponseMessage;
import com.heracles.net.model.User;
import com.heracles.net.service.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Slf4j
@Transactional
@RestController
@AllArgsConstructor
@RequestMapping(path = "/user")
public class UserController {

    private static final String PAGE_SIZE = "pageSize";
    private static final String PAGE_NUMBER = "pageNumber";
    private static final String INVALID_TOKEN = "Invalid token";
    private static final String NO_TOKEN_PROVIDED = "No token provided";

    private final UserService userService;

    @GetMapping(value = "/home")
    public String homePage() {
        return "<h1>Welcome to the Heracles Network API</h1>";
    }
    
    @PutMapping(value = "/new/fan", produces = APPLICATION_JSON_VALUE)
    public void follow(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Following user");
        String token = request.getHeader(AUTHORIZATION);
        response.setContentType(APPLICATION_JSON_VALUE);
        if (token == null) {
            response.setStatus(FORBIDDEN.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(NO_TOKEN_PROVIDED)));
            return;
        }
        DecodedJWT decodedJWT = verifier(token.substring(7));
        if (decodedJWT == null) {
            response.setStatus(EXPECTATION_FAILED.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(INVALID_TOKEN)));
            return;
        }
        String email = decodedJWT.getSubject();
        String userToFollow = request.getParameter("userToFollow");
        log.info("User {} is following user {}", email, userToFollow);
        try {
            ResponseMessage followUser = userService.followUser(email, userToFollow);
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(followUser));
        } catch (Exception e) {
            log.error("Error following user {}", e.getMessage());
            response.setStatus(EXPECTATION_FAILED.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(e.getMessage())));
        }
    }

    @GetMapping(value = "/fans", produces = APPLICATION_JSON_VALUE)
    public void getFans(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getHeader(AUTHORIZATION);
        response.setContentType(APPLICATION_JSON_VALUE);
        if (token == null) {
            response.setStatus(FORBIDDEN.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(NO_TOKEN_PROVIDED)));
            return;
        }
        DecodedJWT decodedJWT = verifier(token.substring(7));
        if (decodedJWT == null) {
            response.setStatus(EXPECTATION_FAILED.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(INVALID_TOKEN)));
            return;
        }
        String email = decodedJWT.getSubject();
        String pageNumber = request.getParameter(PAGE_NUMBER);
        String pageSize = request.getParameter(PAGE_SIZE);
        try {
            log.info("Getting fans for user {}", email);
            Pageable page = PageRequest.of(pageNumber == null ? 0 : Integer.parseInt(pageNumber),
                    pageSize == null ? 10 : Integer.parseInt(pageSize));
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(new ObjectMapper().registerModule(
                    new JavaTimeModule()).writeValueAsString(userService.getFans(email, page)));
        } catch (Exception e) {
            log.error("Error getting fans {}", e.getMessage());
            response.setStatus(EXPECTATION_FAILED.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(e.getMessage())));
        }
    }
    
    @DeleteMapping(value = "/remove/fan", produces = APPLICATION_JSON_VALUE)
    public void unFollow (HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getHeader(AUTHORIZATION);
        response.setContentType(APPLICATION_JSON_VALUE);
        if (token == null) {
            response.setStatus(FORBIDDEN.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(NO_TOKEN_PROVIDED)));
            return;
        }
        DecodedJWT decodedJWT = verifier(token.substring(7));
        if (decodedJWT == null) {
            response.setStatus(EXPECTATION_FAILED.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(INVALID_TOKEN)));
            return;
        }
        String email = decodedJWT.getSubject();
        String userToUnFollow = request.getParameter("userToUnFollow");
        log.info("User {} is unfollowing user {}", email, userToUnFollow);
        try {
            userService.unFollowUser(email, userToUnFollow);
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage("User unfollowed")));
        } catch (Exception e) {
            log.error("Error unfollowing user {}", e.getMessage());
            response.setStatus(GATEWAY_TIMEOUT.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(e.getMessage())));
        }
    }

    @GetMapping(value = "/token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Refreshing token");
        String authHeader = request.getHeader(AUTHORIZATION);
        response.setContentType(APPLICATION_JSON_VALUE);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String refreshToken = authHeader.substring(7);
            try {
                DecodedJWT decoded = verifier(refreshToken);
                String email = decoded.getSubject();
                User user = userService.findUserByEmail(email);
                String newToken = generateToken(user, new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 5));
                Map<String, String> tokens = new HashMap<>(2);
                tokens.put("token", newToken);
                tokens.put("refreshToken", refreshToken);
                Map<String, String> map = new HashMap<>();
                map.put("token", newToken);
                map.put("refreshToken", refreshToken);
                new ObjectMapper().writeValue(response.getOutputStream(), map);
            } catch (Exception e) {
                log.error("Error while refreshing token", e);
                response.setHeader("Error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> map = new HashMap<>();
                map.put("message", e.getMessage());
                new ObjectMapper().writeValue(response.getOutputStream(), map);
            }
        } else {
            response.setStatus(FORBIDDEN.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(NO_TOKEN_PROVIDED)));
        }
    }

}