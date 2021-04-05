package ru.mechtatell.Models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "account")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String login;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<ClientList> clientLists;
}
