package com.heracles.net.api;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpHeaders;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.heracles.net.model.FileDB;
import com.heracles.net.model.User;
import com.heracles.net.service.FileStorageService;
import com.heracles.net.service.UserService;
import com.heracles.net.util.UserRegisterDTO;
import com.heracles.net.util.UserUpdateDTO;
import org.springframework.http.HttpStatus;
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

    @PostMapping(path = "/register")
    public ResponseEntity<String> registerNewUser(@RequestBody UserRegisterDTO user) {
        log.info("New user requested");
        try {
            User userToSave = new User(user.getName(), user.getDateOfBirth(), user.getEmail(), user.getNickName(),
                    user.getPassword(), 0.0f, 0.0f,false, true);
            userService.addNewUser(userToSave);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Email o nickName repetido");
        }
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/register").toUriString());
        
        return ResponseEntity.created(uri).body("El usuario a sido agregado");
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
