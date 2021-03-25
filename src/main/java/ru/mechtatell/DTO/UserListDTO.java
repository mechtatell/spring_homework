package ru.mechtatell.DTO;

import lombok.Data;
import ru.mechtatell.Models.User;
import ru.mechtatell.Models.UserList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserListDTO {
    private String name;
    private List<UserDTO> userList = new ArrayList<>();

    public static UserListDTO from(UserList list) {
        UserListDTO listDTO = new UserListDTO();

        List<UserDTO> userDTOList = list.getUsers().stream()
                .map(UserDTO::from)
                .collect(Collectors.toList());

        listDTO.setName(list.getName());
        listDTO.setUserList(userDTOList);

        return listDTO;
    }

    public UserList toUserList() {
        UserList userList = new UserList();
        userList.setName(name);

        if (!this.userList.isEmpty()) {
            List<User> users = this.userList.stream()
                    .map(UserDTO::toUser)
                    .peek(e -> e.setUserList(userList))
                    .collect(Collectors.toList());

            userList.getUsers().addAll(users);
        }

        return userList;
    }
}
