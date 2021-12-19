
package com.heracles.net.service;

import java.io.IOException;
import java.util.*;

import com.heracles.net.message.ResponseMessage;
import com.heracles.net.model.*;
import com.heracles.net.repository.*;
import com.heracles.net.util.CustomUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UserService implements UserDetailsService, UserInterfaceService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FileDBRepository fileDBRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PostRepository postRepository,
            FileDBRepository fileDBRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.fileDBRepository = fileDBRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public List<User> getUsers() {
        log.info("Getting all users");
        return userRepository.findAll();
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
            log.error("User email {} not found", email);
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(userOptional.get());
    }

    @Override
    public User findUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public void EditUserExtraData(String email, String key, String value) throws Exception {
        User user = userRepository.findUserByEmail(email).orElseThrow();
        if (key.equals("Height")) {
            user.setHeight(Float.parseFloat(value));
        }
        if (key.equals("Weight")) {
            user.setWeight(Float.parseFloat(value));
        }
        if (key.equals("Gender")) {
            user.setGender(Boolean.parseBoolean(value));
        }
        userRepository.save(user);
    }

    @Override
    public void EditUserImportantData() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public ResponseMessage addPost(String email, String content, MultipartFile file) throws UsernameNotFoundException, IOException{
        User user = userRepository.findUserByEmail(email).orElseThrow();
        ResponseMessage responseMessage = new ResponseMessage();
        if (file == null) {
            postRepository.save(new AppPost(content, user));
            responseMessage.setMessage("OnlyText");
        } else {
            AppPost post = new AppPost(content, user);
            FileDB fileDB = new FileDB(file, post);
            postRepository.save(post);
            fileDBRepository.save(fileDB);
            responseMessage.setMessage(fileDB.getPathUrl());
        }
        return responseMessage;
    }
}
