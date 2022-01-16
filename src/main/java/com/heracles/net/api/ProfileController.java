package com.heracles.net.api;
import com.heracles.net.util.RutinaDTO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.heracles.net.message.ResponseMessage;
import com.heracles.net.repository.RutinasRepository;
import com.heracles.net.service.PostService;
import com.heracles.net.service.RutinasService;
import com.heracles.net.service.UserService;
import com.heracles.net.util.UserProfile;
import static com.heracles.net.util.JwtUtil.verifier;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@Slf4j
@Transactional
@RestController
@AllArgsConstructor
@RequestMapping(path = "/profile")
public class ProfileController {

    private final UserService userService;
    private final PostService postService;
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
        String email = decodedJWT.getSubject();

        UserProfile userProfile = new UserProfile(userService.getUserDTO(email), postService.getUserPost(email));
        log.info(userProfile.toString());
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(userProfile));
    }

    @PostMapping(value="/add/rutina")
    public void AddRutina(HttpServletRequest request, HttpServletResponse response) throws IOException{
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
        List <RutinaDTO> rutinas = new ArrayList<RutinaDTO>();
        
        rutinas.add(mapper.readValue(request.getInputStream(),RutinaDTO.class));
        rutinasService.addNewRutinasToUser(email,rutinas);
    }
}
