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
    private String start_t;
    private String end_t;
    private String text;



    public RutinaDTO(Rutinas rutinas){
        this.id = rutinas.getId();
        this.start_t = rutinas.getStart_t();
        this.end_t = rutinas.getEnd_t();
        this.text = rutinas.getText();
        this.date = rutinas.getDate();
    }
}
