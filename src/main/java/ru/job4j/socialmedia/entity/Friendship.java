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
@Table (name = "friendships")
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user1_id")
    private User user1;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user2_id")
    private User user2;

    @NonNull
    private LocalDateTime created;

}
