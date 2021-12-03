package com.heracles.net.api;

import java.net.URI;
import java.util.List;

import com.heracles.net.model.User;
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

@RestController
@RequestMapping(path = "/home")
@AllArgsConstructor
public class HomeController {

    private UserService userService;

    @GetMapping(path = "/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping(path = "/register")
    public ResponseEntity<String> registerNewUser(@RequestBody UserRegisterDTO user) {
        try {
            User userToSave = new User(user.getName(), user.getDateOfBirth(), user.getEmail(), user.getNickName(),
                    user.getPassword(), 0.0f, 0.0f);
            userService.addNewUser(userToSave);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Email o nickName repetido");
        }
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/register").toUriString());
        return ResponseEntity.created(uri).body("El usuario a sido agregado");
    }
}
