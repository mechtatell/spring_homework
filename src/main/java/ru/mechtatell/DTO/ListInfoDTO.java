package ru.mechtatell.DTO;

import lombok.Data;
import ru.mechtatell.Models.UserList;

@Data
public class ListInfoDTO {
    private String name;
    private int count;

    public static ListInfoDTO from(UserList list) {
        ListInfoDTO listDTO = new ListInfoDTO();

        listDTO.setName(list.getName());
        listDTO.setCount(list.getUsers().size());

        return listDTO;
    }
}
