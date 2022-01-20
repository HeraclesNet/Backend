package com.heracles.net.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.heracles.net.model.Rutinas;
import com.heracles.net.model.User;
import com.heracles.net.repository.RutinasRepository;
import com.heracles.net.repository.UserRepository;
import com.heracles.net.util.RutinaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RutinasService implements RutinasInterfaceService{
    private RutinasRepository rutinasRepository;
    private UserRepository userRepository;

    @Autowired
    public RutinasService(RutinasRepository rutinasRepository,UserRepository userRepository){
        this.rutinasRepository = rutinasRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<RutinaDTO> findAllUserRutinas(String email) {
        User usuario = userRepository.findUserByEmail(email).orElseThrow();
        List<Rutinas> listaDeRutinas = rutinasRepository.findByUserId(usuario.getId());
        return listaDeRutinas.stream().map(RutinaDTO::new).collect(Collectors.toList());
    }

    @Override
    public void addNewRutinasToUser(String email, List<RutinaDTO> listaRutinasDTO) {
        User usuario = userRepository.findUserByEmail(email).orElseThrow();
        String userid = usuario.getId();
        List<Rutinas> listaRutinas = new ArrayList<Rutinas>();
        for(int i = 0;i<listaRutinasDTO.size();i++){
            listaRutinas.add(new Rutinas(userid,listaRutinasDTO.get(i)));
        }
        rutinasRepository.saveAll(listaRutinas);
    }

    @Override

    public void updateRutinasToUser(String email, List<RutinaDTO> listaRutinasDTO) {
        User usuario = userRepository.findUserByEmail(email).orElseThrow();
        String userid = usuario.getId();
        List<Rutinas> listaRutinas = new ArrayList<Rutinas>();
        for(int i = 0;i<listaRutinasDTO.size();i++){
            listaRutinas.add(new Rutinas(listaRutinasDTO.get(i).getId(),userid,listaRutinasDTO.get(i)));
        }
        rutinasRepository.saveAll(listaRutinas);
    }
}
