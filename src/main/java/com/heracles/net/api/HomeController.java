package com.heracles.net.api;

import static com.heracles.net.util.JwtUtil.generateToken;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Date;
import java.util.List;
import org.springframework.http.HttpHeaders;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.heracles.net.message.RegisterMessage;
import com.heracles.net.model.FileDB;
import com.heracles.net.model.User;
import com.heracles.net.service.FileStorageService;
import com.heracles.net.service.UserService;
import com.heracles.net.util.UserRegisterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/home")
public class HomeController {
    
    private UserService userService;
    private FileStorageService storageService;
    
    @GetMapping(path = "/users")
    public ResponseEntity<List<User>> getUsers() {
        log.info("All users requested");
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void registerNewUser(@RequestBody UserRegisterDTO user, HttpServletResponse response) throws IOException {
        log.info("New user requested");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            User userToSave = new User(user);
            userService.addNewUser(userToSave);
            String token = generateToken(userToSave, new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 5));
		    String refreshToken = generateToken(userToSave, new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
            response.setHeader(HttpHeaders.LOCATION, uri.toASCIIString());
            response.setStatus(HttpStatus.CREATED.value());
            response.getWriter().write(new ObjectMapper().registerModule(
                    new JavaTimeModule()).writeValueAsString(
                    new RegisterMessage(token, refreshToken, "User registered successfully")));
        } catch (Exception e) {
            log.info("Error registering new user {}", e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(
                new RegisterMessage("", "", e.getMessage())));
        }
        
    }


  
    @GetMapping(value = "/files", produces = "image/*")
    public void getFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        log.info("File requested with id: " + id);
        FileDB fileDB = storageService.getFile(id);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"");
        response.setContentType("image/*");
        response.setContentLength(fileDB.getData().length);
        OutputStream os = response.getOutputStream();
        os.write(fileDB.getData(),0,fileDB.getData().length);
    }
}
