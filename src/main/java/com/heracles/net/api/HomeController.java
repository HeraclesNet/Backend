package com.heracles.net.api;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpHeaders;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heracles.net.model.FileDB;
import com.heracles.net.model.User;
import com.heracles.net.service.FileStorageService;
import com.heracles.net.service.UserService;
import com.heracles.net.util.UserRegisterDTO;
import com.heracles.net.util.UserUpdateDTO;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping(path = "/update")
    public ResponseEntity<String> UpdateUser(@RequestBody UserUpdateDTO user) {
        log.info("Update user requested");
        try{
            userService.EditUserExtraData(user.getEmail(), user.getKey(), user.getValue());
        } catch(Exception e){
            return ResponseEntity.badRequest().body("Los datos no se pudieron alterar");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Alteraditos");
    }

    @Autowired
    private FileStorageService storageService;
  
    @GetMapping(value = "/files", produces = MediaType.IMAGE_JPEG_VALUE)
    public void getFile(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException, IOException {
      String id = request.getParameter("id");
      log.info("ZZZZZZZZZZZZZZZZZZZZZZZ");
      FileDB fileDB = storageService.getFile(id);
      response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"");
      response.setContentType(MediaType.IMAGE_JPEG_VALUE);
      response.setContentLength(fileDB.getData().length);
      OutputStream filecosas = response.getOutputStream();
      filecosas.write(fileDB.getData(),0,fileDB.getData().length);
  /*     response.getWriter().write(new ObjectMapper().writeValueAsString(fileDB.getData())); */
    }
}
