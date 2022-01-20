package com.heracles.net.util;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UserUpdateDTO {
    //private LocalDate dateOfBirth;
    private float weight;
    private float height;
    private boolean gender;
    private boolean visibility;

    public UserUpdateDTO(LocalDate dateOfBirth,float weight,float height,boolean gender,boolean visibility){
        //this.dateOfBirth = dateOfBirth;
        this.weight = weight;
        this.height = height;
        this.gender = gender;
        this.visibility = visibility;
    }

    public UserUpdateDTO(){
    }
}
