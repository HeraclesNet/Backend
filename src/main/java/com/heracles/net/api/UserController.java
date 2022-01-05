package com.heracles.net.api;

import static com.heracles.net.util.JwtUtil.verifier;
import static com.heracles.net.util.JwtUtil.generateToken;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.heracles.net.message.ResponseMessage;
import com.heracles.net.model.User;
import com.heracles.net.service.*;
import com.heracles.net.util.PostDTO;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Slf4j
@Transactional
@RestController
@AllArgsConstructor
@RequestMapping(path = "/user")
public class UserController {

    private static final String INVALID_TOKEN = "Invalid token";
    private static final String NO_TOKEN_PROVIDED = "No token provided";

    private final UserService userService;
    private final PostService postService;

    @GetMapping(value = "/home")
    public String homePage() {
        return "<h1>Welcome to the Heracles Network API</h1>";
    }

    @GetMapping(value = "/posts", produces = APPLICATION_JSON_VALUE)
    public void getPosts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        String token = request.getHeader(AUTHORIZATION);
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
        log.info("User {} is requesting posts", decodedJWT.getSubject());
        Pageable page = PageRequest.of(request.getParameter("pageNumber") == null ? 0 : Integer.parseInt(request.getParameter("pageNumber")),
                request.getParameter("pageSize") == null ? 10 : Integer.parseInt(request.getParameter("pageSize")));
        Page<PostDTO> posts = postService.getPosts(page);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(new ObjectMapper().writeValueAsString(posts));
    }

    @PostMapping(value = "/media/upload", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public void uploadMedia(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String token = request.getHeader(AUTHORIZATION);
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
        response.setContentType(APPLICATION_JSON_VALUE);
        try {
            log.info("Posting contento for user {}", email);
            MultipartFile file = ((StandardMultipartHttpServletRequest) request).getFile("file");
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(new ObjectMapper()
                    .writeValueAsString(userService.addPost(email, request.getParameter("content"), file)));
        } catch (Exception e) {
            log.error("Error posting content {}", e.getMessage());
            response.setStatus(EXPECTATION_FAILED.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(e.getMessage())));
        }
    }
    
    @PutMapping(value = "/fan", produces = APPLICATION_JSON_VALUE)
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
        try {
            log.info("Getting fans for user {}", email);
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(userService.getFans(email)));
        } catch (Exception e) {
            log.error("Error getting fans {}", e.getMessage());
            response.setStatus(EXPECTATION_FAILED.value());
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