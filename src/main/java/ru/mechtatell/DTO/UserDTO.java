package ru.mechtatell.DTO;

import lombok.Data;
import ru.mechtatell.Models.User;

@Data
public class UserDTO {
    private String name;
    private int age;
    private double sum;

    public static UserDTO from(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setName(user.getName());
        userDTO.setAge(user.getAge());
        userDTO.setSum(user.getSum());

        return  userDTO;
    }

    public User toUser() {
        User user = new User();

        user.setName(name);
        user.setAge(age);
        user.setSum(sum);

        return user;
    }
}
