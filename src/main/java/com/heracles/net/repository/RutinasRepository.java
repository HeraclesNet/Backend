package com.heracles.net.repository;
import java.util.List;
import com.heracles.net.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RutinasRepository extends JpaRepository<Rutinas, String> {
    List<Rutinas> findByUserId(String userId);
}
