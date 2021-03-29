package ru.mechtatell.Models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "comparator")
public class UserComparator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "code")
    private String code;
}
