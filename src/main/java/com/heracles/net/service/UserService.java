
package com.heracles.net.service;

import java.util.*;
import com.heracles.net.model.*;
import com.heracles.net.repository.*;
import com.heracles.net.util.CustomUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService implements UserDetailsService, UserInterfaceService {

    private final UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsers() {
        log.info("Getting all users");
        return userRepository.findAll();
    }

    @Override
    public User getUserLogin(String email, String password) throws Exception {
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (!userOptional.isPresent()) {
            throw new Exception("Not found");
        }
        User user = userOptional.get();
        if (!user.getPassword().equals(password)) {
            throw new Exception("Wrong Password");
        }
        return user;
    }

    @Override
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(userOptional.get());
    }

    @Override
    public User findUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public void EditUserExtraData(String email,String key, String value) throws Exception{
        User user = userRepository.findUserByEmail(email).get();
        if(key.equals("Height")){
            user.setHeight(Float.parseFloat(value));
        }
        if(key.equals("Weight")){
            user.setWeight(Float.parseFloat(value));
        }
        if(key.equals("Gender")){
            user.setGender(Boolean.parseBoolean(value));
        }
        userRepository.save(user);
    }

    @Override
    public void EditUserImportantData() throws Exception{
    }
}
