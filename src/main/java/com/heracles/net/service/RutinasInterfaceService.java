package com.heracles.net.service;
import java.util.List;
import com.heracles.net.model.Rutinas;
import com.heracles.net.util.RutinaDTO;

public interface RutinasInterfaceService {

    public List<RutinaDTO> findAllUserRutinas(String email);
    public void addNewRutinasToUser(String email,List<RutinaDTO> listaRutinas);
    public void updateRutinasToUser(String email,List<Rutinas> listaRutinas);
    
}
