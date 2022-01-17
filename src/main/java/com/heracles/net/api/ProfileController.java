package com.heracles.net.api;
import static com.heracles.net.util.JwtUtil.verifier;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.heracles.net.message.ResponseMessage;
import com.heracles.net.service.PostService;
import com.heracles.net.service.RutinasService;
import com.heracles.net.service.UserService;
import com.heracles.net.util.ListRutinaDTO;
import com.heracles.net.util.UserProfile;

import org.apache.tomcat.util.json.JSONParser;
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
    public void AddRutina(HttpServletRequest request, HttpServletResponse response) throws IOException, org.apache.tomcat.util.json.ParseException{
        log.info("------------------------------------------------");
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
        JSONPObject reqJson;
        try{
            Reader reqReader = request.getReader();
            JSONParser parser = new JSONParser(reqReader);
            reqJson = (JSONPObject) parser.parse();
            log.info("------------> {}",reqJson.toString());
        }catch(Exception e){
            log.info("pos si hubo un herror {}",e.getMessage());
        }
        String email = decodedJWT.getSubject();
        ListRutinaDTO rutinas;
        
        //rutinas = mapper.readValues(request.getInputStream().toString(),ListRutinaDTO rutinas);

        //rutinas.add(mapper.readValue(,RutinaDTO.class));
        //rutinasService.addNewRutinasToUser(email,rutinas);
    }
}
