package com.heracles.net.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.heracles.net.util.RutinaDTO;

import lombok.Data;

@Data
@Entity(name = "Rutinas")
@Table(name = "rutinas", schema = "public")
public class Rutinas {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;
    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name = "date")
    private String date;
    @Column(name = "starT", nullable = false)
    private String startT;
    @Column(name = "endT", nullable = false)
    private String endT;
    @Column(name = "text")
    private String text;
    public Rutinas() {
    }


    public Rutinas(String userId, RutinaDTO rutinaDTO) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.startT = rutinaDTO.getStartT();
        this.endT = rutinaDTO.getEndT();
        this.text = rutinaDTO.getText();
        this.date = rutinaDTO.getDate();
    }

    public Rutinas(String id,String userId, RutinaDTO rutinaDTO) {
        this.id = id;
        this.userId = userId;

        this.startT = rutinaDTO.getStartT();
        this.endT = rutinaDTO.getEndT();
        this.text = rutinaDTO.getText();
        this.date = rutinaDTO.getDate();
    }

}
