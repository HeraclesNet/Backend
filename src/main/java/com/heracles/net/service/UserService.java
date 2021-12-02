
package com.heracles.net.service;

import java.util.*;
import com.heracles.net.repository.*;
import com.heracles.net.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@Service
public class UserService {


    private final UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository){
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.userRepository = userRepository;
    }
    


    public List<User> getUsers() {
        log.info("Getting all users");
        return userRepository.findAll();
    }

    public User getUserLogin(String email, String password) throws Exception {
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (!userOptional.isPresent()) {
            throw new Exception("Not found");
        }
        User user = userOptional.get();
        if(!user.getPassword().equals(password)){
            throw new Exception("Wrong Password");
        }
        return user;
    }

    public void addNewUser(User user) throws Exception {
        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            log.error("User already exists");
            throw new Exception("BadRequest");
        }
        userOptional = userRepository.findUserByNickName(user.getNickName());
        if (userOptional.isPresent()) {
            log.error("NickName already exists {}", user.getNickName());
            throw new Exception("BadRequest");
        }
        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }
}
