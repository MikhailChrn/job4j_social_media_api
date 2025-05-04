package ru.job4j.socialmedia.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private User subscribeTo;

    private LocalDateTime create;

}
