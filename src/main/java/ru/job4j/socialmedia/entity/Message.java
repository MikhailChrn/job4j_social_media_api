package ru.job4j.socialmedia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Table (name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_from_id")
    private User userFrom;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_to_id")
    private User userTo;

    private String content;

    @NotNull
    private LocalDateTime created;

}
