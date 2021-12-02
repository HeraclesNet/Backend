package com.heracles.net.api;

import com.heracles.net.dto.UserRegisterDTO;
import com.heracles.net.model.*;
import com.heracles.net.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;
import java.util.*;

@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RestController
@RequestMapping(path = "/")
public class UserController {
    
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping(path = "/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Object> getUser(@RequestBody Map<String,Object> map) {
        User user = null;
        String email = map.get("email").toString();
        String password = map.get("password").toString();
        try{
            user = userService.getUserLogin(email,password);
        } catch (Exception i) {
            if(i.getMessage().equals("Not found"))
                return ResponseEntity.notFound().build();
            else
            
               return ResponseEntity.badRequest().body(i.getMessage());
        }
        return ResponseEntity.ok().body(user);
    }


    @PostMapping(path = "/register")
    public ResponseEntity<String> registerNewUser(@RequestBody UserRegisterDTO user){
        try {
            User userToSave = new User(user.getName(), user.getDateOfBirth(), user.getEmail(), user.getNickName(), user.getPassword(), 0.0f, 0.0f);
            userService.addNewUser(userToSave);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Email o nickName repetido");
        }
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/register").toUriString());
        return ResponseEntity.created(uri).body("El usuario a sido agregado");
    }
}