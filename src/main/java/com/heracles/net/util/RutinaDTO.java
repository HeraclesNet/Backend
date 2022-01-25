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
    private String startt;
    private String ent;
    private String text;



    public RutinaDTO(Rutinas rutinas) {
        this.id = rutinas.getId();
        this.startt = rutinas.getStartt();
        this.ent = rutinas.getEnt();
        this.text = rutinas.getText();
        this.date = rutinas.getDate();
    }

    @Override
    public String toString() {
        return "date: " + this.getDate() + "\n" +
                "ent: " + this.getEnt() + "\n" + 
                "startt" + this.getStartt() + "\n" +
                "text: " + this.getText() + "\n";
    }
}
