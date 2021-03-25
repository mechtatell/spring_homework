package ru.mechtatell.Models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class UserList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "userList", cascade = CascadeType.ALL)
    private List<User> users;
}
