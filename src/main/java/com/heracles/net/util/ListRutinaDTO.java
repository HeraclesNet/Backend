package com.heracles.net.util;

import java.util.List;

import lombok.Data;

@Data
public class ListRutinaDTO {
    private List <RutinaDTO> listRutinaDTO;
    public ListRutinaDTO(List <RutinaDTO> listRutinaDTO){
        this.listRutinaDTO = listRutinaDTO;
    }

    public void setListRutinaDTO(List<RutinaDTO> list){
        this.listRutinaDTO = list;
    }

    @Override
    public String toString(){
        return "ListRutinaDTO{"+
                "listRutinaDTO=" + listRutinaDTO +'}';
    }
}
