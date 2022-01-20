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
    @Column(name = "start", nullable = false)
    private String start;
    @Column(name = "end_t", nullable = false)
    private String end_t;
    @Column(name = "text")
    private String text;
    @Column(name = "back_color")
    private String back_color;
    @Column(name = "border_color")
    private String border_color;

    public Rutinas() {
    }


    public Rutinas(String userId, RutinaDTO rutinaDTO) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.start = rutinaDTO.getStart();
        this.end_t = rutinaDTO.getEnd_t();
        this.text = rutinaDTO.getText();
        this.back_color = rutinaDTO.getBack_color();
        this.border_color = rutinaDTO.getBorder_color();
    }

    public Rutinas(String id,String userId, RutinaDTO rutinaDTO) {
        this.id = id;
        this.userId = userId;
        this.start = rutinaDTO.getStart();
        this.end_t = rutinaDTO.getEnd_t();
        this.text = rutinaDTO.getText();
        this.back_color = rutinaDTO.getBack_color();
        this.border_color = rutinaDTO.getBorder_color();
    }

}
