package ru.mechtatell.Models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "client_list")
public class ClientList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "clientList", cascade = CascadeType.ALL)
    private List<Client> clients;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
