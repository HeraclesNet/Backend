package com.heracles.net.repository;
//import java.util.Optional;

import com.heracles.net.model.*;
//import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RutinaRepository extends JpaRepository<Rutina, String> {
    //Optional<List<Rutina>> findRutinaByiD(String id);
}
