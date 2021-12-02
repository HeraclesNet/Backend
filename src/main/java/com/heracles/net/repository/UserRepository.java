package com.heracles.net.repository;
import com.heracles.net.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String>{

/*     @Query("SELECT s FROM users s WHERE s.email = ?1")*/
    Optional<User> findUserByEmail(String email); 
    Optional<User> findUserByNickName(String nickName);
}
