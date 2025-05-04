package ru.job4j.socialmedia.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User userFrom;

    @ManyToOne
    private User userTo;

    private String content;

    private LocalDateTime create;

}
