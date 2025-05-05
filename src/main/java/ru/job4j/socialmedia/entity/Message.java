package ru.job4j.socialmedia.entity;

import jakarta.persistence.*;
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

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_from_id")
    private User userFrom;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_to_id")
    private User userTo;

    private String content;

    @NonNull
    private LocalDateTime created;

}
