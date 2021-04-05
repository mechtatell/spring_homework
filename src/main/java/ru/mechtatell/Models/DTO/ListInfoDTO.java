package ru.mechtatell.Models.DTO;

import lombok.Data;
import ru.mechtatell.Models.ClientList;

@Data
public class ListInfoDTO {
    private String name;
    private int count;

    public static ListInfoDTO from(ClientList list) {
        ListInfoDTO listDTO = new ListInfoDTO();

        listDTO.setName(list.getName());
        listDTO.setCount(list.getClients().size());

        return listDTO;
    }
}
