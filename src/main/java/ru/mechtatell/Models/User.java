package ru.mechtatell.Models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "client")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @Column(name = "sum")
    private double sum;

    @ManyToOne
    @JoinColumn(name = "list_id")
    private UserList userList;
}
