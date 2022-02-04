package com.heracles.net.api;
import static com.heracles.net.util.JwtUtil.verifier;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.heracles.net.message.ProfileMessage;
import com.heracles.net.message.ResponseMessage;
import com.heracles.net.model.User;
import com.heracles.net.repository.UserRepository;
import com.heracles.net.service.RutinasService;
import com.heracles.net.service.UserService;
import com.heracles.net.util.RutinaDTO;
import com.heracles.net.util.UserDTO;
import com.heracles.net.util.UserUpdateDTO;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
@Transactional
@RestController
@AllArgsConstructor
@RequestMapping(path = "/profile")
public class ProfileController {

    private final UserService userService;
    private final RutinasService rutinasService;
    private static final String INVALID_TOKEN = "Invalid token";
    private static final String NO_TOKEN_PROVIDED = "No token provided"; 
    
    @GetMapping(value = "/user")
    public void profile(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType(APPLICATION_JSON_VALUE);
        String token = request.getHeader(AUTHORIZATION);
        if (token == null) {
            response.setStatus(FORBIDDEN.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(NO_TOKEN_PROVIDED)));
            return;
        }
        DecodedJWT decodedJWT = verifier(token.substring(7));
        if (decodedJWT == null) {
            response.setStatus(EXPECTATION_FAILED.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(INVALID_TOKEN)));
            return;
        }
        
        String nickName = request.getParameter("nickName");
        String email = decodedJWT.getSubject();
        UserDTO other = userService.getUserDTO(nickName);

        log.info(other.toString());
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(new ObjectMapper().registerModule(
            new JavaTimeModule()).writeValueAsString(
                new ProfileMessage(other, userService.isFollowing(nickName, email), userService.isPrivateProfile(nickName))));
    }

    @GetMapping(value="/get/rutina")
    public void getRutinas(HttpServletRequest request, HttpServletResponse response) throws IOException, org.apache.tomcat.util.json.ParseException{
        response.setContentType(APPLICATION_JSON_VALUE);
        String token = request.getHeader(AUTHORIZATION);
        if (token == null) {
            response.setStatus(FORBIDDEN.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(NO_TOKEN_PROVIDED)));
            return;
        }
        DecodedJWT decodedJWT = verifier(token.substring(7));
        if (decodedJWT == null) {
            response.setStatus(EXPECTATION_FAILED.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(INVALID_TOKEN)));
            return;
        }
        String email = decodedJWT.getSubject();
        try {
            log.info("Getting fans for user {}", email);
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(rutinasService.findAllUserRutinas(email)));
        } catch (Exception e) {
            log.error("Error getting fans {}", e.getMessage());
            response.setStatus(EXPECTATION_FAILED.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(e.getMessage())));
        }
    }

    @GetMapping(value="/get/rutina/nickName")
    public void getRutinasByNickName(HttpServletRequest request, HttpServletResponse response) throws IOException, org.apache.tomcat.util.json.ParseException{
        response.setContentType(APPLICATION_JSON_VALUE);
        String token = request.getHeader(AUTHORIZATION);
        User user;
        String nickName = request.getParameter("nickName");
        String email;
        if (token == null) {
            response.setStatus(FORBIDDEN.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(NO_TOKEN_PROVIDED)));
            return;
        }
        DecodedJWT decodedJWT = verifier(token.substring(7));
        if (decodedJWT == null) {
            response.setStatus(EXPECTATION_FAILED.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(INVALID_TOKEN)));
            return;
        }
        user = userService.findUserByNickName(nickName);
        email = user.getEmail();
        try {
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(rutinasService.findAllUserRutinas(email)));
        } catch (Exception e) {
            log.error("Error getting fans {}", e.getMessage());
            response.setStatus(EXPECTATION_FAILED.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(e.getMessage())));
        }
    }

    @PostMapping(value="/add/rutina")
    public void AddRutina(HttpServletRequest request, HttpServletResponse response) throws IOException, org.apache.tomcat.util.json.ParseException{
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType(APPLICATION_JSON_VALUE);
        String token = request.getHeader(AUTHORIZATION);
        if (token == null) {
            response.setStatus(FORBIDDEN.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(NO_TOKEN_PROVIDED)));
            return;
        }
        DecodedJWT decodedJWT = verifier(token.substring(7));
        if (decodedJWT == null) {
            response.setStatus(EXPECTATION_FAILED.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(INVALID_TOKEN)));
            return;
        }
        String email = decodedJWT.getSubject();
        List<RutinaDTO> rutinasDTO = mapper.readValue(request.getInputStream(),new TypeReference<List<RutinaDTO>>(){});
        rutinasService.addNewRutinasToUser(email,rutinasDTO);
    }

    @PostMapping(path = "/update/user")
    public void UpdateUser(HttpServletRequest request, HttpServletResponse response) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType(APPLICATION_JSON_VALUE);
        String token = request.getHeader(AUTHORIZATION);
        if (token == null) {
            response.setStatus(FORBIDDEN.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(NO_TOKEN_PROVIDED)));
            return;
        }
        DecodedJWT decodedJWT = verifier(token.substring(7));
        if (decodedJWT == null) {
            response.setStatus(EXPECTATION_FAILED.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(INVALID_TOKEN)));
            return;
        }
        String email = decodedJWT.getSubject();
        UserUpdateDTO userUpdateDTO = mapper.readValue(request.getInputStream(),UserUpdateDTO.class);
        userService.EditUserExtraData(email,userUpdateDTO);
    }

    @PostMapping(path = "/update/rutinas")
    public void UpdateRutinas(HttpServletRequest request, HttpServletResponse response) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType(APPLICATION_JSON_VALUE);
        String token = request.getHeader(AUTHORIZATION);
        if (token == null) {
            response.setStatus(FORBIDDEN.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(NO_TOKEN_PROVIDED)));
            return;
        }
        DecodedJWT decodedJWT = verifier(token.substring(7));
        if (decodedJWT == null) {
            response.setStatus(EXPECTATION_FAILED.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(INVALID_TOKEN)));
            return;
        }
        String email = decodedJWT.getSubject();
        List<RutinaDTO> rutinasDTO = mapper.readValue(request.getInputStream(),new TypeReference<List<RutinaDTO>>(){});
        rutinasService.updateRutinasToUser(email,rutinasDTO);
    }
}