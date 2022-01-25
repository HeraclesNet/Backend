package com.heracles.net.repository;
import java.util.List;
import com.heracles.net.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RutinasRepository extends JpaRepository<Rutinas, String> {
    List<Rutinas> findByUserId(String userId);

    @Query(value = "DELETE FROM rutinas",nativeQuery =  true)
    void deleteByUserId(String user_id);
    
}
