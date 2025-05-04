package ru.job4j.socialmedia.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User user1;

    @ManyToOne
    private User user2;

    private LocalDateTime create;

}
