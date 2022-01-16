package com.heracles.net.util;
import com.heracles.net.model.Rutinas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RutinaDTO {

    private int dayOfWeek;
    private String hour;
    private String text;

    public RutinaDTO(Rutinas rutinas){
        this.dayOfWeek = rutinas.getDayOfWeek();
        this.hour = rutinas.getHour();
        this.text = rutinas.getText();
    }
}
