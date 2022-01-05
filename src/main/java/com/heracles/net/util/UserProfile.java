package com.heracles.net.util;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserProfile {

    private UserDTO user;
    private Page<PostDTO> posts;
      
    
}
