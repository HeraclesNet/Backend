package com.heracles.net.api;

import static com.heracles.net.util.JwtUtil.verifier;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heracles.net.message.ResponseMessage;
import com.heracles.net.service.PostService;
import com.heracles.net.service.UserService;
import com.heracles.net.util.PostDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RestController
@AllArgsConstructor
@RequestMapping(path = "/post")
public class PostController {

    private static final String INVALID_TOKEN = "Invalid token";
    private static final String NO_TOKEN_PROVIDED = "No token provided";
	
	private final PostService postService;
	private final UserService userService;

	@GetMapping(value = "/get", produces = APPLICATION_JSON_VALUE)
    public void getPosts(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
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
		int pageNumber = request.getParameter("number") == null ? 0
				: Integer.parseInt(request.getParameter("number"));
		int pageSize = request.getParameter("size") == null ? 10
				: Integer.parseInt(request.getParameter("size"));
		String sortBy = request.getParameter("sort");
		log.info("pageNumber: {}, pageSize: {}, sortBy: {}", pageNumber, pageSize, sortBy);
		Pageable page;
		Page<PostDTO> posts;
		if (sortBy.equals("createdAt")) {
			page = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
			posts = postService.getPosts(false, decodedJWT.getSubject(), page);
		} else if (sortBy.equals("muscles")) {
			page = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
			posts = postService.getPosts(false, decodedJWT.getSubject(), page);
		} else {
			page = PageRequest.of(pageNumber, pageSize);
			posts = postService.getPosts(true, decodedJWT.getSubject(), page);
		}
		response.setStatus(HttpStatus.OK.value());
		response.getWriter().write(new ObjectMapper().writeValueAsString(posts));
	}
	
	@PostMapping(value = "/media/upload", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public void uploadMedia(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
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
		response.setContentType(APPLICATION_JSON_VALUE);
		try {
			log.info("Posting contento for user {}", email);
			MultipartFile file = ((StandardMultipartHttpServletRequest) request).getFile("file");
			response.setStatus(HttpStatus.OK.value());
			response.getWriter().write(new ObjectMapper()
					.writeValueAsString(userService.addPost(email, request.getParameter("content"), file)));
		} catch (Exception e) {
			log.error("Error posting content {}", e.getMessage());
			response.setStatus(EXPECTATION_FAILED.value());
			response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(e.getMessage())));
		}
	}
	
	@PutMapping(value = "/muscle", produces = APPLICATION_JSON_VALUE)
    public void addMuscle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        log.info("Adding muscle");
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
        response.setContentType(APPLICATION_JSON_VALUE);
        String email = decodedJWT.getSubject();
        try {
            log.info("updating post for user {}", email);
            String postId = request.getParameter("postId");
            int muscle = Integer.parseInt(request.getParameter("muscle"));
            postService.upDateMuscle(postId, muscle);
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage("Muscle updated")));
        } catch (Exception e) {
            log.error("Error posting muscle {}", e.getMessage());
            response.setStatus(EXPECTATION_FAILED.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(e.getMessage())));
        }
    }

	@GetMapping(value = "/user")
	public void returnPost(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException, IOException{
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

		String nickName = request.getParameter("nickName"); 		
		List<PostDTO> postOther;

		try {
			if(nickName == null){
				postOther = postService.getUserPost(email, true);
			}else{
				postOther = postService.getUserPost(nickName, false);
			}
			log.info(nickName);
			response.setStatus(HttpStatus.OK.value());
			response.getWriter().write(new ObjectMapper().writeValueAsString(postOther));
		
		} catch (Exception e) {
			log.error("Error getting post {}", e.getMessage());
            response.setStatus(EXPECTATION_FAILED.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(e.getMessage())));
		}	


	}
}
