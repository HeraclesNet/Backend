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
    private String start;
    private String end_t;
    private String text;
    private String back_color;
    private String border_color;


    public RutinaDTO(Rutinas rutinas){
        this.id = rutinas.getId();
        this.start = rutinas.getStart();
        this.end_t = rutinas.getEnd_t();
        this.text = rutinas.getText();
        this.back_color = rutinas.getBack_color();
        this.border_color = rutinas.getBorder_color();
    }
}
