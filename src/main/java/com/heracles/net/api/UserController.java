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
import com.heracles.net.message.ResponseMessage;
import com.heracles.net.model.User;
import com.heracles.net.service.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Transactional
@RestController
@AllArgsConstructor
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/home")
    public String homePage() {
        return "<h1>Welcome to the Heracles Network API</h1>";
    }

    @PostMapping(value = "/media/upload", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public void uploadMedia(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String token = request.getHeader(AUTHORIZATION);
        DecodedJWT decodedJWT = verifier(token.substring(7));
        String email = decodedJWT.getSubject();
        response.setContentType(APPLICATION_JSON_VALUE);
        try {
            log.info("Posting contento for user {}", email);
            MultipartFile file = ((StandardMultipartHttpServletRequest) request).getFile("file");
            ResponseMessage responseMessage = userService.addPost(email, request.getParameter("content"),
                    Integer.parseInt(request.getParameter("muscles")), file);
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseMessage));
        } catch (Exception e) {
            response.setStatus(EXPECTATION_FAILED.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(e.getMessage())));
        }
    }

    @GetMapping(value = "/token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Refreshing token");
        String authHeader = request.getHeader(AUTHORIZATION);
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
                response.setContentType(APPLICATION_JSON_VALUE);
                Map<String, String> map = new HashMap<>();
                map.put("token", newToken);
                map.put("refreshToken", refreshToken);
                new ObjectMapper().writeValue(response.getOutputStream(), map);
            } catch (Exception e) {
                log.error("Error while refreshing token", e);
                response.setHeader("Error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                Map<String, String> map = new HashMap<>();
                map.put("message", e.getMessage());
                new ObjectMapper().writeValue(response.getOutputStream(), map);
            }
        }
    }

}