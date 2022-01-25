package com.heracles.net.util;
import com.heracles.net.model.Rutinas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RutinaDTO {
    private String id;
    private String date;
    private String startT;
    private String endT;
    private String text;



    public RutinaDTO(Rutinas rutinas){
        this.id = rutinas.getId();
        this.startT = rutinas.getStartT();
        this.endT = rutinas.getEndT();
        this.text = rutinas.getText();
        this.date = rutinas.getDate();
    }
}
