package com.heracles.net.api;

import java.net.URI;
import java.util.List;

import com.heracles.net.model.User;
import com.heracles.net.service.UserService;
import com.heracles.net.util.UserRegisterDTO;
import com.heracles.net.util.UserUpdateDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
@RequestMapping(path = "/home")
@AllArgsConstructor
@Slf4j
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
}
