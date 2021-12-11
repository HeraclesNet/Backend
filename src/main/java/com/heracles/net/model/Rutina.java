package com.heracles.net.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity(name = "Rutina")
@Table(name = "rutina", schema = "public")
public class Rutina {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;
    @Column(name = "userId", nullable = false)
    private String userId;
    @Column(name = "dayOfWeek")
    private String dayOfWeek;
    @Column(name = "hour")
    private String hour;
    @Column(name = "text")
    private String text;

    public Rutina() {
    }

    public Rutina(String userId, String dayOfWeek, String hour, String text) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.dayOfWeek = dayOfWeek;
        this.hour = hour;
        this.text = text;
    }

}
