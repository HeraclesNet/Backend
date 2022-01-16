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
    @Column(name = "userId", nullable = false)
    private String userId;
    @Column(name = "dayOfWeek")
    private int dayOfWeek;
    @Column(name = "hour")
    private String hour;
    @Column(name = "text")
    private String text;

    public Rutinas() {
    }

    public Rutinas(String userId, int dayOfWeek, String hour, String text) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.dayOfWeek = dayOfWeek;
        this.hour = hour;
        this.text = text;
    }

    public Rutinas(String userId, RutinaDTO rutinaDTO) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.dayOfWeek = rutinaDTO.getDayOfWeek();
        this.hour = rutinaDTO.getHour();
        this.text = rutinaDTO.getText();
    }


    

}
